package de.einfachpunkt.backend.parser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.models.Rss;

public class RSSFeedParser {

    private URL url;
    private File file;

    public RSSFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public RSSFeedParser(File file) {
        this.file = file;
    }

    public FeedChannel readFeed() {
        FeedChannel feed = null;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String description = "";
            String title = "";
            String link = "";
            String guid = "";
            String language = "";
            String pubDate = "";
            String lastBuildDate = "";
            String image = "";

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    image = "";
                    switch (localPart) {
                        case Rss.ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new FeedChannel(title, link, description, language, pubDate, lastBuildDate);
                            }
                            event = eventReader.nextEvent();
                            break;
                        case Rss.IMAGE:
                            StartElement var = event.asStartElement();
                            image = var.getAttributeByName(new QName("url")).getValue();
                            break;
                        case Rss.TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case Rss.DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                        case Rss.LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case Rss.GUID:
                            guid = getCharacterData(event, eventReader);
                            break;
                        case Rss.LANGUAGE:
                            language = getCharacterData(event, eventReader);
                            break;
                        case Rss.LAST_BUILD_DATE:
                            lastBuildDate = getCharacterData(event, eventReader);
                            break;
                        case Rss.PUB_DATE:
                            pubDate = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(Rss.ITEM)) {
                        FeedItem item = new FeedItem();
                        item.setDescription(description);
                        item.setLink(link);
                        item.setTitle(title);
                        item.setPubDate(pubDate);
                        item.setGuid(guid);
                        item.setImage(image);
                        feed.getItems().add(item);
                        event = eventReader.nextEvent();
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        if (file == null) {
            try {
                return url.openStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


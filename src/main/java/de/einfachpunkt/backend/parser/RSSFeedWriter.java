package de.einfachpunkt.backend.parser;

import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.backend.models.Rss;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;

public class RSSFeedWriter {

    private String outputFile;
    private FeedChannel rssfeed;

    public RSSFeedWriter(FeedChannel rssfeed, String outputFile) {
        this.rssfeed = rssfeed;
        this.outputFile = outputFile;
    }

    public void write() throws Exception {
        // create a XMLOutputFactory
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        // create XMLEventWriter
        XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(outputFile));

        // create a EventFactory
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");

        // create and write Start Tag
        StartDocument startDocument = eventFactory.createStartDocument();
        eventWriter.add(startDocument);

        // create open tag
        eventWriter.add(end);

        StartElement rssStart = eventFactory.createStartElement("", "", "rss");
        eventWriter.add(rssStart);
        eventWriter.add(eventFactory.createAttribute("version", "2.0"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createStartElement("", "", Rss.CHANNEL));
        eventWriter.add(end);

        // Write the different nodes
        createNode(eventWriter, Rss.TITLE, rssfeed.getTitle());
        createNode(eventWriter, Rss.LINK, rssfeed.getLink());
        createNode(eventWriter, Rss.DESCRIPTION, rssfeed.getDescription());
        createNode(eventWriter, Rss.LANGUAGE, rssfeed.getLanguage());
        // createNode(eventWriter, Rss.COPYRIGHT, rssfeed.getCopyright());
        createNode(eventWriter, Rss.PUB_DATE, rssfeed.getPubDate());

        for (FeedItem entry : rssfeed.getNews()) {
            eventWriter.add(eventFactory.createStartElement("", "", Rss.ITEM));
            eventWriter.add(end);
            createNode(eventWriter, Rss.TITLE, entry.getTitle());
            createNode(eventWriter, Rss.DESCRIPTION, entry.getDescription());
            createNode(eventWriter, Rss.LINK, entry.getLink());
            createNode(eventWriter, Rss.AUTHOR, entry.getAuthor());
            createNode(eventWriter, Rss.GUID, entry.getGuid());
            createNode(eventWriter, Rss.IMAGE, entry.getImage());
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndElement("", "", Rss.ITEM));
            eventWriter.add(end);
        }

        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", Rss.CHANNEL));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndElement("", "", "rss"));
        eventWriter.add(end);
        eventWriter.add(eventFactory.createEndDocument());

        eventWriter.close();
    }

    private void createNode(XMLEventWriter eventWriter, String name, String value) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(sElement);
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}
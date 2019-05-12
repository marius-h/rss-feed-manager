package de.einfachpunkt.backend.parser;

import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.backend.models.Rss;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.FileOutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

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
        createNode(eventWriter, Rss.TITLE, rssfeed.getTitle(), null);
        createNode(eventWriter, Rss.LINK, rssfeed.getLink(), null);
        createNode(eventWriter, Rss.DESCRIPTION, rssfeed.getDescription(), null);
        createNode(eventWriter, Rss.LANGUAGE, rssfeed.getLanguage(), null);
        // createNode(eventWriter, Rss.COPYRIGHT, rssfeed.getCopyright());
        createNode(eventWriter, Rss.PUB_DATE, rssfeed.getPubDate(), null);

        for (FeedItem entry : rssfeed.getItems()) {
            eventWriter.add(eventFactory.createStartElement("", "", Rss.ITEM));
            eventWriter.add(end);
            if (entry.getTitle() != null && !entry.getTitle().isEmpty()) {
                createNode(eventWriter, Rss.TITLE, entry.getTitle(), null);
            }
            if (entry.getDescription() != null && !entry.getDescription().isEmpty()) {
                createNode(eventWriter, Rss.DESCRIPTION, entry.getDescription(), null);
            }
            if (entry.getLink() != null && !entry.getLink().isEmpty()) {
                createNode(eventWriter, Rss.LINK, entry.getLink(), null);
            }
            if (entry.getAuthor() != null && !entry.getAuthor().isEmpty()) {
                createNode(eventWriter, Rss.AUTHOR, entry.getAuthor(), null);
            }
            if (entry.getPubDate() != null && !entry.getPubDate().isEmpty()) {
                createNode(eventWriter, Rss.PUB_DATE, entry.getPubDate(), null);
            }
            if (entry.getGuid() != null && !entry.getGuid().isEmpty()) {
                createNode(eventWriter, Rss.GUID, entry.getGuid(), null);
            }
            if (entry.getImage() != null && !entry.getImage().isEmpty()) {
                createNode(eventWriter, Rss.IMAGE, "",
                        new ArrayList<>(Collections.singletonList(new Enclosure(entry.getImage()))).iterator());
            }
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

    private void createNode(XMLEventWriter eventWriter, String name, String value, Iterator attributes) throws XMLStreamException {
        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");
        // create Start node
        eventWriter.add(tab);
        if (attributes != null) {
            StartElement sElement = eventFactory.createStartElement("", "", name, attributes, null);
            eventWriter.add(sElement);
        } else {
            StartElement sElement = eventFactory.createStartElement("", "", name);
            eventWriter.add(sElement);
        }
        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);
        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }
}
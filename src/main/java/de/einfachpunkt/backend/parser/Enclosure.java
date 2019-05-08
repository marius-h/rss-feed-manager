package de.einfachpunkt.backend.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import java.io.Writer;

class Enclosure implements Attribute {

    private String link;

    Enclosure(String link) {
        this.link = link;
    }

    @Override
    public QName getName() {
        return new QName("url");
    }

    @Override
    public String getValue() {
        return link;
    }

    @Override
    public String getDTDType() {
        return null;
    }

    @Override
    public boolean isSpecified() {
        return false;
    }

    @Override
    public int getEventType() {
        return 0;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public boolean isStartElement() {
        return false;
    }

    @Override
    public boolean isAttribute() {
        return true;
    }

    @Override
    public boolean isNamespace() {
        return false;
    }

    @Override
    public boolean isEndElement() {
        return false;
    }

    @Override
    public boolean isEntityReference() {
        return false;
    }

    @Override
    public boolean isProcessingInstruction() {
        return false;
    }

    @Override
    public boolean isCharacters() {
        return false;
    }

    @Override
    public boolean isStartDocument() {
        return false;
    }

    @Override
    public boolean isEndDocument() {
        return false;
    }

    @Override
    public StartElement asStartElement() {
        return null;
    }

    @Override
    public EndElement asEndElement() {
        return null;
    }

    @Override
    public Characters asCharacters() {
        return null;
    }

    @Override
    public QName getSchemaType() {
        return null;
    }

    @Override
    public void writeAsEncodedUnicode(Writer writer) throws XMLStreamException {

    }
}

package de.einfachpunkt.backend.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedItem implements Serializable {

    private String title;
    private String author;
    private String link;
    private String description;
    private String pubDate;
    private String guid;
    private String image;

    @Override
    public String toString() {
        return "FeedItem{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", guid='" + guid + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimeAgo() throws ParseException {
        Date d = new SimpleDateFormat("E, d MMMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(pubDate);
        Date now = new Date();
        int seconds = Math.round(Math.abs((now.getTime() - d.getTime()) / 1000));
        int minutes = Math.round(Math.abs(seconds / 60));
        int hours = Math.round(Math.abs(minutes / 60));
        int days = Math.round(Math.abs(hours / 24));
        long months = Math.round(Math.abs(days / 30.416));
        int years = Math.round(Math.abs(days / 365));

        if (Double.isNaN(seconds)) {
            return "";
        } else if (seconds <= 45) {
            return "Vor einigen Sekunden";
        } else if (seconds <= 90) {
            return "Vor einer Minute";
        } else if (minutes <= 45) {
            return "Vor " + minutes + " Minuten";
        } else if (minutes <= 90) {
            return "Vor einer Stunde";
        } else if (hours <= 22) {
            return "Vor " + hours + " Stunden";
        } else if (hours <= 36) {
            return "Vor einem Tag";
        } else if (days <= 25) {
            return "Vor " + days + " Tagen";
        } else if (days <= 45) {
            return "Vor einem Monat";
        } else if (days <= 345) {
            return "Vor " + months + " Monaten";
        } else if (days <= 545) {
            return "Vor einem Jahr";
        } else { // (days > 545)
            return "Vor " + years + " Jahren";
        }
    }
}

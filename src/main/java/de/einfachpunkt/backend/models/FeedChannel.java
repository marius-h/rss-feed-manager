package de.einfachpunkt.backend.models;

import java.util.ArrayList;
import java.util.List;

/**
 * The channel of an RSS feed
 *
 * @author Marius Höfler
 */
public class FeedChannel {

    private String title;
    private String link;
    private String description;
    private String language;
    private String pubDate;
    private String lastBuildDate;
    private List<FeedItem> items = new ArrayList<>();

    public FeedChannel(String title, String link, String description, String language, String pubDate, String lastBuildDate) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.language = language;
        this.pubDate = pubDate;
        this.lastBuildDate = lastBuildDate;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<FeedItem> getItems() {
        return items;
    }

    public void setItems(List<FeedItem> items) {
        this.items = items;
    }


}

package de.einfachpunkt.ui.views.feedlist;

import de.einfachpunkt.backend.models.FeedItem;

public interface EditItemCallback {

    void onEdit(FeedItem feedItem, int position);
}

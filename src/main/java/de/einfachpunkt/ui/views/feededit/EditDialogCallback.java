package de.einfachpunkt.ui.views.feededit;

import de.einfachpunkt.backend.models.FeedItem;

public interface EditDialogCallback {

    void onSave(FeedItem feedItem, int position);

    void onDelete(int position);

    void onAdd(FeedItem feedItem, int position);
}

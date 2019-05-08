package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.einfachpunkt.backend.models.FeedItem;

import java.text.ParseException;

/**
 * Component for displaying an item of an RSS feed
 */
public class FeedListItem extends VerticalLayout {

    private FeedItem feedItem;

    public FeedListItem(FeedItem feedItem) {
        this.feedItem = feedItem;

        addContent();
    }

    public FeedListItem(FeedItem feedItem, boolean isEditable) {
        this.feedItem = feedItem;
        addContent();
    }

    private void addContent() {
        HorizontalLayout detailContainer = new HorizontalLayout();
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        detailContainer.add(textContainer);
        String imgSrc = feedItem.getImage();
        if (!imgSrc.isEmpty()) {
            Image image = new Image(feedItem.getImage(), "Bild");
            image.setHeight("100px");
            image.setWidth("100px");
            image.addClassName("item_img");
            detailContainer.add(image);
        }
        H3 headline = new H3(feedItem.getTitle());
        textContainer.add(headline);
        try {
            textContainer.add(new H5(feedItem.getTimeAgo()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textContainer.add(new Text(feedItem.getDescription()));

        add(detailContainer);

        if (feedItem.getLink() != null) {
            if (!feedItem.getLink().isEmpty()) {
                Button actionBtn = new Button("Weiterlesen".toUpperCase());
                actionBtn.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
                actionBtn.addClickListener(event -> UI.getCurrent().getPage()
                        .executeJavaScript("window.open(\"" + feedItem.getLink() + "\", \"_self\");"));
                add(actionBtn);
            }
        }
        addClassName("item_card");
    }
}

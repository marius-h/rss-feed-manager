package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.einfachpunkt.backend.models.FeedItem;

public class FeedListItem extends VerticalLayout {

    private FeedItem feedItem;

    public FeedListItem(FeedItem feedItem) {
        this.feedItem = feedItem;

        //System.out.println(feedItem.toString());

        addContent();
    }

    private void addContent() {
        HorizontalLayout detailContainer = new HorizontalLayout();
        VerticalLayout textContainer = new VerticalLayout();
        textContainer.setPadding(false);
        detailContainer.add(textContainer);
        System.out.println(feedItem.getImage());
        if (feedItem.getImage() != null) {
            Image image = new Image(feedItem.getImage(), "test");
            image.setHeight("100px");
            image.setWidth("100px");
            image.addClassName("item_img");
            detailContainer.add(image);
        }
        H4 headline = new H4(feedItem.getTitle());
        textContainer.add(headline);
        textContainer.add(new Text(feedItem.getDescription()));

        Button actionBtn = new Button("Weiterlesen".toUpperCase());
        actionBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(detailContainer, actionBtn);
        addClassName("item_card");
    }
}

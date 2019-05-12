package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import de.einfachpunkt.backend.models.FeedItem;

import java.text.ParseException;

/**
 * Component for displaying an item of an RSS feed
 */
public class FeedListItem extends VerticalLayout {

    private FeedItem feedItem;
    private EditItemCallback editItemCallback;
    private int position;

    public FeedListItem(FeedItem feedItem) {
        this.feedItem = feedItem;
        addContent();
    }

    public FeedListItem(FeedItem feedItem, int position, EditItemCallback editItemCallback) {
        this.feedItem = feedItem;
        this.position = position;
        this.editItemCallback = editItemCallback;
        addContent();
    }

    private void addContent() {
        HorizontalLayout detailContainer = new HorizontalLayout();
        detailContainer.setWidthFull();
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
            H5 time = new H5(feedItem.getTimeAgo());
            time.addClassName("time_style");
            textContainer.add(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        textContainer.add(new Text(feedItem.getDescription()));

        add(detailContainer);

        HorizontalLayout actions = new HorizontalLayout();

        if (feedItem.getLink() != null) {
            if (!feedItem.getLink().isEmpty()) {
                Button actionBtn = new Button("Weiterlesen".toUpperCase());
                actionBtn.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
                actionBtn.addClickListener(event -> UI.getCurrent().getPage()
                        .executeJavaScript("window.open(\"" + feedItem.getLink() + "\", \"_self\");"));
                actions.add(actionBtn);
            }
        }

        if (editItemCallback != null) {
            Button editBtn = new Button(new Icon(VaadinIcon.EDIT));
            editBtn.addClickListener(event -> editItemCallback.onEdit(feedItem, position));
            actions.add(editBtn);
        }

        add(actions);

        addClassName("item_card");
    }
}

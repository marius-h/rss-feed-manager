package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.einfachpunkt.backend.Review;
import de.einfachpunkt.backend.ReviewService;
import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.backend.parser.RSSFeedParser;
import de.einfachpunkt.backend.parser.RSSFeedWriter;
import de.einfachpunkt.ui.MainLayout;
import de.einfachpunkt.ui.common.AbstractEditorDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route (value = "", layout = MainLayout.class)
@PageTitle ("Mein Feed")
public class FeedList extends VerticalLayout {

    private final H2 header = new H2("Mein Feed");
    private final List<Component> listItems = new ArrayList<>();

    private final FeedEditorDialog form = new FeedEditorDialog(
            this::saveCategory, this::deleteItem);

    public FeedList() {
        initView();

        updateView();

        addContent();
    }

    private void initView() {
        addClassName("categories-list");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.CENTER);

        Button reloadBtn = new Button("Aktualisieren");
        reloadBtn.setWidth("600px");
        reloadBtn.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);

        container.add(header, reloadBtn);

        listItems.forEach(container::add);

        add(container);
    }

    private Button createEditButton(FeedItem feedItem) {
        Button editBtn = new Button("Edit Item", event -> form.open(feedItem,
                AbstractEditorDialog.Operation.EDIT));
        editBtn.setIcon(new Icon("lumo", "edit"));
        editBtn.addClassName("review__edit");
        editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        return editBtn;
    }

    private void updateView() {
        FeedChannel rssChannel = getRSSChannel();
        rssChannel.getItems().forEach(item -> listItems.add(new FeedListItem(item)));
        try {
            new RSSFeedWriter(rssChannel, "feed.xml").write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FeedChannel getRSSChannel() {
        //RSSFeedParser parser = new RSSFeedParser(new File("feed.xml"));
        RSSFeedParser parser = new RSSFeedParser("https://www.spiegel.de/international/germany/index.rss");
        return parser.readFeed();
    }

    private void saveCategory(FeedItem feedItem,
                              AbstractEditorDialog.Operation operation) {

        Notification.show(
                "FeedItem successfully " + operation.getNameInText() + "ed.",
                3000, Notification.Position.BOTTOM_START);
        updateView();
    }

    private void deleteItem(FeedItem feedItem) {
        List<Review> reviewsInCategory = ReviewService.getInstance()
                .findReviews(feedItem.getTitle());

        reviewsInCategory.forEach(review -> {
            ReviewService.getInstance().saveReview(review);
        });

        Notification.show("FeedItem successfully deleted.", 3000,
                Notification.Position.BOTTOM_START);
        updateView();
    }
}
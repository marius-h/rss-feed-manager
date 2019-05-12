package de.einfachpunkt.ui.views.feededit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.backend.parser.RSSFeedParser;
import de.einfachpunkt.backend.parser.RSSFeedWriter;
import de.einfachpunkt.ui.MainLayout;
import de.einfachpunkt.ui.views.feedlist.EditItemCallback;
import de.einfachpunkt.ui.views.feedlist.FeedListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route (value = "edit", layout = MainLayout.class)
@PageTitle ("Feed editieren")
public class FeedEdit extends VerticalLayout implements EditDialogCallback, EditItemCallback {

    private static boolean loginNeeded = true;
    private String USERNAME = "hans@die-wursties.de";
    private String PASSWORD = "megaWurst";

    private final List<Component> listItems = new ArrayList<>();
    private final FeedEditDialog form = new FeedEditDialog(this);
    private FeedChannel feedChannel;

    private int btnCount;
    private int itemCount;

    public FeedEdit() {
        if (loginNeeded) {
            showLogin();
        }
        loginNeeded = true;
        initView();
    }

    private void showLogin() {
        LoginOverlay login = new LoginOverlay();
        login.addLoginListener(e -> {
            if (e.getUsername().equals(USERNAME) && e.getPassword().equals(PASSWORD)) {
                login.close();
            } else {
                login.setError(true);
            }
        });
        login.setOpened(true);
        login.setForgotPasswordButtonVisible(false);
        login.setTitle("RSS Feed Login");
        login.setDescription("Melden Sie sich an, um den Feed zu verwalten.");

        LoginI18n i18n = LoginI18n.createDefault();
        login.setI18n(i18n);
    }

    private void initView() {
        setAlignItems(Alignment.CENTER);
        updateView();
    }

    private void updateView() {
        btnCount = 0;
        itemCount = 0;
        listItems.clear();
        feedChannel = getRSSChannel();
        listItems.add(addButton());
        feedChannel.getItems().forEach(item -> {
            listItems.add(new FeedListItem(item, itemCount, this));
            listItems.add(addButton());
            itemCount++;
        });
        listItems.forEach(this::add);
    }

    private Button addButton() {
        Button button = new Button(new Icon(VaadinIcon.PLUS));
        button.setId("" + btnCount);
        button.setMaxWidth("600px");
        button.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        button.addClickListener(event -> {
            if (button.getId().isPresent()) {
                int btnId = Integer.valueOf(button.getId().get());
                System.out.println("open Form " + btnId);
                openForm(null, btnId);
            }
        });
        btnCount++;
        return button;
    }

    private FeedChannel getRSSChannel() {
        RSSFeedParser parser = new RSSFeedParser(new File("feed.xml"));
        return parser.readFeed();
    }

    private void openForm(FeedItem feedItem, int position) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        getUI().ifPresent(ui -> ui.add(form));
        if (form.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(form));
        }
        form.open(feedItem, position);
    }

    private void writeToFile() {
        try {
            new RSSFeedWriter(feedChannel, "feed.xml").write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSave(FeedItem feedItem, int position) {
        Notification.show("Die Nachricht wurde erfolgreich geändert.", 3000,
                Notification.Position.BOTTOM_START);
        List<FeedItem> feedItems = feedChannel.getItems();
        feedItems.set(position, feedItem);
        feedChannel.setItems(feedItems);
        writeToFile();
        loginNeeded = false;
        UI.getCurrent().getPage().reload();
    }

    @Override
    public void onDelete(int position) {
        Notification.show("Die Nachricht wurde erfolgreich gelöscht.", 3000,
                Notification.Position.BOTTOM_START);
        List<FeedItem> feedItems = feedChannel.getItems();
        feedItems.remove(position);
        feedChannel.setItems(feedItems);
        writeToFile();
        loginNeeded = false;
        UI.getCurrent().getPage().reload();
    }

    @Override
    public void onAdd(FeedItem feedItem, int position) {
        Notification.show("Die Nachricht wurde erfolgreich hinzugefügt.", 3000,
                Notification.Position.BOTTOM_START);
        List<FeedItem> feedItems = feedChannel.getItems();
        feedItems.add(position, feedItem);
        feedChannel.setItems(feedItems);
        writeToFile();
        loginNeeded = false;
        UI.getCurrent().getPage().reload();
    }

    @Override
    public void onEdit(FeedItem feedItem, int position) {
        openForm(feedItem, position);
    }
}

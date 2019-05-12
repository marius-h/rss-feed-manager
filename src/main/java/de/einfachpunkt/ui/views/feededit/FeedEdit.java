package de.einfachpunkt.ui.views.feededit;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.parser.RSSFeedParser;
import de.einfachpunkt.ui.MainLayout;
import de.einfachpunkt.ui.views.feedlist.FeedListItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route(value = "edit", layout = MainLayout.class)
@PageTitle("Feed editieren")
public class FeedEdit extends VerticalLayout {
    private String USERNAME = "hans@die-wursties.de";
    private String PASSWORD = "megaWurst";

    private final List<Component> listItems = new ArrayList<>();

    public FeedEdit() {
        showLogin();
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
        login.setTitle("RSS Feed Login");
        login.setDescription("Melden Sie sich an, um den Feed zu verwalten.");

        LoginI18n i18n = LoginI18n.createDefault();
        login.setI18n(i18n);
    }

    private void initView() {
        setAlignItems(Alignment.CENTER);
        add(addButton());
        updateView();
        listItems.forEach(this::add);
    }

    private void updateView() {
        FeedChannel rssChannel = getRSSChannel();
        rssChannel.getItems().forEach(item -> {
            listItems.add(new FeedListItem(item));
            listItems.add(addButton());
        });
    }

    private Button addButton() {
        Button button = new Button(new Icon(VaadinIcon.PLUS));
        button.setMaxWidth("600px");
        button.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        return button;
    }

    private FeedChannel getRSSChannel() {
        RSSFeedParser parser = new RSSFeedParser(new File("feed.xml"));
        return parser.readFeed();
    }
}

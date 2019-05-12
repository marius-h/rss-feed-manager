package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.einfachpunkt.backend.models.FeedChannel;
import de.einfachpunkt.backend.parser.RSSFeedParser;
import de.einfachpunkt.backend.parser.RSSFeedWriter;
import de.einfachpunkt.ui.MainLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Route (value = "", layout = MainLayout.class)
@PageTitle ("Mein Feed")
public class FeedList extends VerticalLayout {

    private final H2 header = new H2("Mein Feed");
    private final List<Component> listItems = new ArrayList<>();

    public FeedList() {
        initView();
    }

    private void initView() {
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);
        updateView();
        addContent();
    }

    private void addContent() {
        VerticalLayout container = new VerticalLayout();
        container.setClassName("view-container");
        container.setAlignItems(Alignment.CENTER);

        Button reloadBtn = new Button("Aktualisieren");
        reloadBtn.setWidth("600px");
        reloadBtn.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        reloadBtn.addClickListener(e -> UI.getCurrent().getPage().reload());

        container.add(header, reloadBtn);

        listItems.forEach(container::add);

        add(container);
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
        RSSFeedParser parser = new RSSFeedParser(new File("feed.xml"));
        //RSSFeedParser parser = new RSSFeedParser("https://www.spiegel.de/international/germany/index.rss");
        return parser.readFeed();
    }
}
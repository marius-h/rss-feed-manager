package de.einfachpunkt.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.PageConfigurator;
import de.einfachpunkt.ui.views.feededit.FeedEdit;
import de.einfachpunkt.ui.views.feedlist.FeedList;

/**
 * The main layout contains the header with the navigation buttons, and the
 * child views below that.
 */
@HtmlImport("frontend://styles/shared-styles.html")
@PWA(name = "RSS Feed Manager", shortName = "RSSManager")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
public class MainLayout extends Div implements RouterLayout, PageConfigurator {

    public MainLayout() {
        H2 title = new H2("RSS Feed");
        title.addClassName("main-layout__title");

        RouterLink reviews = new RouterLink(null, FeedEdit.class);
        reviews.add(new Icon(VaadinIcon.EDIT), new Text("Feed editieren"));
        reviews.addClassName("main-layout__nav-item");
        reviews.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink feed = new RouterLink(null, FeedList.class);
        feed.add(new Icon(VaadinIcon.NEWSPAPER), new Text("Mein Feed"));
        feed.addClassName("main-layout__nav-item");

//        Div navigation = new Div(reviews, categories);
        Div navigation = new Div(feed);
        navigation.addClassName("main-layout__nav");

        Div header = new Div(title, navigation);
        header.addClassName("main-layout__header");
        add(header);

        addClassName("main-layout");
    }

    @Override
    public void configurePage(InitialPageSettings settings) {
        settings.addMetaTag("apple-mobile-web-app-capable", "yes");
        settings.addMetaTag("apple-mobile-web-app-status-bar-style", "black");
    }
}

package de.einfachpunkt.ui.views.feededit;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.einfachpunkt.ui.MainLayout;

@Route(value = "edit", layout = MainLayout.class)
@PageTitle("Feed editieren")
public class FeedEdit extends VerticalLayout {
    private String USERNAME = "hans@die-wursties.de";
    private String PASSWORD = "megaWurst";

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
        add(new Text("Baum"));
    }
}

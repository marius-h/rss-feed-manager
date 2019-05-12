package de.einfachpunkt.ui.views.feededit;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import de.einfachpunkt.backend.models.FeedItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A dialog for editing and adding {@link FeedItem} objects.
 */
public class FeedEditDialog extends Dialog {

    private final FormLayout formLayout = new FormLayout();
    private final TextArea titleField = new TextArea("Titel");
    private final TextArea descriptionField = new TextArea("Beschreibung");
    private final TextField linkField = new TextField("Link");
    private final TextField imageField = new TextField("Bild");
    private final TextField authorField = new TextField("Autor");
    private final Button saveButton = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen");
    private final Button deleteButton = new Button("Löschen");
    private final HorizontalLayout buttonBar = new HorizontalLayout(saveButton,
            cancelButton, deleteButton);

    private FeedItem feedItem;
    private int position;
    private EditDialogCallback editDialogCallback;

    FeedEditDialog(EditDialogCallback editDialogCallback) {
        this.editDialogCallback = editDialogCallback;
        initForm();
    }

    public void open(FeedItem feedItem, int position) {
        this.feedItem = feedItem;
        this.position = position;
        initFieldValues();
        open();
    }

    private void initForm() {
        addFields();
        initFieldValues();
        initButtonBar();
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
    }

    private void initFieldValues() {
        titleField.setValue("");
        descriptionField.setValue("");
        linkField.setValue("");
        imageField.setValue("");
        authorField.setValue("");
        if (feedItem != null) {
            String title = feedItem.getTitle();
            String description = feedItem.getDescription();
            String link = feedItem.getLink();
            String image = feedItem.getImage();
            String author = feedItem.getAuthor();
            if (title != null) {
                titleField.setValue(title);
            }
            if (description != null) {
                descriptionField.setValue(description);
            }
            if (link != null) {
                linkField.setValue(link);
            }
            if (image != null) {
                imageField.setValue(image);
            }
            if (author != null) {
                authorField.setValue(author);
            }
        }
    }

    private void addFields() {
        titleField.setRequired(true);
        descriptionField.setRequired(true);
        titleField.setClearButtonVisible(true);
        descriptionField.setClearButtonVisible(true);
        linkField.setClearButtonVisible(true);
        imageField.setClearButtonVisible(true);
        authorField.setClearButtonVisible(true);
        formLayout.add(titleField, descriptionField, linkField, imageField, authorField);
        Div div = new Div(formLayout);
        add(div);
    }

    private void initButtonBar() {
        saveButton.setAutofocus(true);
        saveButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        cancelButton.addClickListener(e -> close());
        deleteButton.addClickListener(e -> deleteItem());
        saveButton.addClickListener(e -> {
            if (feedItem == null) {
                addItem();
            } else {
                saveChanges();
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonBar.setClassName("buttons");
        buttonBar.setSpacing(true);
        add(buttonBar);
    }

    private void toItem() {
        feedItem = new FeedItem();
        feedItem.setTitle(titleField.getValue());
        feedItem.setDescription(descriptionField.getValue());
        feedItem.setLink(linkField.getValue());
        feedItem.setImage(imageField.getValue());
        feedItem.setAuthor(authorField.getValue());
    }

    private void saveChanges() {
        toItem();
        editDialogCallback.onSave(feedItem, position);
        close();
    }

    private void deleteItem() {
        editDialogCallback.onDelete(position);
        close();
    }

    private void addItem() {
        toItem();

        // set pubDate to the current date and time
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, d MMMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        String dateString = dateFormat.format(date);
        feedItem.setPubDate(dateString);

        editDialogCallback.onAdd(feedItem, position);
        close();
    }
}

package de.einfachpunkt.ui.views.feedlist;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.StringLengthValidator;
import de.einfachpunkt.backend.ReviewService;
import de.einfachpunkt.backend.models.FeedItem;
import de.einfachpunkt.ui.common.AbstractEditorDialog;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A dialog for editing {@link FeedItem} objects.
 */
public class FeedEditorDialog extends AbstractEditorDialog<FeedItem> {

    private final TextField itemTitleField = new TextField("Title");

    public FeedEditorDialog(BiConsumer<FeedItem, Operation> itemSaver,
                            Consumer<FeedItem> itemDeleter) {
        super("item", itemSaver, itemDeleter);

        addTitleField();
    }

    private void addTitleField() {
        getFormLayout().add(itemTitleField);

//        getBinder().forField(itemTitleField)
//                .withConverter(String::trim, String::trim)
//                .withValidator(new StringLengthValidator(
//                        "FeedItem Title must contain at least 3 printable characters",
//                        3, null))
//                .withValidator(
//                        Title -> CategoryService.getInstance()
//                                .findCategories(Title).size() == 0,
//                        "FeedItem Title must be unique")
//                .bind(FeedItem::getTitle, FeedItem::setTitle);
    }

    @Override
    protected void confirmDelete() {
        int reviewCount = ReviewService.getInstance()
                .findReviews(getCurrentItem().getTitle()).size();
        if (reviewCount > 0) {
            openConfirmationDialog("Delete category",
                    "Are you sure you want to delete the “"
                            + getCurrentItem().getTitle()
                            + "” category? There are " + reviewCount
                            + " reviews associated with this category.",
                    "Deleting the category will mark the associated reviews as “undefined”. "
                            + "You can edit individual reviews to select another category.");
        } else {
            doDelete(getCurrentItem());
        }
    }
}

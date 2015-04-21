package ru.ncedu.onlineshop.vaadin.componentlayouts.common;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.vaadin.ShopUI;

/**
 * Created by nikita on 04.02.15.
 */
public class DoubleButtonsDialog extends HorizontalLayout {
    protected Button confirmButton;
    protected Button cancelButton;

    public DoubleButtonsDialog(String confirmButtonName, String cancelButtonName) {
        confirmButton = new Button(confirmButtonName);
        cancelButton = new Button(cancelButtonName);
        addComponents(confirmButton, cancelButton);
        applyStyle();
    }

    private void applyStyle() {
        setImmediate(true);
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //setSizeFull();
        setWidth("100%");
        setHeightUndefined();

        cancelButton.setStyleName(ValoTheme.BUTTON_SMALL);
        confirmButton.setStyleName(ValoTheme.BUTTON_SMALL);
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}

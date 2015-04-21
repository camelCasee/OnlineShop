package ru.ncedu.onlineshop.vaadin.componentlayouts.common;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;

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
        setStyleName("outlined");
        //setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //setSizeFull();
        setWidth("100%");
        setHeightUndefined();
    }

    public Button getConfirmButton() {
        return confirmButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }
}

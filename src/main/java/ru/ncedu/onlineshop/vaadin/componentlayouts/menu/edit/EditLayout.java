package ru.ncedu.onlineshop.vaadin.componentlayouts.menu.edit;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;

/**
 * Created by nikita on 08.02.15.
 */
public class EditLayout extends HorizontalLayout {
    protected Button deleteTypeButton;
    protected Button editTypeButton;
    protected Button addTypeButton;
    protected ProductTypeTreeLayout parentLayout;

    public EditLayout(ProductTypeTreeLayout parentLayout) {
        this.parentLayout = parentLayout;
        setupComponents();
        setupLayout();
        addComponents(addTypeButton, editTypeButton/*, deleteTypeButton*/);
    }

    private void setupComponents() {
//        deleteTypeButton = new Button("Delete");
//        addDeleteButtonClickListener();
        editTypeButton = new Button("Edit");
        editTypeButton.setStyleName(ValoTheme.BUTTON_SMALL);
        addEditButtonClickListener();
        addTypeButton = new Button("Add");
        addTypeButton.setStyleName(ValoTheme.BUTTON_SMALL);
        addAddButtonClickListener();
    }

//    private void addDeleteButtonClickListener() {
//        deleteTypeButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                parentLayout.removeComponent(EditLayout.this);
//                ProductType productType = (ProductType) parentLayout.getTree().getValue();
//                parentLayout.addComponentAsFirst(new EditTypeLayout(productType, parentLayout));
//            }
//        });
//    }

    private void addEditButtonClickListener() {
        editTypeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ProductType productType = (ProductType) parentLayout.getTree().getValue();
                if (productType == null) {
                    Notification.show("Select type, please!");
                    return;
                }
                parentLayout.addComponentAsFirst(new EditTypeLayout(productType, parentLayout));
                parentLayout.removeComponent(EditLayout.this);
            }
        });
    }

    private void addAddButtonClickListener() {
        addTypeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                parentLayout.addComponentAsFirst(new CreateTypeLayout(parentLayout));
                parentLayout.removeComponent(EditLayout.this);
            }
        });
    }

    private void setupLayout() {
        //setStyleName("outlined");
        setImmediate(true);
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        setWidthUndefined();
//        setSizeFull();
//        setWidth("100%");
//        setHeightUndefined();
    }

}

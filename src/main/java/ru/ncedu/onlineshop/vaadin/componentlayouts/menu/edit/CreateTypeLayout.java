package ru.ncedu.onlineshop.vaadin.componentlayouts.menu.edit;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;

/**
 * Created by nikita on 09.02.15.
 */
public class CreateTypeLayout extends EditTypeLayout {

    public CreateTypeLayout(ProductTypeTreeLayout parentLayout) {
        super(new ProductType("new product type"), parentLayout);
        type.setParentType(nullSelectionProductType);
    }

    @Override
    protected void addCancelingListener() {
        buttonsDialog.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                replaceThisLayoutWithEditLayout();
            }
        });
    }

    @Override
    protected void addConfirmationListener() {
        buttonsDialog.getConfirmButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (typeTextField.getValue().equals("")) {
                    Notification.show("Input, please, the name for editing type!");
                    return;
                }

                type.setName(typeTextField.getValue());

                if (type.getParentType() == nullSelectionProductType) {
                    type.setParentType(null);
                }

                type = serviceAPI.addProductType(type);
                replaceThisLayoutWithEditLayout();
                parentLayout.updateTree();
            }
        });
    }

    @Override
    protected void replaceThisLayoutWithEditLayout() {
        parentLayout.addComponentAsFirst(new EditLayout(parentLayout));
        parentLayout.removeComponent(this);
    }
}

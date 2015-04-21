package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.ProductListLayout;

/**
 * Created by nikita on 02.02.15.
 */
public class EditableProductLayout extends InformationalProductLayout {
    protected Button editButton;
    protected Button deleteButton;
    private ProductListLayout parent;

    public EditableProductLayout(Product product, ProductListLayout productListLayout) {
        super(product);
        parent = productListLayout;
        addEditAndDeleteButtons();
    }

    private void addEditAndDeleteButtons() {
        editButton = new Button("Edit");
        deleteButton = new Button("Delete");
        editButton.setStyleName(ValoTheme.BUTTON_TINY);
        deleteButton.setStyleName(ValoTheme.BUTTON_TINY);
        nameLayout.addComponents(editButton, deleteButton);
        //nameLayout.setMargin(true);
        nameLayout.setSpacing(true);
        nameLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        //nameLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addEditButtonClickListener();
        addDeleteButtonClickListener();
    }

    private void addEditButtonClickListener()  {
        editButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                replaceEditableProductLayoutWithEditProductLayout();
            }
        });
    }

    protected void addDeleteButtonClickListener() {
        deleteButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                parent.removeProductByLayout(EditableProductLayout.this);
            }
        });
    }

    private void replaceEditableProductLayoutWithEditProductLayout() {
        Integer index = parent.getComponentIndex(this);
        parent.addComponent(new EditProductLayout(product, parent),  index);
        parent.removeComponent(this);
    }

}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts;

import com.vaadin.ui.Button;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.CreateProductLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.EditableProductLayout;

/**
 * Created by nikita on 03.02.15.
 */
public class EditableProductsListOfTypeLayout extends ProductListOfTypeLayout {
    private Button addProductButton;
    public EditableProductsListOfTypeLayout(ProductType productType, ServiceAPI service) {
        super(productType, service);
        setupAddButton();
        addComponentAsFirst(addProductButton);
    }

    private void setupAddButton() {
        addProductButton = new Button("Add");
        //addProductButton.setSizeFull();
        addProductButton.setWidth("30%");
        addProductButton.setHeightUndefined();
        addAddButtonClickListener();
    }

    private void addAddButtonClickListener() {
        addProductButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addComponent(new CreateProductLayout(productType, EditableProductsListOfTypeLayout.this), 1);
            }
        });
    }

    @Override
    public void addProductLayouts() {

        for (Product product: products) {
            addComponent(new EditableProductLayout(product, this));
        }
    }
}

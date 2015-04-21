package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.ui.Button;
import ru.ncedu.onlineshop.entity.product.*;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.ProductListLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikita on 07.02.15.
 */
public class CreateProductLayout extends EditProductLayout {
    public CreateProductLayout(ProductType productType, ProductListLayout productListLayout) {
        super(createEmptyProductOfType(productType), productListLayout);
        List<Manufacturer> manufacturers = serviceAPI.getManufacturers();
        if (manufacturers.size() != 0) {
            product.setManufacturer(manufacturers.get(0));
        }
    }

    private static Product createEmptyProductOfType(ProductType productType) {
        return new Product(productType, null, new ProductSize(0, 0, 0), new ArrayList<Parameter>(0), 0, "name", 0);
    }

    @Override
    protected void addCancelingListener() {
        doubleButtonsDialog.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeComponent(CreateProductLayout.this);
            }
        });
    }

    @Override
    protected void addConfirmationListener() {
        doubleButtonsDialog.getConfirmButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveChanges();
            }
        });
    }

    @Override
    protected void saveChanges() {
        updateProductFields();
        product = serviceAPI.addProduct(product);
        replaceEditProductLayoutWithEditableProductLayout();
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage;

import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductStorage;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class StorageLayout extends ParameterLayout {
    private ProductStorage productStorage;

    public StorageLayout(ProductStorage productStorage) {
        super();
    }

    public abstract int getProductQuantity();
    public abstract void setProductQuantity (int quantity);
}

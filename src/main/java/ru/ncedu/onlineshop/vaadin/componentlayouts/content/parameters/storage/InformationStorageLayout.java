package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage;

import com.vaadin.ui.Label;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.entity.product.ProductStorage;

/**
 * Created by ali on 14.02.15.
 */
public class InformationStorageLayout extends StorageLayout {

    private Label quantityLabel;

    public InformationStorageLayout(ProductStorage productStorage){
        super(productStorage);

        quantityLabel = new Label("Available quantity: "+ productStorage.getQuantity());
        addComponent(quantityLabel);
    }

    @Override
    public int getProductQuantity() {
        return Integer.parseInt(quantityLabel.getValue());
    }

    @Override
    public void setProductQuantity(int quantity) {
        quantityLabel.setValue(String.valueOf(quantity));
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage;

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.entity.product.ProductStorage;

/**
 * Created by ali on 14.02.15.
 */
public class EditableStorageLayout extends StorageLayout {

    protected TextField quantityTextField;

    protected Button changeQuantityButton;

    public EditableStorageLayout(ProductStorage productStorage) {
        super(productStorage);
        quantityTextField = new TextField("Quantity:", String.valueOf(productStorage.getQuantity()));
        quantityTextField.setReadOnly(false);
        changeQuantityButton = new Button("Change quantity: ");
        addComponent(quantityTextField);
    }

    @Override
    public int getProductQuantity() {
        return Integer.valueOf(quantityTextField.getValue());
    }

    @Override
    public void setProductQuantity(int quantity) {
        quantityTextField.setValue(String.valueOf(quantity));
    }
}

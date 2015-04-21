package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer;

import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.product.Manufacturer;

/**
 * Created by ali on 14.02.15.
 */
public class EditableManufacturerLayout extends ManufacturerLayout {

    protected TextField nameTextField;
    protected TextField countryTextField;

    public EditableManufacturerLayout(Manufacturer manufacturer) {
        super(manufacturer);
        nameTextField = new TextField("Name:", manufacturer.getName());
        nameTextField.setReadOnly(false);
        countryTextField = new TextField("Country:", manufacturer.getCountry());
        countryTextField.setReadOnly(false);
        addComponent(nameTextField);
        addComponent(countryTextField);
    }

    @Override
    public String getManufacturerName() {
        return nameTextField.getValue();
    }

    @Override
    public void setManufacturerName(String name) {
        nameTextField.setValue(name);
    }

    @Override
    public String getManufacturerCountry() {
        return countryTextField.getValue();
    }

    @Override
    public void setManufacturerCountry(String country) {
        countryTextField.setValue(country);
    }
}

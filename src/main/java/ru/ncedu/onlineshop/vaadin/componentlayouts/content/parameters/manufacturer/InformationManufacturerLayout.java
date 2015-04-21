package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer;

import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.product.Manufacturer;

/**
 * Created by ali on 14.02.15.
 */
public class InformationManufacturerLayout extends ManufacturerLayout {

    private Label nameLabel;
    private Label countryLabel;

    public InformationManufacturerLayout(Manufacturer manufacturer){
        super(manufacturer);

        nameLabel = new Label("Name: "+ manufacturer.getName());
        countryLabel = new Label("Country: " + manufacturer.getCountry());
        addComponent(nameLabel);
        addComponent(countryLabel);
    }

    @Override
    public String getManufacturerName() {
        return nameLabel.getValue();
    }

    @Override
    public void setManufacturerName(String name) {
        nameLabel.setValue(name);
    }

    @Override
    public String getManufacturerCountry() {
        return countryLabel.getValue();
    }

    @Override
    public void setManufacturerCountry(String country) {
        countryLabel.setValue(country);
    }
}

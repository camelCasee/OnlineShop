package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer;

import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class ManufacturerLayout extends ParameterLayout {
    protected Manufacturer manufacturer;

    public ManufacturerLayout(Manufacturer manufacturer) {
        super();
        this.manufacturer = manufacturer;
    }

    public abstract String getManufacturerName();
    public abstract void setManufacturerName (String name);
    public abstract String getManufacturerCountry();
    public abstract void setManufacturerCountry (String country);
}

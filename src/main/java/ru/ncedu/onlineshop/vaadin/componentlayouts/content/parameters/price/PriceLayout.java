package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price;

import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class PriceLayout extends ParameterLayout {
    public PriceLayout() {
        super();
    }

    public abstract Double getPrice();
    public abstract void setPrice(Double price);
}

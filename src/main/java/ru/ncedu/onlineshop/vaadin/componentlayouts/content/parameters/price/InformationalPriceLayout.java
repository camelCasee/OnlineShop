package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price;

import com.vaadin.ui.Label;
import ru.ncedu.onlineshop.service.ServiceAPI;

/**
 * Created by nikita on 04.02.15.
 */
public class InformationalPriceLayout extends PriceLayout {
    protected Label priceLabel;

    public InformationalPriceLayout(Double price) {
        super();
        priceLabel = new Label(price.toString());
        addComponent(priceLabel);
    }

    @Override
    public Double getPrice() {
        return Double.parseDouble(priceLabel.getValue());
    }

    @Override
    public void setPrice(Double price) {
        priceLabel.setValue(price.toString());
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price;

import com.vaadin.ui.Label;

/**
 * Created by nikita on 04.02.15.
 */
public class InformationalPriceLayout extends PriceLayout {
    protected Label priceLabel;
    protected double price;

    public InformationalPriceLayout(Double price) {
        this.price = price;
        priceLabel = new Label("Price: " + price.toString());
        addComponent(priceLabel);
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
        priceLabel.setValue("Price: " + price.toString());
    }
}

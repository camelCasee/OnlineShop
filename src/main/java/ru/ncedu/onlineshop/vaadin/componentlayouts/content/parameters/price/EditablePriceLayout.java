package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price;

import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.service.ServiceAPI;

/**
 * Created by nikita on 04.02.15.
 */
public class EditablePriceLayout extends PriceLayout {
    protected TextField priceTextFiled;

    public EditablePriceLayout(Double price) {
        super();
        priceTextFiled = new TextField("Price:", price.toString());
        priceTextFiled.setReadOnly(false);
        addComponent(priceTextFiled);
    }

    @Override
    public Double getPrice() {
        return Double.parseDouble(priceTextFiled.getValue());
    }

    @Override
    public void setPrice(Double price) {
        priceTextFiled.setValue(price.toString());
    }
}

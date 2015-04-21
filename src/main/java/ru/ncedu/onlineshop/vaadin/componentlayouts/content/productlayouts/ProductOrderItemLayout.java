package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.product.ProductStorage;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.quantity.QuantityLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.OrderProductListLayout;

/**
 * Created by ali on 31.01.15.
 */
public class ProductOrderItemLayout extends InformationalProductLayout /*ProductLayout*/{
    private OrderItem orderItem;
    private QuantityLayout quantityLayout;
    //private TextField productQuantity;

    public ProductOrderItemLayout(final OrderItem orderItem, final OrderProductListLayout parentProductList) {
        super(orderItem.getProduct());
        this.orderItem = orderItem;
        quantityLayout = new QuantityLayout(orderItem.getQuantity());
        addProductQuantityValueChangeListener(parentProductList);
        addComponent(quantityLayout);
        ProductStorage productStorage = orderItem.getProduct().getProductStorage();
        if (productStorage.getQuantity() == 0){
            quantityLayout.getQuantityTextEdit().setValue(String.valueOf(0));
            quantityLayout.getQuantityTextEdit().setReadOnly(true);
        } else {
            quantityLayout.getValidator().setMaxValue(productStorage.getQuantity());
        }
    }

    private void addProductQuantityValueChangeListener(final OrderProductListLayout parentProductList) {
        quantityLayout.getQuantityTextEdit().addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                int quantity = checkProductQuantity();
                if (quantity >= 0) {
                    orderItem.setQuantity(quantity);
                    parentProductList.changeTotalPrice();
                }
            }
        });
    }

    public int checkProductQuantity() {
        try {
            Integer res = Integer.parseInt(quantityLayout.getQuantityTextEdit().getValue());
            quantityLayout.getQuantityTextEdit().validate();
            return res;
        } catch (Validator.InvalidValueException e) {
            quantityLayout.getQuantityTextEdit().setValue(
                    String.valueOf(quantityLayout.getValidator().getMaxValue()));
            return -1;
        } catch (NumberFormatException e){
            quantityLayout.getQuantityTextEdit().setValue(
                    String.valueOf(quantityLayout.getValidator().getMinValue()));
            return -2;
        }
    }
}

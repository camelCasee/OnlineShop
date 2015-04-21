package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.OrderProductListLayout;

/**
 * Created by ali on 02.02.15.
 */
public class ProductOrderItemViewLayout extends InformationalProductLayout {
    private OrderItem orderItem;
    protected TextField productQuantity;

    public ProductOrderItemViewLayout(final OrderItem orderItem, final OrderProductListLayout parentProductList) {
        super(orderItem.getProduct());
        this.orderItem = orderItem;

        productQuantity = new TextField("Quantity:", String.valueOf(orderItem.getQuantity()));
//        productQuantity.setConverter(new StringToIntegerConverter());
//        productQuantity.addValidator(new IntegerRangeValidator("Input integer value!", 0, 10000));
        productQuantity.setReadOnly(true);

        nameLayout.addComponent(productQuantity);
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts;

import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.order.OrderState;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.ProductOrderItemLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.ProductOrderItemViewLayout;

import java.util.List;

/**
 * Created by ali on 31.01.15.
 */
public class OrderProductListLayout extends ProductListLayout {
    private Order order;

    public OrderProductListLayout(Order order, ServiceAPI service) {
        super();
        this.order = order;
//        products = service.getOrderProductList(order);
        param = 4;
        addProductLayouts();
    }

    @Override
    public void addProductLayouts() {
        if (param == 4) {
            List<OrderItem> orderItemList = order.getOrderItemList();
            if (order.getState() == OrderState.PROCESSING) {
                for (int i = 0; i < orderItemList.size(); i++)
                    addComponent(new ProductOrderItemLayout(orderItemList.get(i), this));
            } else {
                for (int i = 0; i < orderItemList.size(); i++)
                    addComponent(new ProductOrderItemViewLayout(orderItemList.get(i), this));
            }
        }
    }

    public void changeTotalPrice(){
        order.notifyObservers();
    }

    public boolean checkBeforeFormalizationOrder(){
        for (int i=0; i<components.size(); i++){
            if (components.get(i) instanceof ProductOrderItemLayout){
                ProductOrderItemLayout productOrderItemLayout =
                        (ProductOrderItemLayout)components.get(i);
                if (productOrderItemLayout.checkProductQuantity()<0)
                    return false;
            }
        }
        return true;
    }


}

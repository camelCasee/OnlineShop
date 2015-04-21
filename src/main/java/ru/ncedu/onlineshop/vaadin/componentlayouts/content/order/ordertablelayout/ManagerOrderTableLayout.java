package ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout;

import com.vaadin.event.ItemClickEvent;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.orderviewlayout.ManagerOrderViewLayout;

import java.util.List;

/**
 * Created by ali on 05.02.15.
 */
public class ManagerOrderTableLayout extends OrderTableLayout {

    private ManagerOrderViewLayout orderViewLayout;

    public ManagerOrderTableLayout(ServiceAPI serviceAPI, OrderService orderService) {
        super(serviceAPI, orderService);
        updateOrders();
    }

    @Override
    public void addOrderViewLayout(Order order){
//        orderList.set(orderList.indexOf(findOrderWithSameId(order.getId())),orderService.getOrderWithItems(order));
        if (orderViewLayout != null)
            removeComponent(orderViewLayout);
        orderViewLayout = new ManagerOrderViewLayout(order, serviceAPI, orderService, this);
        addComponent(orderViewLayout);
        orderViewLayout.setWidth("50%");
    }

    @Override
    protected void removeOrderViewLayout() {
        if (orderViewLayout != null) {
            removeComponent(orderViewLayout);
            orderViewLayout = null;
        }
    }

    @Override
    public void updateOrders() {
        Order selectedOrder = null;
        if (orderTable.getValue() != null){
            selectedOrder = (Order)orderTable.getValue();
//            System.out.println("DEBUG OF ORDER TABLE"+orderTable.getValue().toString());
        }
        orderContainer.removeAllItems();
        orderList = orderService.getOrders();
        orderContainer.addAll(orderList);
        if (selectedOrder != null){
            Order newSelectedOrder = findOrderWithSameId(selectedOrder.getId());
            orderTable.setValue(newSelectedOrder);
        }
    }
}

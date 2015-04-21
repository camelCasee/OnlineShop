package ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout;

import com.vaadin.event.ItemClickEvent;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.orderviewlayout.UserOrderViewLayout;

/**
 * Created by ali on 05.02.15.
 */
public class UserOrderTableLayout extends OrderTableLayout {

    private UserOrderViewLayout orderViewLayout;
    private User user;

    public UserOrderTableLayout(ServiceAPI serviceAPI, OrderService orderService, User user) {
        super(serviceAPI, orderService);
        this.user = user;
        updateOrders();
    }

    @Override
    public void addOrderViewLayout(Order order){
        if (orderViewLayout != null)
            removeComponent(orderViewLayout);
        orderViewLayout = new UserOrderViewLayout(order, serviceAPI, orderService, this);
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
        }
        orderContainer.removeAllItems();
        orderList = orderService.getOrdersOfUser(user);

        for (int i=0; i<orderList.size(); i++)
            System.out.println(orderList.get(i).getUser().toString());

        orderContainer.addAll(orderList)

        ;
        if (selectedOrder != null){
            Order newSelectedOrder = findOrderWithSameId(selectedOrder.getId());
            orderTable.setValue(newSelectedOrder);
        }
    }


}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.orderviewlayout;

import com.vaadin.ui.*;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout.OrderTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.OrderProductListLayout;

import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;

/**
 * Created by ali on 02.02.15.
 */
public class ManagerOrderViewLayout extends UserOrderViewLayout {

    protected Button deliveryOrderButton;
    protected Button completeOrderButton;

    public ManagerOrderViewLayout(Order handledOrder, ServiceAPI serviceAPI, final OrderService orderService, final OrderTableLayout parentPage) {
        super(handledOrder, serviceAPI, orderService, parentPage);

        deliveryOrderButton = new Button("Delivery order", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    order = orderService.deliverOrder(order);
                    changeButtons();
                    parentPage.updateOrders();
                } catch (OptimisticLockException e){
                    e.printStackTrace();
                    Notification.show("Order handling exception!");
                } catch (IncorrectStateException e) {
                    e.printStackTrace();
                    Notification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            }
        });

        completeOrderButton = new Button("Complete order", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    order = orderService.completeOrder(order);
                    parentPage.updateOrders();
                    changeButtons();
                } catch (OptimisticLockException e){
                    e.printStackTrace();
                    Notification.show("Order handling exception!");
                } catch (IncorrectStateException e) {
                    e.printStackTrace();
                }
            }
        });

        changeButtons();
    }

    @Override
    protected void addProductOrderView() {
        OrderProductListLayout productOrderViewLayout = new OrderProductListLayout(order, serviceAPI);
        addComponent(productOrderViewLayout);
    }

    private void changeButtons(){
        buttonsLayout.removeAllComponents();
        switch (order.getState()) {
            case CONFIRMED:
                buttonsLayout.addComponent(cancelOrderButton);
                buttonsLayout.addComponent(deliveryOrderButton);
                break;
            case DELIVERING:
                buttonsLayout.addComponent(cancelOrderButton);
                buttonsLayout.addComponent(completeOrderButton);
                break;
        }
        buttonsLayout.addComponent(renewOrderButton);
    }
}

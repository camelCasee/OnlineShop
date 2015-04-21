package ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.observerpattern.Observable;
import ru.ncedu.onlineshop.observerpattern.Observer;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.page.UserPage;

/**
 * Created by ali on 01.01.15.
 */
public class ShoppingCartLayout extends VerticalLayout implements Observer{
    private OrderService orderService;

    private Integer totalPrice = new Integer(0);

    private UserPage userPage;

    // components
    private Label totalPriceLabel = new Label(totalPrice.toString());
    private Button orderFormalizationButton = new Button("View order");

    public ShoppingCartLayout(OrderService orderService, UserPage parent){
//        OrderService orderService = (OrderService) context.getBean("orderService");
//        UserService userService = (UserService) context.getBean("userService");
//        order = (Order)context.getBean("order");

        this.orderService = orderService;
        this.userPage = parent;
        Order order = orderService.getCurrentOrder();
        order.registerObserver(this);
        notifyObserver(order, null);
        addComponent(totalPriceLabel);
        orderFormalizationButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                userPage.addOrderFormalizationLayout();
            }
        });
        addComponent(orderFormalizationButton);
    }

    @Override
    public void notifyObserver(Observable subject, Object arg) {
        if (subject instanceof Order){
            totalPrice = ((Order)subject).getTotalPrice();
            totalPriceLabel.setValue(totalPrice.toString());
        } else {
            Notification.show("Error!");
        }
    }

}

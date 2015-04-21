package ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart;

import com.vaadin.ui.*;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.observerpattern.Observable;
import ru.ncedu.onlineshop.observerpattern.Observer;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.page.UserPage;

/**
 * Created by ali on 01.01.15.
 */
public class ShoppingCartLayout extends VerticalLayout implements Observer{
    private OrderService orderService;

    private Integer totalPrice = new Integer(0);

    private UserPage userPage;

    // components
    private Label totalPriceLabel = new Label("Sum: " + totalPrice.toString());
    private Button orderFormalizationButton = new Button("View order");

    public ShoppingCartLayout(OrderService orderService, UserPage parent){
//        OrderService orderService = (OrderService) context.getBean("orderService");
//        UserService userService = (UserService) context.getBean("userService");
//        order = (Order)context.getBean("order");

        this.orderService = orderService;
        this.userPage = parent;

        setupThisComponent();

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

    private void setupThisComponent() {
        setImmediate(true);
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setWidth("100%");
        setHeightUndefined();
    }

    @Override
    public void notifyObserver(Observable subject, Object arg) {
        if (subject instanceof Order){
            totalPrice = ((Order)subject).getTotalPrice();
            totalPriceLabel.setValue("Sum: " + totalPrice.toString());
        } else {
            Notification.show("Error!");
        }
    }

}

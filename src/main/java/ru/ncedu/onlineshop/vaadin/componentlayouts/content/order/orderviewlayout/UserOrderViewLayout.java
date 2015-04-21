package ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.orderviewlayout;

import com.rits.cloning.Cloner;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderState;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout.OrderTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.OrderProductListLayout;

import javax.persistence.OptimisticLockException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ali on 02.02.15.
 */
public class UserOrderViewLayout extends VerticalLayout {
    protected Order order;
    protected ServiceAPI serviceAPI;
    protected OrderService orderService;

    protected Button renewOrderButton;
    protected Button cancelOrderButton;

    protected HorizontalLayout buttonsLayout = new HorizontalLayout();

    public UserOrderViewLayout(Order handledOrder, ServiceAPI serviceAPI, final OrderService orderService, final OrderTableLayout parentPage) {
        this.order = handledOrder;
        this.serviceAPI = serviceAPI;
        this.orderService = orderService;
        addOrderInfoText();
        addProductOrderView();

        setSpacing(true);
        setMargin(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setWidth("100%");

        addComponent(buttonsLayout);

        buttonsLayout.setSpacing(true);
        buttonsLayout.setMargin(true);
        buttonsLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        buttonsLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        buttonsLayout.setWidth("100%");

        renewOrderButton = new Button("Renew order", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    Cloner cloner = new Cloner();
                    Order newOrder = cloner.deepClone(order);
                    System.out.println("ADASD"+order.toString());
                    System.out.println(newOrder.toString());
                    orderService.getCurrentOrder().fillContent(newOrder);
                } catch (OptimisticLockException e){
                    e.printStackTrace();
                    Notification.show("Order handling exception!");
                }
            }
        });
        renewOrderButton.setStyleName(ValoTheme.BUTTON_SMALL);

        cancelOrderButton = new Button("Cancel order", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                try {
                    order = orderService.cancelOrder(order);
                    changeButtons();
                    parentPage.updateOrders();
                } catch (OptimisticLockException e){
                    e.printStackTrace();
                    Notification.show("Order handling exception!");
                } catch (IncorrectStateException e) {
                    e.printStackTrace();
                }
            }
        });
        cancelOrderButton.setStyleName(ValoTheme.BUTTON_SMALL);
        changeButtons();
    }

    protected void addProductOrderView() {
        OrderProductListLayout productOrderViewLayout = new OrderProductListLayout(order, serviceAPI);
        productOrderViewLayout.setWidth("100%");
        addComponent(productOrderViewLayout);
    }

    protected void addOrderInfoText() {
        TextArea textArea = new TextArea(
                "Order info", "Order №: " + order.getId() + "\n" +
                "Delivery method: "+ order.getDeliveryMethod().toString() +"\n"+
                "Delivery address: " + order.getDeliveryAddress()+"\n"+
                "Contact phone: " + order.getPhoneNumber() +"\n"+
                "Email: " + order.getContactEmail()+"\n"+
                "Contact name: " + order.getContactName()+"\n"+
                "Total price: " + order.getTotalPrice()+"\n"+
                "Creation date: " + convertFromCalendarToString(order.getCreationDate())+"\n"+
                "Confirmation date: " + convertFromCalendarToString(order.getConfirmationDate()) +"\n"+
                "Delivery date: " + convertFromCalendarToString(order.getDeliveryStartDate())+"\n"+
                "Completion date: " + convertFromCalendarToString(order.getCompletionDate())
        );
        textArea.setRows(11);
        textArea.setWidth("90%");
        textArea.setReadOnly(true);
        textArea.setWordwrap(false);
        textArea.addStyleName(ValoTheme.TEXTAREA_BORDERLESS);
        addComponent(textArea);
    }

    private void changeButtons(){
        buttonsLayout.removeAllComponents();
        if (order.getState() == OrderState.CONFIRMED) {
            buttonsLayout.addComponent(cancelOrderButton);
        }
        buttonsLayout.addComponent(renewOrderButton);

    }

    private String convertFromCalendarToString(Calendar calendar){
        if (calendar == null)
            return "null";
        Date dateValue = calendar.getTime();//"Formatted date: " +
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateValue);
    }
}

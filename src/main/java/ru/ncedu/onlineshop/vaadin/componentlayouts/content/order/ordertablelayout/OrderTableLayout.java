package ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.ShopUI;

import java.util.List;

/**
 * Created by ali on 05.02.15.
 */
public abstract class OrderTableLayout extends HorizontalLayout {

    protected BeanItemContainer<Order> orderContainer;
    protected Table orderTable;

    protected ServiceAPI serviceAPI;
    protected OrderService orderService;

    protected VerticalLayout tableLayout = new VerticalLayout();
    protected Button updateTableButton;

    public OrderTableLayout(ServiceAPI serviceAPI, OrderService orderService) {
        this.serviceAPI = serviceAPI;
        this.orderService = orderService;
        createOrderContainer();
        createTableLayout();
    }

    private void createOrderContainer(){
        orderContainer = new BeanItemContainer<Order>(Order.class);
        orderContainer.removeContainerProperty("orderItemList");
        orderContainer.removeContainerProperty("deliveryAddress");
        orderContainer.removeContainerProperty("deliveryMethod");
        orderContainer.removeContainerProperty("phoneNumber");
        orderContainer.removeContainerProperty("contactName");
        orderContainer.removeContainerProperty("contactEmail");
        orderContainer.removeContainerProperty("user");
        orderContainer.addNestedContainerProperty("user.id");
    }

    private void createTableLayout(){
        // создание таблицы
        orderTable = new Table("Orders");
//            @Override
//            protected String formatPropertyValue(Object rowId, Object colId,
//                                                 Property property) {
//                Object v = property.getValue();
//                if (v instanceof Calendar) {
//                    Date dateValue = ((Calendar)v).getTime();//"Formatted date: " +
//                    return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateValue);
//
//                }
//                return super.formatPropertyValue(rowId, colId, property);
//            }
//        };
        // настройка внешности и насыщение таблицы данными
        orderTable.setWidth("50%");
        orderTable.setPageLength(8);
        orderTable.setSelectable(true);
        orderTable.setContainerDataSource(orderContainer);
        // нафига логирование когда есть System.out?)))
        System.out.println(orderTable.getVisibleColumns().length);
        for (int i=0; i<orderTable.getVisibleColumns().length; i++)
            System.out.println(orderTable.getVisibleColumns()[i]);

        // переупорядычивание колонок и обработчик выделения заказа
        orderTable.setVisibleColumns(new Object[]{"id", "state", "totalPrice", "user.id"});
//        updateOrders();
        orderTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                addOrderViewLayout((Order) event.getItemId());
            }
        });

        orderTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
        orderTable.addStyleName(ValoTheme.TABLE_SMALL);
        orderTable.addStyleName(ShopUI.Styles.ANIMATION_SLIDE_RIGHT);

        tableLayout.addComponent(orderTable);

        updateTableButton = new Button("Update table", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateOrders();
            }
        });
        tableLayout.addComponent(updateTableButton);
        addComponent(tableLayout);

        updateTableButton.addStyleName(ValoTheme.BUTTON_SMALL);
        updateTableButton.addStyleName(ShopUI.Styles.ANIMATION_SLIDE_RIGHT);
        tableLayout.setImmediate(true);
        tableLayout.setMargin(true);
        tableLayout.setSpacing(true);
        tableLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        tableLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        tableLayout.setDefaultComponentAlignment(Alignment.TOP_CENTER);
    }

    protected abstract void addOrderViewLayout(Order order);

    protected abstract void removeOrderViewLayout();

//        orderTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//            @Override
//            public void itemClick(ItemClickEvent event) {
//                event.getItemId()
//            }
//        });

    public abstract void updateOrders();

    protected List<Order> orderList;

    public void updateOrderViewLayout(){
        Order currentOrder = (Order)orderTable.getValue();
        updateOrders();
        currentOrder = findOrderWithSameId(currentOrder.getId());
        if (currentOrder != null) {
            orderTable.setValue(currentOrder);
            addOrderViewLayout(currentOrder);
        }
        else
            removeOrderViewLayout();
    }

    protected Order findOrderWithSameId(long id){
        for (int i=0; i<orderList.size(); i++)
            if (orderList.get(i).getId()==id)
                return orderList.get(i);
        return null;
    }
//        orderContainer.removeAllItems();
//        orderContainer.addAll(orderService.getOrders());
//    }
}

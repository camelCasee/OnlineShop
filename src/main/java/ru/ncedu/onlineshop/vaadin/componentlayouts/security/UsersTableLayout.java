package ru.ncedu.onlineshop.vaadin.componentlayouts.security;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout.UserOrderTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.orderviewlayout.UserOrderViewLayout;

import java.util.List;

/**
 * Created by ali on 07.02.15.
 */
public class UsersTableLayout extends HorizontalLayout {

    private Table userTable;

    protected BeanItemContainer<User> userContainer;

    protected ServiceAPI serviceAPI;
    protected OrderService orderService;
    protected UserService userService;

    protected VerticalLayout tableLayout = new VerticalLayout();
    protected Button updateTableButton;
    protected Button orderOnBehalf;

    public UsersTableLayout(ServiceAPI serviceAPI, OrderService orderService, UserService userService) {
        this.serviceAPI = serviceAPI;
        this.orderService = orderService;
        this.userService = userService;
        createOrderContainer();
        createTableLayout();
    }

    private void createOrderContainer(){
        userContainer = new BeanItemContainer<User>(User.class);
        userContainer.removeContainerProperty("authorities");
        userContainer.removeContainerProperty("addresses");
        userContainer.removeContainerProperty("contactPhones");
        userContainer.removeContainerProperty("emails");
        userContainer.removeContainerProperty("auditItems");
        userContainer.removeContainerProperty("accountNonExpired");
        userContainer.removeContainerProperty("accountNonLocked");
        userContainer.removeContainerProperty("credentialsNonExpired");
        userContainer.removeContainerProperty("enabled");

    }

    private void createTableLayout(){
        // создание таблицы с обработчиком даты(не лезть, взято с офф сайта)
        userTable = new Table("Users");

        userTable.setWidth("50%");
        userTable.setPageLength(8);
        userTable.setSelectable(true);
        userTable.setContainerDataSource(userContainer);
        // нафига логирование когда есть System.out?)))
        System.out.println(userTable.getVisibleColumns().length);
        for (int i=0; i<userTable.getVisibleColumns().length; i++)
            System.out.println(userTable.getVisibleColumns()[i]);

        // переупорядычивание колонок и обработчик выделения юзера
        userTable.setVisibleColumns(new Object[]{"id", "username", "password"});
//        updateOrders();
        userTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                addUserOrdersLayout((User) event.getItemId());
            }
        });
        tableLayout.addComponent(userTable);

        updateTableButton = new Button("Update table", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateUserTable();
            }
        });

        orderOnBehalf = new Button("Order on his behalf", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                User newUser = (User)userTable.getValue();
                if (newUser != null)
                    orderService.getCurrentOrder().setUser(newUser);
            }
        });

        tableLayout.addComponent(orderOnBehalf);
        tableLayout.addComponent(updateTableButton);
        addComponent(tableLayout);
    }

    protected List<User> userList;
    protected UserOrderTableLayout userOrderTableLayout;

    protected void addUserOrdersLayout(User user){
        if (userOrderTableLayout != null)
            removeComponent(userOrderTableLayout);
        userOrderTableLayout = new UserOrderTableLayout(serviceAPI, orderService, user);
        addComponent(userOrderTableLayout);
        userOrderTableLayout.setWidth("50%");
    }

    protected void removeUserOrdersLayout(){
        if (userOrderTableLayout != null) {
            removeComponent(userOrderTableLayout);
            userOrderTableLayout = null;
        }
    }

//        orderTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
//            @Override
//            public void itemClick(ItemClickEvent event) {
//                event.getItemId()
//            }
//        });

    public void updateUserTable(){
        User selectedUser = null;
        if (userTable.getValue() != null){
            selectedUser = (User)userTable.getValue();
//            System.out.println("DEBUG OF ORDER TABLE"+orderTable.getValue().toString());
        }
        userContainer.removeAllItems();
        userList = userService.getAllUsers();
        userContainer.addAll(userList);
        if (selectedUser != null){
            User newSelectedUser = findUserWithSameId(selectedUser.getId());
            userTable.setValue(newSelectedUser);
        }
    }

    private User findUserWithSameId(long id){
        for (int i=0; i<userList.size(); i++)
            if (userList.get(i).getId()==id)
                return userList.get(i);
        return null;
    }


//    public void updateSelectedUserOrderLayout(){
//        User currentUser = (User)userTable.getValue();
//        updateUsers();
//        currentOrder = findOrderWithSameId(currentOrder.getId());
//        if (currentOrder != null) {
//            orderTable.setValue(currentOrder);
//            addOrderViewLayout(currentOrder);
//        }
//        else
//            removeOrderViewLayout();
//    }
//
//    protected Order findOrderWithSameId(long id){
//        for (int i=0; i<orderList.size(); i++)
//            if (orderList.get(i).getId()==id)
//                return orderList.get(i);
//        return null;
//    }
//        orderContainer.removeAllItems();
//        orderContainer.addAll(orderService.getOrders());
//    }

}

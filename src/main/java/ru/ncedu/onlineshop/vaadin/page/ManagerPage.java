package ru.ncedu.onlineshop.vaadin.page;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout.ManagerOrderTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.security.UsersTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart.OrderFormalizationLayout;

/**
 * Created by ali on 29.01.15.
 */
public class ManagerPage extends UserPage {
    private ServiceAPI serviceAPI;
    private OrderService orderService;
    private UserService userService;

    private User handledUser;

    public ManagerPage(Navigator navigator, ApplicationContext context) {
        super(navigator, context);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        createPageContent();
        authenticationComponent.updateState();
        if (authenticationService.isAnonymousSession() || !authenticationService.isManager()) {
            System.out.println("return to user page");
            navigator.navigateTo("");
            return;
        }
        productViewContent.updateCatalog();
    }

    @Override
    public void addOrderFormalizationLayout() {
        OrderFormalizationLayout tabContent = null;
        try {
            tabContent = new OrderFormalizationLayout(serviceAPI, orderService, userService);
            TabSheet.Tab tab = tabSheet.addTab(tabContent, "Order");
            tab.setClosable(true);
            tabSheet.setSelectedTab(tab);
        } catch (IncorrectStateException e) {
            e.printStackTrace();
            Notification.show("There is not allowed to open more than one order formalization window", Notification.Type.ERROR_MESSAGE);
        }
    }

    private ManagerOrderTableLayout managerOrderTableLayout;
    public void addOrderManagementLayout(){
        managerOrderTableLayout = new ManagerOrderTableLayout(serviceAPI, orderService);
        managerOrderTableLayout.setCaption("Order management");
        tabSheet.addTab(managerOrderTableLayout, "Order management");
    }

    private UsersTableLayout usersTableLayout;
    public void addUsersTableLayout(){
        usersTableLayout = new UsersTableLayout(serviceAPI, orderService, userService);
        usersTableLayout.setCaption("Users");
        tabSheet.addTab(usersTableLayout, "Users");
    }

    @Override
    protected void createPageContent() {
        super.createPageContent();

        if (serviceAPI == null) {
            serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
            orderService = (OrderService) context.getBean("orderService");
            userService = (UserService) context.getBean("userService");
            titleLayout.addComponent(new Label("MANAGERRRRRR"));
            addOrderManagementLayout();
            addUsersTableLayout();
            tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
                @Override
                public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                    if (tabSheet.getSelectedTab() != null &&
                            tabSheet.getSelectedTab().getCaption() != null) {//"Order management"
                        switch (tabSheet.getSelectedTab().getCaption()) {
                            case "Order management":
                                managerOrderTableLayout.updateOrders();
                                break;
                            case "Users":
                                usersTableLayout.updateUserTable();
                                break;
                        }
                    }
                }
            });
        }
    }
}

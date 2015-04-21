package ru.ncedu.onlineshop.vaadin.page;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.security.AuthenticationService;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.UserProductViewContent;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.order.ordertablelayout.UserOrderTableLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart.OrderFormalizationLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart.ShoppingCartLayout;
import static com.vaadin.ui.TabSheet.Tab;

/**
 * Created by Али on 25.09.14.
 */
public class UserPage extends BasePage {
    private ServiceAPI serviceAPI;
    private OrderService orderService;
    protected AuthenticationService authenticationService;
    protected UserService userService;

    protected VerticalLayout shoppingCartLayout = new VerticalLayout();
    protected UserProductViewContent productViewContent;

    public UserPage(Navigator navigator, ApplicationContext context) {
        super(navigator, context);
    }

    private void addShoppingCartLayout(){
        Panel shoppingCartPanel = new Panel("Shopping cart");
        ShoppingCartLayout shoppingCartPanelContent = new ShoppingCartLayout(orderService, this);
        shoppingCartPanel.setContent(shoppingCartPanelContent);
        shoppingCartPanel.setSizeFull();
        shoppingCartLayout.addComponent(shoppingCartPanel);
        shoppingCartLayout.setSizeFull();

        shoppingCartLayout.setImmediate(true);
        shoppingCartLayout.setSpacing(true);
        shoppingCartLayout.setMargin(true);
        shoppingCartLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        shoppingCartLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        shoppingCartLayout.addStyleName(ShopUI.Styles.ANIMATION_SLIDE_UP);
        shoppingCartLayout.addStyleName(ShopUI.Styles.ANIMATION_FADE_IN);

        rightLayout.addComponent(shoppingCartLayout);
        rightLayout.setExpandRatio(shoppingCartLayout, 2.0f);
    }

    public void addOrderFormalizationLayout() {
        AuthenticationService authenticationService = (AuthenticationService) context.getBean("authenticationService");
        UserService userService = (UserService) context.getBean("userService");
        OrderFormalizationLayout tabContent = null;
        try {
            orderService.getCurrentOrder().setUser(authenticationService.getCurrentUser());
            tabContent = new OrderFormalizationLayout(serviceAPI, orderService, userService);
            Tab tab = tabSheet.addTab(tabContent, "Order");
            tab.setClosable(true);
            tabSheet.setSelectedTab(tab);
        } catch (IncorrectStateException e) {
            e.printStackTrace();
            Notification.show("There is not allowed to open more than one order formalization window", Notification.Type.ERROR_MESSAGE);
        }
    }

    private UserOrderTableLayout personalOffice;
    public void addPersonalOffice(User user){
        if (personalOffice == null) {
            personalOffice = new UserOrderTableLayout(serviceAPI, orderService, user);
            personalOffice.setCaption("Personal office");
            tabSheet.addTab(personalOffice, "Personal office");
            tabSheet.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
                @Override
                public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                    if (tabSheet.getSelectedTab()!= null &&
                            tabSheet.getSelectedTab().getCaption()!= null &&
                            tabSheet.getSelectedTab().getCaption().equals("Personal office"))
                        personalOffice.updateOrders();
                }
            });
        } else {
            personalOffice.updateOrders();
        }
//        } else {
//            UserOrderTableLayout personalOffice = new UserOrderTableLayout(serviceAPI, orderService, user);
//            tabSheet.removeTab(tabSheet.getTab(personalOffice));
//            tabSheet.addTab(personalOffice);
//            this.personalOffice = personalOffice;
//        }
    }

    public void removePersonalOffice(){
        if (personalOffice != null) {
            Tab tab = tabSheet.getTab(personalOffice);
            if (tab != null)
                tabSheet.removeTab(tab);
            personalOffice = null;
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        createPageContent();
        authenticationComponent.updateState();
        if (!authenticationService.isAnonymousSession()) {
            if (authenticationService.isManager()) {
                System.out.println("return to manager page");
                navigator.navigateTo("managerPage");
                return;
            } else if (authenticationService.isAdmin()) {
                System.out.println("return to manager page");
                navigator.navigateTo("adminPage");
                return;
            }
        };
        if (authenticationService.getCurrentUser() != null){
            addPersonalOffice(authenticationService.getCurrentUser());
        }
        productViewContent.updateCatalog();
    }

    @Override
    protected void createPageContent() {
        super.createPageContent();

        if (serviceAPI == null) {
            serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
            orderService = (OrderService) context.getBean("orderService");
            authenticationService = (AuthenticationService) context.getBean("authenticationService");
            userService = (UserService) context.getBean("userService");
            productViewContent = new UserProductViewContent(serviceAPI, orderService);
            tabSheet.addTab(productViewContent, "Catalog");
            addShoppingCartLayout();

            tabSheet.setCloseHandler(new TabSheet.CloseHandler() {
                @Override
                public void onTabClose(TabSheet tabsheet,
                                       Component tabContent) {
                    Tab tab = tabsheet.getTab(tabContent);
                    // We need to close it explicitly in the handler
                    tabsheet.removeTab(tab);
                    OrderFormalizationLayout.allowFormalizationLayout();
                }
            });
        }
    }
}

package ru.ncedu.onlineshop.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.page.AdminPage;
import ru.ncedu.onlineshop.vaadin.page.ManagerPage;
import ru.ncedu.onlineshop.vaadin.page.UserPage;

import javax.servlet.ServletContext;
import java.util.List;

@Theme("valo")
@Component
@Scope(value = "session")
public class ShopUI extends UI {
    Navigator navigator;
    ApplicationContext context;
    ServiceAPI service;
    OrderService orderService;
    UserService userService;

    protected static final String MAINVIEW = "main";
    {
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        service = (ServiceAPI) context.getBean("serviceAPI");
        orderService = (OrderService) context.getBean("orderService");
        userService = (UserService) context.getBean("userService");
    }

    @Override
    protected void init(VaadinRequest request) {
        initializeDatabase();
        initializeNavigator();
        applyBasicPageStyle();
        service.printStatistics();

//        long begin = System.nanoTime();
//        for (int i=0; i<100; i++){
//            List<Product> products = service.getProductOfType(service.getProductTypes().get(0));
//        }
//        long end = System.nanoTime();
//
//        System.out.println(String.valueOf(end - begin));
//        System.out.println("begin of main");
//        Notification.show("start");
    }

    private void initializeDatabase() {
        try {
            service.initializeTestDB();
            userService.initialize();
        } catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    private UserPage userPage;
    private void initializeNavigator() {
        navigator = new Navigator(this, this);
        navigator.addView("", new UserPage(navigator, context));
        navigator.addView("managerPage", new ManagerPage(navigator, context));
        navigator.addView("adminPage", new AdminPage(navigator, context));

//        navigator.addViewChangeListener(new ViewChangeListener() {
//            @Override
//            public boolean beforeViewChange(ViewChangeEvent event) {
//                View oldView = event.getOldView();
//                if (oldView instanceof UserPage || oldView instanceof ManagerPage)
//
//            }
//
//            @Override
//            public void afterViewChange(ViewChangeEvent event) {
//
//            }
//        });
    }

    private void applyBasicPageStyle() {
        getPage().setTitle("Shop");
    }
}

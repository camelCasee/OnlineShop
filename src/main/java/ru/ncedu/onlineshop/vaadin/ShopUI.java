package ru.ncedu.onlineshop.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.page.AdminPage;
import ru.ncedu.onlineshop.vaadin.page.ManagerPage;
import ru.ncedu.onlineshop.vaadin.page.UserPage;

import javax.servlet.ServletContext;

@Theme("simpleShop")
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
        //navigator.addView("", new TestPage());
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

    public static class Styles {
        public static final String PRODUCT_LAYOUT = "product-layout";
        public static final String SMALL_MARGINS = "small-margins";
        public static final String SMALL_SPACING = "small-spacing";
        public static final String ANIMATION_FADE_IN = "animation-fade-in";
        public static final String ANIMATION_FADE_OUT = "animation-fade-out";
        public static final String ANIMATION_SLIDE_UP = "animation-slide-up";
        public static final String ANIMATION_SLIDE_DOWN = "animation-slide-down";
        public static final String ANIMATION_SLIDE_LEFT = "animation-slide-left";
        public static final String ANIMATION_SLIDE_RIGHT = "animation-slide-right";
    }

    private static final class TestPage extends VerticalLayout implements View {

        public TestPage() {
            addStyleName(Styles.PRODUCT_LAYOUT);
            addComponent(new Label("Test!"));
            setImmediate(true);
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {

        }
    }

}


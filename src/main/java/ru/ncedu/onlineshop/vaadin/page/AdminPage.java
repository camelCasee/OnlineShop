package ru.ncedu.onlineshop.vaadin.page;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.security.AuthenticationService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.AdminProductViewContent;

/**
 * Created by ali on 01.01.15.
 */
public class AdminPage extends BasePage {
    private ServiceAPI serviceAPI;
    AuthenticationService authenticationService;
    //private UserService userService;

    public AdminPage(Navigator navigator, ApplicationContext context) {
        super(navigator, context);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        createPageContent();
        authenticationComponent.updateState();
        if (authenticationService.isAnonymousSession() || !authenticationService.isAdmin()) {
            System.out.println("return to user page");
            navigator.navigateTo("");
            return;
        }
    }

    @Override
    protected void createPageContent() {
        super.createPageContent();

        if (serviceAPI == null) {
            serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
            authenticationService = (AuthenticationService) context.getBean("authenticationService");
            //userService = (UserService) context.getBean("userService");
            //userService.getUserByName()
            tabSheet.addTab(new AdminProductViewContent(serviceAPI), "Catalog");
        }
    }
}

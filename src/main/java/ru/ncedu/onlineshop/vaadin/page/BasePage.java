package ru.ncedu.onlineshop.vaadin.page;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.security.AuthenticationComponent;

import javax.naming.AuthenticationException;

/**
 * Created by ali on 01.01.15.
 */
public abstract class BasePage extends HorizontalLayout implements View {
    //protected HorizontalLayout topLayout = new HorizontalLayout();
    protected VerticalLayout leftLayout = new VerticalLayout();
    protected VerticalLayout rightLayout = new VerticalLayout();

    protected VerticalLayout authenticationLayout = new VerticalLayout();
;
    protected TabSheet tabSheet = new TabSheet();

    protected HorizontalLayout titleLayout = new HorizontalLayout();
    protected HorizontalLayout contentLayout =  new HorizontalLayout();

    protected Navigator navigator;
    protected ApplicationContext context;

    public BasePage(Navigator navigator, ApplicationContext context) {
        this.navigator = navigator;
        this.context = context;
    }

    protected AuthenticationComponent authenticationComponent;

    private void addAuthenticationLayout(){
        Panel authenticationPanel = new Panel("Authentication");
        authenticationComponent = new AuthenticationComponent(context, navigator, this);
        authenticationComponent.setSizeFull();
        authenticationPanel.setContent(authenticationComponent);
        authenticationPanel.setSizeFull();
        authenticationLayout.addComponent(authenticationPanel);
        authenticationLayout.setSizeFull();
    }

    private void addTitleLayout(){
        Label title = new Label("There will be beautiful title image");
        titleLayout.addComponent(title);
        titleLayout.setSizeFull();
        titleLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
    }

    private void configureRightLayout(){
        addAuthenticationLayout();

        rightLayout.addComponent(authenticationLayout);
        rightLayout.setExpandRatio(authenticationLayout, 3.0f);
        authenticationLayout.setHeight("30%");
    }

    private void configureLeftLayout(){
        addTitleLayout();

        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        contentLayout.addComponent(tabSheet);
        contentLayout.setSizeFull();

        leftLayout.addComponent(titleLayout);
        leftLayout.addComponent(contentLayout);
        leftLayout.setExpandRatio(titleLayout, 2.0f);
        leftLayout.setExpandRatio(contentLayout, 8.0f);
        leftLayout.setSizeFull();
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        createPageContent();
        authenticationComponent.updateState();
    }

    protected void createPageContent(){
        if (components.isEmpty()) {
            configureLeftLayout();
            configureRightLayout();

            this.addComponent(leftLayout);
            this.setExpandRatio(leftLayout, 8.0f);
            this.addComponent(rightLayout);
            this.setExpandRatio(rightLayout, 2.0f);
            this.setSizeFull();
        }
    }
}

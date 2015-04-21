package ru.ncedu.onlineshop.vaadin.page;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.security.AuthenticationComponent;

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

        authenticationLayout.setImmediate(true);
        authenticationLayout.setSpacing(true);
        authenticationLayout.setMargin(true);
        authenticationLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        authenticationLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        authenticationLayout.addStyleName(ShopUI.Styles.ANIMATION_SLIDE_UP);
        authenticationLayout.addStyleName(ShopUI.Styles.ANIMATION_FADE_IN);
    }

    private void addTitleLayout() {
        Resource resource = new ThemeResource("img/logo.png");
//        Label title = new Label("Shop");
        Image logo = new Image(null, resource);
//        title.addStyleName(ValoTheme.LABEL_HUGE);
//        title.setWidthUndefined();
        titleLayout.addComponent(logo);
        titleLayout.setWidth("30%");
        titleLayout.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
    }

    private void configureRightLayout(){
        addAuthenticationLayout();

        rightLayout.setSpacing(true);
        rightLayout.setMargin(true);
        rightLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        rightLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);

        rightLayout.addComponent(authenticationLayout);
        rightLayout.setExpandRatio(authenticationLayout, 3.0f);
        authenticationLayout.setHeight("30%");
    }

    private void configureLeftLayout(){
        addTitleLayout();

        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_FRAMED);
        tabSheet.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        tabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        contentLayout.addComponent(tabSheet);
        contentLayout.setSizeFull();

        leftLayout.addComponent(titleLayout);
        leftLayout.addComponent(contentLayout);
        leftLayout.setExpandRatio(titleLayout, 2.0f);
        leftLayout.setExpandRatio(contentLayout, 8.0f);
        leftLayout.setComponentAlignment(titleLayout, Alignment.TOP_CENTER);
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

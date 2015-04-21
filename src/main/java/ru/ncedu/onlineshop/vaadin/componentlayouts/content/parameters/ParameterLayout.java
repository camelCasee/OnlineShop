package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.service.ServiceAPI;

import javax.servlet.ServletContext;

/**
 * Created by nikita on 06.02.15.
 */
public abstract class ParameterLayout extends HorizontalLayout {
//    protected ServiceAPI serviceAPI;

    protected ParameterLayout() {
//        this.serviceAPI = serviceAPI;
        applyStyle();
//        getServiceApi();
    }

    private void applyStyle() {
        setImmediate(true);
        setStyleName("outlined");
        //setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        //setSizeFull();
        setWidth("100%");
        setHeightUndefined();
    }


}

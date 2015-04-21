package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import ru.ncedu.onlineshop.vaadin.ShopUI;

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
        //setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        setWidth("100%");
        setHeightUndefined();
    }


}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.ProductListLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;

/**
 * Created by ali on 29.01.15.
 */
public abstract class ProductViewContent extends HorizontalLayout {
    protected ProductListLayout contentLayout;
    protected ProductTypeTreeLayout menuLayout;
    protected ServiceAPI serviceAPI;

    public ProductViewContent(ProductTypeTreeLayout menuLayout, ServiceAPI serviceAPI) {
        this.menuLayout = menuLayout;
        this.serviceAPI = serviceAPI;
        addClickListenerForTreeOfProductTypes();
        setupLayout();
    }

    private void setupLayout() {
        setImmediate(true);
        //setStyleName("outlined");
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.TOP_CENTER);
        setWidth("100%");
        setHeightUndefined();
    }

    protected abstract void addClickListenerForTreeOfProductTypes();

    protected abstract void addDefaultContent();

    protected void setContentLayout(ProductListLayout newContentLayout) {
        if (contentLayout != null) {
            removeComponent(contentLayout);
        }
        contentLayout = newContentLayout;
        setupLayoutAfterSettingTheContent();
    }

    private void setupLayoutAfterSettingTheContent () {
        if (contentLayout == null) {
            return;
        }

        removeAllComponents();
        addComponents(menuLayout, contentLayout);
        setExpandRatio(menuLayout, 2.0f);
        setExpandRatio(contentLayout, 8.0f);
        menuLayout.setWidth("100%");
        menuLayout.setHeightUndefined();
        contentLayout.setWidth("100%");
        contentLayout.setHeightUndefined();
    }
}

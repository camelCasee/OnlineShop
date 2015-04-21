package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer.ManufacturerLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name.NameLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price.PriceLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter.SimpleParameterLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size.SizeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage.StorageLayout;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 28.12.14.
 */
public abstract class ProductLayout extends VerticalLayout {
    protected Product product;

    protected NameLayout nameLayout;
    protected PriceLayout priceLayout;
    protected SizeLayout sizeLayout;
    protected ManufacturerLayout manufacturerLayout;
    protected StorageLayout storageLayout;

    protected boolean parametersListIsVisible = false;
    protected List<SimpleParameterLayout> parametersLayoutsList = new ArrayList<>(10);
    protected ServiceAPI serviceAPI;

    protected ProductLayout(final Product product) {
        this.product = product;
        setupLayout();
        initializeServiceApi();
    }

    private void setupLayout() {
        addStyleName(ShopUI.Styles.PRODUCT_LAYOUT);
        setImmediate(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setWidth("84%");
        //setWidthUndefined();
        setHeightUndefined();
    }

    private void initializeServiceApi() {
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
    }

    public Product getProduct() {
        return product;
    }

    protected abstract void addNameLayout();

    protected abstract void addPriceLayout();

    protected abstract void addSizeLayout();

    protected abstract void addListOfProductParameterLayouts();

    protected abstract void addManufacturerLayout();

    protected abstract void addStorageLayout();

    protected void addNameLayoutClickListener() {
        if (nameLayout == null) return;

        nameLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                parametersListIsVisible = !parametersListIsVisible;
                if (parametersListIsVisible == true) {
                    addSizeLayout();
                    addManufacturerLayout();
                    addStorageLayout();

                    for (SimpleParameterLayout parameter: parametersLayoutsList)
                        addComponent(parameter);

                } else {
                    removeComponent(sizeLayout);
                    removeComponent(manufacturerLayout);
                    removeComponent(storageLayout);

                    for (SimpleParameterLayout parameter: parametersLayoutsList)
                        removeComponent(parameter);
                }
            }
        });
    }

    public void deleteProduct() {
        serviceAPI.removeProduct(product);
    }
}

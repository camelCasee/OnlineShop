package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts;

import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.ProductCatalogLayout;

/**
 * Created by ali on 29.01.15.
 */
public class CatalogProductListOfTypeLayout extends ProductListOfTypeLayout {
    private OrderService orderService;

    public CatalogProductListOfTypeLayout(ProductType productType, OrderService orderService, ServiceAPI service) {
        super(productType, service);
        this.orderService = orderService;
        param = 3;
        addProductLayouts();
    }

    @Override
    public void addProductLayouts() {
        if (param == 3)
            for (Product product: products)
                addComponent(new ProductCatalogLayout(orderService.getCurrentOrder(), product));
    }
}

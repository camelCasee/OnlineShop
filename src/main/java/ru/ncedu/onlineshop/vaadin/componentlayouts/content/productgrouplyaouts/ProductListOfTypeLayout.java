package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts;

import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.InformationalProductLayout;


public class ProductListOfTypeLayout extends ProductListLayout {
    protected ProductType productType;


    @SuppressWarnings("unchecked")
    public ProductListOfTypeLayout(ProductType productType, ServiceAPI service) {
        super();
        this.productType = productType;
        products = service.getProductOfType(productType);
        param = 2;
        addProductLayouts();
    }

    @Override
    public void addProductLayouts() {
        if (param == 2) {
            for (Product product : products) {
                addComponent(new InformationalProductLayout(product));
            }
        }
    }
}

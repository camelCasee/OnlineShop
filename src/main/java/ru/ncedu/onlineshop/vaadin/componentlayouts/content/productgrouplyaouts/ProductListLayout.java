package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.ProductLayout;

import java.util.List;

/**
 * Created by ali on 31.01.15.
 */
public abstract class ProductListLayout extends VerticalLayout {
    protected List<Product> products;
    protected int param;

    @SuppressWarnings("unchecked")
    public ProductListLayout() {
        setupLayout();
        param = 1;
    }

    private void setupLayout() {
        setImmediate(true);
        setStyleName("outlined");
//        setMargin(true);
//        setSpacing(true);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        //setSizeFull();
        setWidth("100%");
        setHeightUndefined();
    }

    public abstract void addProductLayouts();

    public void removeProductByLayout(ProductLayout productLayout) {
        removeComponent(productLayout);
        productLayout.deleteProduct();
    }
}

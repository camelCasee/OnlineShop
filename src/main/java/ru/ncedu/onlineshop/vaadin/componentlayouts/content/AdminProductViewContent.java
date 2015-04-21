package ru.ncedu.onlineshop.vaadin.componentlayouts.content;

import com.vaadin.event.ItemClickEvent;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.EditableProductsListOfTypeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.AdminProductTypeTreeLayout;

/**
 * Created by nikita on 05.02.15.
 */
public class AdminProductViewContent extends ProductViewContent {

    public AdminProductViewContent(ServiceAPI serviceAPI) {
        super(new AdminProductTypeTreeLayout(serviceAPI), serviceAPI);
        addDefaultContent();
    }

    @Override
    protected void addClickListenerForTreeOfProductTypes()  {
        menuLayout.getTree().addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                setContentLayout(new EditableProductsListOfTypeLayout((ProductType)event.getItemId(), serviceAPI));
            }
        });
    }

    @Override
    protected void addDefaultContent() {
        setContentLayout(new EditableProductsListOfTypeLayout(menuLayout.getDefaultProductType(), serviceAPI));
    }
}

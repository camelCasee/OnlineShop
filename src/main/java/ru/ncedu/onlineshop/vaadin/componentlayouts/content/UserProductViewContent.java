package ru.ncedu.onlineshop.vaadin.componentlayouts.content;

import com.vaadin.event.ItemClickEvent;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderState;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.observerpattern.Observable;
import ru.ncedu.onlineshop.observerpattern.Observer;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.CatalogProductListOfTypeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.UserProductTypeTreeLayout;

/**
 * Created by ali on 29.01.15.
 */
public class UserProductViewContent extends ProductViewContent implements Observer {
    private OrderService orderService;

    public UserProductViewContent(ServiceAPI serviceAPI, OrderService orderService) {
        super(new UserProductTypeTreeLayout(serviceAPI), serviceAPI);
        this.orderService = orderService;
        addDefaultContent();
        orderService.getCurrentOrder().registerObserver(this);
    }

    @Override
    protected void addClickListenerForTreeOfProductTypes() {
        menuLayout.getTree().addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                System.out.println("inside");
                System.out.println("selected product type:" + event.getItemId());
//                System.out.println(":" + event.getItemId().equals(serviceAPI.getProductTypeByName(((ProductType) event.getItemId()).getName())));
                setContentLayout(new CatalogProductListOfTypeLayout((ProductType) event.getItemId(), orderService, serviceAPI));
            }
        });
    }

    @Override
    protected void addDefaultContent() {
        setContentLayout(new CatalogProductListOfTypeLayout(menuLayout.getDefaultProductType(), orderService, serviceAPI));
    }

    @Override
    public void notifyObserver(Observable subject, Object arg) {
        if (subject instanceof Order){
            Order currentOrder= (Order)subject;
            if (currentOrder.getState() == OrderState.CONFIRMED) {
                System.out.println();
                ProductType type = (ProductType) menuLayout.getTree().getValue();
                if (type != null)
                setContentLayout(new CatalogProductListOfTypeLayout(type, orderService, serviceAPI));
            }
        }
    }

    public void updateCatalog(){
        if (menuLayout != null){
            ProductTypeTreeLayout newMenu = new UserProductTypeTreeLayout(serviceAPI);
            replaceComponent(menuLayout, newMenu);
            menuLayout = newMenu;
            addClickListenerForTreeOfProductTypes();
            menuLayout.setSizeFull();
            addDefaultContent();
        }
    }
}

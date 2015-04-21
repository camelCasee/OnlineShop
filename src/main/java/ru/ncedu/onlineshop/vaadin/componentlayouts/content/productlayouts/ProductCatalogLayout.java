package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.vaadin.ShopUI;

/**
 * Created by ali on 29.01.15.
 */
public class ProductCatalogLayout extends InformationalProductLayout {
    private Order order;
    private Button orderProductButton = new Button();
    private boolean wasOrdered = false;

    public ProductCatalogLayout(Order newOrder, final Product product) {
        super(product);
        order = newOrder;
        wasOrdered = order.findProductInOrder(product);
        addOrderProductButtonClickListener(product);
        setOrderProductButtonCaptionDependsOnOrder();

        orderProductButton.setStyleName(ValoTheme.BUTTON_TINY);
        //priceLayout.setMargin(true);
        priceLayout.setSpacing(true);
        priceLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        //priceLayout.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        priceLayout.addComponent(orderProductButton);
//        order.registerObserver(this);
    }

    private void addOrderProductButtonClickListener(final Product product) {
        orderProductButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateProductStateInCart();
            }
        });
        if (product.getProductStorage().getQuantity() == 0)
            orderProductButton.setEnabled(false);
    }

    public void updateProductStateInCart() {
        addOrRemoveProductFromCart();
        setOrderProductButtonCaptionDependsOnOrder();
    }

    private void addOrRemoveProductFromCart() {
        if (wasOrdered == true){
            order.removeProductFromOrder(product);
            wasOrdered = false;
        } else {
            if (product.getProductStorage().getQuantity() == 0)
                Notification.show("Quantity error",
                        "There are not any products of this type in the storage",
                        Notification.Type.ERROR_MESSAGE);
            else {
                order.addProductToOrder(product);
                wasOrdered = true;
            }
        }
    }

    private void setOrderProductButtonCaptionDependsOnOrder() {
        if (wasOrdered == true){
            orderProductButton.setCaption("Remove from cart");
        } else {
            orderProductButton.setCaption("Add to cart");
        }
    }
}

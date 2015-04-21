package ru.ncedu.onlineshop.spring;

import com.vaadin.navigator.Navigator;
import org.springframework.stereotype.Component;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;

/**
 * Created by ali on 06.02.15.
 */

public class ShoppingCartService {

    private Order currentOrder = new Order();

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }
}

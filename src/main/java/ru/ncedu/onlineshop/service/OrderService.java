package ru.ncedu.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.order.OrderState;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.dao.orders.OrderDAO;
import ru.ncedu.onlineshop.spring.ShoppingCartService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 31.12.14.
 */

@Component
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ServiceAPI serviceAPI;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Transactional
    public List<Order> getOrders(){
        return orderDAO.findAll();
    }

    @Transactional
    public void removeOrder(Order order){
        orderDAO.remove(order.getId());
    }

    @Transactional
    public List<Order> getOrdersOfUser(User user){
        return orderDAO.getOrdersOfUser(user);
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public Order confirmOrder(Order order) throws IncorrectStateException {
        order.confirm();
        Order res = orderDAO.save(order);
        return  res;
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public Order deliverOrder(Order order) throws IncorrectStateException {
        if (order.getState() == OrderState.CONFIRMED)
            serviceAPI.pickupProductsFromStorage(order);
        order.deliver();
        Order res = orderDAO.save(order);
        return res;
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public Order completeOrder(Order order) throws IncorrectStateException {
        order.complete();
        return orderDAO.save(order);
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public Order cancelOrder(Order order) throws IncorrectStateException {
        if (order.getState() == OrderState.DELIVERING)
            serviceAPI.returnProductsToStorage(order);
        order.cancel();
        return orderDAO.save(order);
    }

    public Order getCurrentOrder() {
        return shoppingCartService.getCurrentOrder();
    }
}

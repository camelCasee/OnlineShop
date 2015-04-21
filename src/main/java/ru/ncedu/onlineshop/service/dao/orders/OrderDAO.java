package ru.ncedu.onlineshop.service.dao.orders;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 30.12.14.
 */
@Repository("orderDAO")
public class OrderDAO extends GenericDAOImpl<Order> {
    public OrderDAO() {super();}

    public List<Order> getOrdersOfUser(User user){
        TypedQuery<Order> query = entityManager.createNamedQuery("getOrdersOfUser", entityClass);
        query.setParameter(1, user.getId());
        query.setHint("org.hibernate.cacheable", true);
        List<Order> res = query.getResultList();
//        for (int i=0; i<res.size(); i++) {
//            try {
//                System.out.println(res.get(i).getUser().getAddresses().size());
//                System.out.println(res.get(i).getUser().getEmails().size());
//                System.out.println(res.get(i).getUser().getContactPhones().size());
//            } catch (NullPointerException e){
//
//            }
//            for (int j=0; j<res.get(i).getOrderItemList().size(); j++)
//                res.get(i).getOrderItemList().get(j).getProduct().getName();      // АХАХАХАХАХАХА ДОЛОЙ ИНКАПСУЛЯЦИЮ АХАХАХАХА
//        }
        return res;
//        TypedQuery<Order> query = entityManager.createQuery("select o from Order o join fetch o.user where o.user.id = ?1", entityClass);
//        query.setParameter(1, user.getId());
//        return query.getResultList();
    }

    @Override
    public List<Order> findAll() {
        TypedQuery<Order> query = entityManager.createNamedQuery("selectAll", entityClass).setHint("org.hibernate.cacheable", true);
        List<Order> res = query.getResultList();
//        for (int i=0; i<res.size(); i++) {
//            try {
//                System.out.println(res.get(i).getUser().getAddresses().size());
//                System.out.println(res.get(i).getUser().getEmails().size());
//                System.out.println(res.get(i).getUser().getContactPhones().size());
//            } catch (NullPointerException e){
//
//            }
//            for (int j=0; j<res.get(i).getOrderItemList().size(); j++)
//                res.get(i).getOrderItemList().get(j).getProduct().getName();      // АХАХАХАХАХАХА ДОЛОЙ ИНКАПСУЛЯЦИЮ АХАХАХАХА
//        }
        return res;

//        TypedQuery<Order> query = entityManager.createQuery(
//                "select o from Order o left join fetch o.user", entityClass);
//        return query.getResultList();
    }

//    public Order getOrderWithItems(Order order){
//        TypedQuery<Order> query = entityManager.createQuery(
//                "select o from Order o left join fetch o.user where o.id= ?1", entityClass);
//        Order res = query.setParameter(1, order.getId()).getSingleResult();
//        for (int j=0; j<res.getOrderItemList().size(); j++)
//            res.getOrderItemList().get(j).getProduct().getName();
//        return res;
//    }

//    @Override
//    public Order save(Order order) {
//        Order res = super.save(order);
//        User user = res.getUser();
//        try {
//            System.out.println(user.getAddresses().size());
//            System.out.println(user.getEmails().size());
//            System.out.println(user.getContactPhones().size());
//        } catch (NullPointerException e){
//
//        }
//
//        for (int j=0; j<res.getOrderItemList().size(); j++)
//            res.getOrderItemList().get(j).getProduct().getName();
//        return res;
//    }

    //    public Order loadingOrderProducts(Order order){
//
//    }

    public Order getLastUserOrder(User user){
        TypedQuery<Order> query = entityManager.createQuery(
                "select o from Order o where o.user.id = ?1 and o.creationDate = " +
                        "(select max(or.creationDate) from Order or)", entityClass);
        query.setParameter(1, user.getId());
        return query.getSingleResult();
    }

    public Order clearOrderItems(Order order){
        order.setOrderItemList(new ArrayList<OrderItem>());
        return entityManager.merge(order);
    }

    public Order addOrderItem(Order order, OrderItem orderItem){
        order.getOrderItemList().add(orderItem);
        return entityManager.merge(order);
    }
}

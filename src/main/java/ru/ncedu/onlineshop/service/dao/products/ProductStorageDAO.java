package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductStorage;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import javax.management.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ali on 15.02.15.
 */
@Repository("productStorageDAO")
public class ProductStorageDAO extends GenericDAOImpl<ProductStorage> {
    public ProductStorageDAO(){
        super();
    }

    private List<ProductStorage> getProductStoragesOfOrder(Order order){
        // get storage list of order
        List<Long> productIds = new ArrayList<>(order.getOrderItemList().size());
        for (Iterator<OrderItem> iterator = order.getOrderItemList().iterator(); iterator.hasNext();)
            productIds.add(iterator.next().getProduct().getId());
        TypedQuery<ProductStorage> query = entityManager.createNamedQuery("getProductStoragesOfOrder", entityClass);
        query.setParameter(1, productIds);
        query.setHint("org.hibernate.cacheable", true);

        List<ProductStorage> storages =  query.getResultList();
        return storages;
    }

    public void pickupProductsFromStorage(Order order) throws IncorrectStateException {
        List<ProductStorage> storages = getProductStoragesOfOrder(order);
        // update storages
        for (int i=0; i<storages.size(); i++){
            ProductStorage storage = storages.get(i);
            storage.setQuantity(storage.getQuantity() - order.getOrderItemList().get(i).getQuantity());
            storage = update(storage);
        }
    }

    public void returnProductsToStorage(Order order) throws IncorrectStateException {
        List<ProductStorage> storages = getProductStoragesOfOrder(order);
        // update storages
        for (int i=0; i<storages.size(); i++){
            ProductStorage storage = storages.get(i);
            storage.setQuantity(storage.getQuantity() + order.getOrderItemList().get(i).getQuantity());
            storage = update(storage);
        }
    }

    public void updateProductStoragesOfOrder(Order order) {
        List<ProductStorage> storages = getProductStoragesOfOrder(order);
        for (int i=0; i<storages.size(); i++){
            ProductStorage storage = storages.get(i);
            order.getOrderItemList().get(i).getProduct().setProductStorage(storage);
        }
    }
}

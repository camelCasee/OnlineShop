package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Али on 08.09.14.
 */
@Repository("productDAO")
public class ProductDAO extends GenericDAOImpl<Product> {
    public ProductDAO(){
        super();
    }

    public List<Product> getProductListOfType(ProductType productType){
//        cache = entityManager.getEntityManagerFactory().getCache();
//        System.out.println("CACHE:" + cache.contains(ProductType.class, productType.getId()));

        TypedQuery<Product> query = entityManager.createNamedQuery("getProductListOfType", entityClass);
        query.setParameter(1, productType.getId());
        query.setHint("org.hibernate.cacheable", true);
        List<Product> res =  query.getResultList();
//        for (int i=0; i<res.size(); i++){
//            System.out.println("CACHE:" + cache.contains(entityClass, res.get(i).getId()));
//        }

        return res;
    }

//TODO Спросить у Саши на счет перфоманса запроса!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public List<Product> getOrderProductList(Order order){
        // create join contains query
        List<OrderItem> orderItems = order.getOrderItemList();
        TypedQuery<OrderItem> query = entityManager.createQuery("select o from OrderItem o left join fetch o.product where o in ?1", OrderItem.class);
        query.setParameter(1, orderItems);

        //get result of query and create return list
        List<OrderItem> resOrderItemList = query.getResultList();
        List<Product> resProductList = new ArrayList<>(orderItems.size());
        for (OrderItem orderItem: resOrderItemList){
            resProductList.add(orderItem.getProduct());
        }
        return resProductList;
    }

//    @Override
//    public Product save(Product product){
//        Product res = super.save(product);
//        entityManager.flush();
//        return res;
//    }
}
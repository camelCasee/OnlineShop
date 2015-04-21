package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Али on 13.09.14.
 */
@Repository("productTypeDAO")
public class ProductTypeDAO extends GenericDAOImpl<ProductType> {
    public ProductTypeDAO(){
        super();
    }

    public List<ProductType> getTreeOfProductType(){
        TypedQuery<ProductType> query = entityManager.createNamedQuery("getTreeOfProductType", entityClass);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    public ProductType getProductTypeByName(String name){
        return entityManager.createNamedQuery("getProductTypeByName",
                entityClass).setParameter("param", name).setHint("org.hibernate.cacheable", true).getSingleResult();
    }
}

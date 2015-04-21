package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.product.ProductField;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import java.util.List;

/**
 * Created by Али on 14.09.14.
 */
@Repository("productFieldDAO")
public class ProductFieldDAO extends GenericDAOImpl<ProductField> {
    public ProductFieldDAO(){
        super();
    }

    public List<ProductField> getAllProductFieldsOfType(ProductType productType) {
        return entityManager.createNamedQuery("getAllProductFieldsOfType", ProductField.class).setParameter(1, productType).setHint("org.hibernate.cacheable", true).getResultList();
    }
}

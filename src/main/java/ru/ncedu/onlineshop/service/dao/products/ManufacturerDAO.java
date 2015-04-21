package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

/**
 * Created by Али on 14.09.14.
 */
@Repository("manufacturerDAO")
public class ManufacturerDAO extends GenericDAOImpl<Manufacturer> {
    public ManufacturerDAO(){
        super();
    }
}

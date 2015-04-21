package ru.ncedu.onlineshop.service.dao.products;

import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

@Repository("parameterDao")
public class ParameterDAO extends GenericDAOImpl<Parameter> {
    public ParameterDAO() {
        super();
    }
}

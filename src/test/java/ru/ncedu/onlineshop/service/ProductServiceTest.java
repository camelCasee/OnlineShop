package ru.ncedu.onlineshop.service.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import ru.ncedu.onlineshop.entity.product.*;
import ru.ncedu.onlineshop.service.dao.products.ManufacturerDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductFieldDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductTypeDAO;
import ru.ncedu.onlineshop.spring.SpringBeans;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Али on 10.09.14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/test_spring.xml"})
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {SpringBeans.class})
@TransactionConfiguration(transactionManager="transactionManagerTest")
public class ProductServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ProductFieldDAO productFieldDAO;

    @Autowired
    private ProductTypeDAO productTypeDAO;

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @BeforeClass
    public static void initialize(){
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/test_spring.xml");
//        productDAO = new ProductDAO();
//        Object object = context.getBean("entityManagerFactoryTest");
//
//        LocalContainerEntityManagerFactoryBean bean = (LocalContainerEntityManagerFactoryBean)object;
//        productDAO.setManager(bean.getObject().createEntityManager());


//        ProductType type = new ProductType("Shoe");
//        ProductField shoeSize = new ProductField("Shoe size", type);
//        ProductField shoeMaterial = new ProductField("Shoe material", type);
//        ProductSize size = new ProductSize(10, 10, 40);
//        List<Parameter> shoeParameters = new ArrayList<Parameter>();
//        shoeParameters.add(new Parameter(shoeSize, "42"));
//        shoeParameters.add(new Parameter(shoeMaterial, "leather"));
//
//        Manufacturer manufacturer = new Manufacturer("RalfRinger", "Motherland");
//
//        Product product = new Product(type, manufacturer, size, shoeParameters, 10000, "Usual shoe", 2340);
//        for (Parameter parameter: shoeParameters)
//            parameter.setProduct(product);
//
//        productDAO.addOrUpdateProduct(product);
    }

    @Test
    public void testFindProduct() throws Exception {

    }

    @Test
    @Rollback
    public void testAddOrUpdateProduct() throws Exception {
        ProductType type = new ProductType("Jeans");
        type = productTypeDAO.save(type);
        ProductField jeansSize = new ProductField("Jeans size", type);
        ProductField jeansMaterial = new ProductField("Jeans material", type);
        jeansSize = productFieldDAO.save(jeansSize);
        jeansMaterial = productFieldDAO.save(jeansMaterial);
        //ProductField
        ProductSize size = new ProductSize(50, 1, 120);
        List<Parameter> jeansParameters = new ArrayList<Parameter>();
        jeansParameters.add(new Parameter(jeansSize, "42"));
        jeansParameters.add(new Parameter(jeansMaterial, "Cotton"));

        Manufacturer manufacturer = new Manufacturer("Huigun Zeipin", "China");
        manufacturer = manufacturerDAO.save(manufacturer);

        Product product = new Product(type, manufacturer, size, jeansParameters, 10000, "Usual jeans", 2340);
        for (Parameter parameter: jeansParameters)
            parameter.setProduct(product);
        product = productDAO.save(product);

        Product findedProduct = productDAO.find(product.getId());

        assertEquals("Add was failed", product, findedProduct);
    }



    @Test
    @Rollback
    public void testRemoveProduct() throws Exception {
        ProductType type = new ProductType("Jeans");
        type = productTypeDAO.save(type);
        ProductField jeansSize = new ProductField("Jeans size", type);
        ProductField jeansMaterial = new ProductField("Jeans material", type);
        jeansSize = productFieldDAO.save(jeansSize);
        jeansMaterial = productFieldDAO.save(jeansMaterial);
        Manufacturer manufacturer = new Manufacturer("Huigun Zeipin", "China");
        manufacturer = manufacturerDAO.save(manufacturer);
        //ProductField
        ProductSize size = new ProductSize(50, 1, 120);
        List<Parameter> jeansParameters = new ArrayList<Parameter>();
        jeansParameters.add(new Parameter(jeansSize, "42"));
        jeansParameters.add(new Parameter(jeansMaterial, "Cotton"));

        Product product = new Product(type, manufacturer, size, jeansParameters, 10000, "Usual jeans", 2340);
        for (Parameter parameter: jeansParameters)
            parameter.setProduct(product);
        product = productDAO.save(product);

        Product findedProduct = productDAO.find(product.getId());

        assertEquals("Add was failed", product, findedProduct);

        System.out.println(findedProduct.getId());
        productDAO.remove(product.getId());

        assertEquals("Delete was failed", null, productDAO.find(product.getId()));
    }
}

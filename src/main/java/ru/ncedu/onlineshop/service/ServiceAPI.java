package ru.ncedu.onlineshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.order.OrderItem;
import ru.ncedu.onlineshop.entity.product.*;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.service.dao.products.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Али on 09.09.14.
 */
@Component
@Transactional(readOnly = true)
public class ServiceAPI {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private ParameterDAO parameterDAO;

    @Autowired
    private ProductFieldDAO productFieldDAO;

    @Autowired
    private ProductTypeDAO productTypeDAO;

    @Autowired
    private ManufacturerDAO manufacturerDAO;

    @Autowired ProductStorageDAO productStorageDAO;

    private boolean isDatabaseInitialized = false;

    @Transactional
    public void initializeTestDB(){
        if (isDatabaseInitialized == true)
            return;
        isDatabaseInitialized = true;

        if (getProducts().size() != 0)
            return;

        try {

            Manufacturer manufacturer = new Manufacturer("RalfRinger", "Motherland");
            manufacturer = addManufacturer(manufacturer);

            ProductType type = new ProductType("Shoe");
            ProductType subType = new ProductType("Usual shoe");
            type.getChildTypes().add(subType);
            subType.setParentType(type);
            type = addProductType(type);
            subType = type.getChildTypes().iterator().next();
            ProductType jeansType = new ProductType("Jeans");
            jeansType = addProductType(jeansType);

            System.out.println(type.toString());

            List<ProductType> findType = getProductTypeTree();

            ProductField shoeSize = new ProductField("Shoe size", type);
            ProductField shoeMaterial = new ProductField("Shoe material", type);
            shoeSize =  addProductField(shoeSize);
            shoeMaterial = addProductField(shoeMaterial);

            ProductSize size = new ProductSize(10, 10, 40);
            List<Parameter> shoeParameters = new ArrayList<Parameter>();
            shoeParameters.add(new Parameter(shoeSize, "42"));
            shoeParameters.add(new Parameter(shoeMaterial, "leather"));

            Product product = new Product(subType, manufacturer, size, shoeParameters, 10000, "Usual man shoe", 2340);
            for (Parameter parameter: shoeParameters)
                parameter.setProduct(product);
            ProductStorage productStorage = new ProductStorage(product, 10);
            product.setProductStorage(productStorage);
            product = addProduct(product);
            productStorage = product.getProductStorage();
            productStorage.setProduct(product);
            productStorage = addProductStorage(productStorage);
            System.out.println(product.toString());
            System.out.println(productStorage.toString());

            size = new ProductSize(10, 10, 40);
            shoeParameters = new ArrayList<Parameter>();
            shoeParameters.add(new Parameter(shoeSize, "44"));
            shoeParameters.add(new Parameter(shoeMaterial, "natural leather"));
            Product product2 = new Product(type, manufacturer, size, shoeParameters, 10000, "Good woman shoe", 2340);
            for (Parameter parameter: shoeParameters)
                parameter.setProduct(product2);
            ProductStorage productStorage2 = new ProductStorage(product2, 10);
            product2.setProductStorage(productStorage2);
            product2 = addProduct(product2);
            productStorage2 = product2.getProductStorage();
            productStorage2.setProduct(product2);
            productStorage2 = addProductStorage(productStorage2);
            System.out.println(product2.toString());

            // jeans
            ProductField jeansSize = new ProductField("Jeans size", type);
            ProductField jeansMaterial = new ProductField("Jeans material", type);
            jeansSize = addProductField(jeansSize);
            jeansMaterial = addProductField(jeansMaterial);
            //ProductField
            ProductSize jeansProdSize = new ProductSize(50, 1, 120);
            List<Parameter> jeansParameters = new ArrayList<Parameter>();
            jeansParameters.add(new Parameter(jeansSize, "42"));
            jeansParameters.add(new Parameter(jeansMaterial, "Cotton"));
            Manufacturer jeansManufacturer = new Manufacturer("Huigun Zeipin", "China");
            jeansManufacturer = addManufacturer(jeansManufacturer);
            Product jeans = new Product(jeansType, jeansManufacturer, jeansProdSize, jeansParameters, 10000, "Usual jeans", 2340);
            for (Parameter parameter: jeansParameters)
                parameter.setProduct(jeans);

            ProductStorage productStorage3 = new ProductStorage(jeans, 10);
            jeans.setProductStorage(productStorage3);
            jeans = addProduct(jeans);
            productStorage3 = jeans.getProductStorage();
            productStorage3.setProduct(jeans);
            productStorage3 = addProductStorage(productStorage3);


            Product product1 = productDAO.find(1l);
            Product product5 = productDAO.find(2l);
            Product product7 = productDAO.find(3l);
            System.out.println(product7.toString());
//        Notification.show(product1.getName());
//        Notification.show(product5.getName());
//        Notification.show(product7.getName());

            List<Product> list = productDAO.findAll();
//        System.out.println(product1);
            //productDAO.removeProduct(product1);

            System.out.println(list.get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Product getProduct(Long id){
        return productDAO.find(id);
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(){
        return productDAO.findAll();
    }

    @Transactional
    public List<ProductField> getProductFields(){
        return productFieldDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<ProductType> getProductTypes(){
        return productTypeDAO.findAll();
    }

    @Transactional
    public ProductType getProductType(Long id){
        return productTypeDAO.find(id);
    }

    @Transactional
    public List<Manufacturer> getManufacturers(){
        return manufacturerDAO.findAll();
    }

    @Transactional
    public Product addProduct(Product product){
        return productDAO.save(product);
    }

    @Transactional
    public ProductStorage addProductStorage(ProductStorage storage) { return  productStorageDAO.save(storage); }

    @Transactional
    public ProductField addProductField(ProductField productField){
        return productFieldDAO.save(productField);
    }

    @Transactional
    public ProductType addProductType(ProductType productType){
        return productTypeDAO.save(productType);
    }

    @Transactional
    public Manufacturer addManufacturer(Manufacturer manufacturer){
        return manufacturerDAO.save(manufacturer);
    }

    @Transactional
    public void removeProduct(Product product){
        productDAO.remove(product.getId());
    }

    @Transactional
    public void removeProductField(ProductField productField){
        productFieldDAO.remove(productField.getId());
    }

    @Transactional
    public void removeProductType(ProductType productType){
        productTypeDAO.remove(productType.getId());
    }

    @Transactional
    public void removeManufacturer(Manufacturer manufacturer){
        manufacturerDAO.remove(manufacturer.getId());
    }

    @Transactional
    public Product removeParameter(Parameter parameter){
        Product product = parameter.getProduct();
        product.getParameterList().remove(parameter);
        return productDAO.update(product);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductOfType(ProductType type){
        return productDAO.getProductListOfType(type);
    }

    @Transactional(readOnly = true)
    public List<ProductType> getProductTypeTree(){
        return productTypeDAO.getTreeOfProductType();
    }

    @Transactional
    public ProductType getProductTypeByName(String name){
        return productTypeDAO.getProductTypeByName(name);
    }

    @Transactional
    public List<Product> getOrderProductList(Order order) {
        return productDAO.getOrderProductList(order);
    }

    @Transactional
    public List<ProductField> getProductFieldsOfType(ProductType productType) {
        return productFieldDAO.getAllProductFieldsOfType(productType);
    }


    @Transactional
    public Product updateProduct(Product product) {
        // JPA not delete orphan parameters automatically
        Product oldProduct = productDAO.find(product.getId());
        for (Parameter parameter: oldProduct.getParameterList()) {
            if (!product.getParameterList().contains(parameter)) {
                parameterDAO.remove(parameter.getId());
            }
        }
        return productDAO.update(product);
    }

    @Transactional
    public ProductType updateProductType(ProductType productType) {
        ProductType oldType = productTypeDAO.find(productType.getId());
        if (oldType.getParentType() == null && productType.getParentType() != null) {
            ProductType typeIterator = productType;
            while (typeIterator != null) {
                if (typeIterator.getParentType() != null && typeIterator.getParentType().equals(oldType)) {
                    break;
                }
                typeIterator = typeIterator.getParentType();
            }
            System.out.println("iterator: " + typeIterator);
            System.out.println("old: " + oldType);
            if (typeIterator != null && typeIterator.getParentType() != null && typeIterator.getParentType().equals(oldType)) {
                typeIterator.setParentType(null);
                typeIterator = productTypeDAO.update(typeIterator);
                //typeIterator = productTypeDAO.find(typeIterator.getId());
                productType.getChildTypes().remove(typeIterator);
            }
        }
        return productTypeDAO.update(productType);
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public void pickupProductsFromStorage(Order order) throws IncorrectStateException {
        productStorageDAO.pickupProductsFromStorage(order);
    }

    @Transactional(rollbackFor = IncorrectStateException.class)
    public void returnProductsToStorage(Order order) throws IncorrectStateException {
        productStorageDAO.returnProductsToStorage(order);
    }

    @Transactional
    public void updateProductStoragesOfOrder(Order order){
        productStorageDAO.updateProductStoragesOfOrder(order);
    }

    public boolean checkStoragesOfOrder(Order order){
        List<OrderItem> orderItems = order.getOrderItemList();
        for (int i=0; i< orderItems.size(); i++){
            ProductStorage storage = orderItems.get(i).getProduct().getProductStorage();
            if (storage.getQuantity() == 0)
                return false;
        }
        return true;
    }

    public void printStatistics(){

        productDAO.printStatistics();
    }

}
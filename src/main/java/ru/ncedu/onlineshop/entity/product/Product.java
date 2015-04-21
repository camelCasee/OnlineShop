package ru.ncedu.onlineshop.entity.product;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Али on 08.09.14.
 */
@Entity
@Table (name = "products",
        uniqueConstraints = { @UniqueConstraint(columnNames={"type_id", "manufacturer_id",
                "size_id", "price", "weight", "name"})},
        indexes = {@Index(columnList="type_id"), @Index(columnList="manufacturer_id"), @Index(columnList="size_id"),
                @Index(columnList="type_id"), @Index(columnList="manufacturer_id"), @Index(columnList="size_id")})
@NamedQueries({
        @NamedQuery(name="getProductListOfType",
                query="select p from Product p where p.type.id = ?1",
                hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
)
//        @NamedQuery(name="selectAll",
//                query="select p from Product p")
})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product {
    @Transient
    private static Logger logger = LoggerFactory.getLogger(Product.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "product_id")
    private Long id;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToOne (optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ProductType type;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToOne (optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToOne (optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "size_id")
    private ProductSize size;

    @Version
    private int version;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = ProductStorage.class)
    @JoinColumn(name = "product_storage_id", referencedColumnName = "product_storage_id")
    private ProductStorage productStorage;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany (mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER,  targetEntity=Parameter.class)
    private List<Parameter> parameterList;

    private double price;
    private String name;
    private double weight;

    public Product() {
    }

    public Product(ProductType type, Manufacturer manufacturer, ProductSize size, List<Parameter> parameterList, double price, String name, double weight) {
        this.type = type;
        this.manufacturer = manufacturer;
        this.size = size;
        this.parameterList = parameterList;
        this.price = price;
        this.name = name;
        this.weight = weight;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Product.logger = logger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ProductSize getSize() {
        return size;
    }

    public void setSize(ProductSize size) {
        this.size = size;
    }

    public List<Parameter> getParameterList() {
        return parameterList;
    }

    public List<ProductField> getListOfProductFields() {
        List<ProductField> productFields = new ArrayList<>(0);
        for (Parameter parameter: parameterList) {
            productFields.add(parameter.getField());
        }

        return productFields;
    }

    public void setParameterList(List<Parameter> parameterList) {
        this.parameterList = parameterList;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public ProductStorage getProductStorage() {
        return productStorage;
    }

    public void setProductStorage(ProductStorage productStorage) {
        this.productStorage = productStorage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (Double.compare(product.price, price) != 0) return false;
        if (Double.compare(product.weight, weight) != 0) return false;
        if (manufacturer != null ? !manufacturer.equals(product.manufacturer) : product.manufacturer != null)
            return false;
        if (name != null ? !name.equals(product.name) : product.name != null) return false;
        if (parameterList != null ? !parameterList.equals(product.parameterList) : product.parameterList != null)
            return false;
        if (size != null ? !size.equals(product.size) : product.size != null) return false;
        if (type != null ? !type.equals(product.type) : product.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = type != null ? type.hashCode() : 0;
        result = 31 * result + (manufacturer != null ? manufacturer.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (parameterList != null ? parameterList.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", type=" + type +
                ", manufacturer=" + manufacturer +
                ", size=" + size +
                ", version=" + version +
                ", productStorage=" + (productStorage == null ? "null" : productStorage)  +
                ", parameterList=" + parameterList +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}

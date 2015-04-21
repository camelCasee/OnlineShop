package ru.ncedu.onlineshop.entity.product;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by Али on 08.09.14.
 */
@Entity
@Table(name = "parameters",
        indexes = {@Index(columnList="field_id"), @Index(columnList="product_id") },
                uniqueConstraints = { @UniqueConstraint(columnNames={"field_id", "product_id", "value"})})
//@NamedQueries({
//        @NamedQuery(name="selectAll",
//                query="select p from Parameter p")
//})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parameter {
    @Transient
    private static Logger logger = LoggerFactory.getLogger(Parameter.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "parameter_id")
    private Long id;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "field_id")
    private ProductField field;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    private String value;

    public Parameter() {
    }

    public Parameter(ProductField field, String value) {
        this.field = field;
        this.value = value;
    }

    public Parameter(ProductField field, Product product, String value) {
        this.field = field;
        this.product = product;
        this.value = value;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        Parameter.logger = logger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductField getField() {
        return field;
    }

    public void setField(ProductField field) {
        this.field = field;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;

        Parameter parameter = (Parameter) o;

        if (field != null ? !field.getId().equals(parameter.field.getId()) : parameter.field != null) return false;
        if (id != null ? !id.equals(parameter.id) : parameter.id != null) return false;
        if (product != null ? !product.getId().equals(parameter.product.getId()) : parameter.product != null) return false;
        if (value != null ? !value.equals(parameter.value) : parameter.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (field != null ? field.getId().hashCode() : 0);
        result = 31 * result + (product != null ? product.getId().hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "field=" + field.getName() +
                ", value='" + value + '\'' +
                '}';
    }
}



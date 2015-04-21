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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Али on 08.09.14.
 */
@Entity
@Table(name = "product_types",
    uniqueConstraints={ @UniqueConstraint(columnNames={"name"})},
        indexes = {@Index(columnList = "parent_product_type_id")}
)
@NamedQueries({
        @NamedQuery(name="getProductTypeByName",
                query="select p from ProductType p where p.name = :param"),
        @NamedQuery(name="getTreeOfProductType",
                query="select p from ProductType p where p.parentType is NULL")
})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductType {
    @Transient
    private static Logger logger = LoggerFactory.getLogger(ProductType.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "product_type_id")
    private Long id;

    private String name;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToMany (mappedBy = "parentType", cascade = CascadeType.ALL, fetch = FetchType.EAGER,  targetEntity=ProductType.class)
    private Set<ProductType> childTypes =  new HashSet<ProductType>();

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_type_id", referencedColumnName = "product_type_id", nullable = true)
    private ProductType parentType = null;

    public Set<ProductType> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(Set<ProductType> childTypes) {
        this.childTypes = childTypes;
    }

    public ProductType getParentType() {
        return parentType;
    }

    public void setParentType(ProductType parentType) {
        this.parentType = parentType;
    }

    public ProductType() {

    }

    public ProductType(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        ProductType.logger = logger;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductType that = (ProductType) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String parentName = parentType == null? "null":parentType.getName();
        String res = "ProductType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentType=" + parentName +
                '}';
        for (ProductType type: childTypes)
            res += type.toString();
        return res;
    }
}

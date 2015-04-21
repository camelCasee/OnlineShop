package ru.ncedu.onlineshop.entity.product;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by Али on 08.09.14.
 */
@Entity
@Table(name = "product_fields",
       uniqueConstraints = { @UniqueConstraint(columnNames={"name", "type_id"})},
        indexes = {@Index(columnList = "type_id")}
)
@NamedQueries({
        @NamedQuery(name="getAllProductFieldsOfType",
                query="select p from ProductField p where p.type = ?1")
})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductField {
    @Transient
    private static Logger logger = LoggerFactory.getLogger(ProductField.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "product_field_id")
    private Long id;

    private String name;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private ProductType type;

    public ProductField() {
    }

    public ProductField(String name, ProductType type) {
        this.name = name;
        this.type = type;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        ProductField.logger = logger;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductField)) return false;

        ProductField that = (ProductField) o;

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

}

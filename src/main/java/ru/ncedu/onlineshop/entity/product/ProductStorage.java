package ru.ncedu.onlineshop.entity.product;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import ru.ncedu.onlineshop.exception.IncorrectStateException;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by ali on 14.02.15.
 */
@Entity
@Table(name = "product_storages",
        indexes = {@Index(columnList = "product_id")},
        uniqueConstraints = { @UniqueConstraint(columnNames={"product_id"})})
@NamedQueries({
        @NamedQuery(name="getProductStoragesOfOrder",
                query="select ps from ProductStorage ps where ps.product.id in ?1")
})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "product_storage_id")
    private Long id;

    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Version
    private int version;

    private int quantity;

    public ProductStorage(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public ProductStorage() {

    }

    @PreUpdate
    public void checkQuantity() throws IncorrectStateException {
        if (getQuantity() < 0)
            throw new IncorrectStateException("There aren't so many products in storage");
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductStorage)) return false;

        ProductStorage that = (ProductStorage) o;

        if (quantity != that.quantity) return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + quantity;
        return result;
    }

    @Override
    public String toString() {
        return "ProductStorage{" +
                "id=" + id +
//                ", product=" + product +
                ", version=" + version +
                ", quantity=" + quantity +
                '}';
    }
}

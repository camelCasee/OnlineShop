package ru.ncedu.onlineshop.entity.product;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Али on 08.09.14.
 */
@Entity
@Table(name = "product_sizes")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProductSize {
    @Transient
    private static Logger logger = LoggerFactory.getLogger(ProductSize.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "product_size_id")
    private Long id;

    private double width;
    private double height;
    private double length;

    @Override
    public String toString() {
        return "ProductSize{" +
                "width=" + width +
                ", height=" + height +
                ", length=" + length +
                '}';
    }

    public String getStringValue() {
        return "width: " + width +
                ", height: " + height +
                ", length: " + length;
    }

    public ProductSize() {
    }

    public ProductSize(double width, double height, double length) {
        this.width = width;
        this.height = height;
        this.length = length;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }


}

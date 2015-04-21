package ru.ncedu.onlineshop.service.dao;

import net.sf.ehcache.*;
import net.sf.ehcache.hibernate.EhcacheJtaTransactionManagerLookup;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.orm.jpa.EntityManagerFactoryInfo;
import ru.ncedu.onlineshop.entity.product.Product;

import javax.management.j2ee.statistics.Statistic;
import javax.persistence.*;
import javax.persistence.Cache;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Али on 13.09.14.
 */
/* TODO смысл существования этого класса мне непонятен. Всё, что тут реализовано, с тем же успехом выполняет сам EntityManager. */
public class GenericDAOImpl<T> implements GenericDAO<T> {

    protected Class<T> entityClass;

    @PersistenceContext
(properties = {
            @PersistenceProperty(name = "javax.persistence.cache.storeMode", value = "CacheStoreMode.USE") ,
            @PersistenceProperty(name = "javax.persistence.cache.retrieveMode", value = "CacheRetrieveMode.USE")})
    protected EntityManager entityManager;

    protected Cache cache;

    @SuppressWarnings("unchecked")
    public GenericDAOImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass
                .getActualTypeArguments()[0];
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "vehicleCache", key = "#id")
     public T find(Long id) {

//        entityManager.setProperty("javax.persistence.cache.storeMode", CacheStoreMode.USE);
//        entityManager.setProperty("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
        T object = entityManager.find(entityClass, id);
        Cache cache1 = entityManager.getEntityManagerFactory().getCache();
        System.out.println("Contains in cache: " +cache1.contains(entityClass, id));

        return object;
    }

    @Override
    public T save(T t) {
        return entityManager.merge(t);
    }

    @Override
    public T update(T t){
        return entityManager.merge(t);
    }

    @Override
    public void remove(Long id) {
        T managed = entityManager.find(entityClass, id);
        entityManager.remove(managed);
    }

    @Override
    public List<T> findAll() {
        TypedQuery<T> query = entityManager.createQuery("from "+ entityClass.getName(), entityClass);
        query.setHint("org.hibernate.cacheable", true);
        return query.getResultList();
    }

    @Override
    public void printStatistics(){
        Cache cache1 = entityManager.getEntityManagerFactory().getCache();
        System.out.print(cache1.contains(Product.class, 1));
        EntityManagerFactoryInfo emfi = (EntityManagerFactoryInfo)entityManager.getEntityManagerFactory();
        EntityManagerFactory emf = emfi.getNativeEntityManagerFactory();
        EntityManagerFactoryImpl empImpl = (EntityManagerFactoryImpl)emf;
        Statistics statistic = empImpl.getSessionFactory().getStatistics();
        System.out.print(statistic);
    }
}

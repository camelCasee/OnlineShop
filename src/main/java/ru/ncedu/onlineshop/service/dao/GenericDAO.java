package ru.ncedu.onlineshop.service.dao;


import java.util.List;

/**
 * Created by Али on 13.09.14.
 */
public interface GenericDAO <T > {
    T find(Long id);
    T save(T t);
    T update(T t);
    void remove(Long id);
    List<T> findAll();
    void printStatistics();
}
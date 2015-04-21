package ru.ncedu.onlineshop.observerpattern;

import java.util.Objects;

/**
 * Created by ali on 29.01.15.
 */
public interface Observer {
    void notifyObserver(Observable subject, Object arg);
}

package ru.ncedu.onlineshop.observerpattern;

/**
 * Created by ali on 29.01.15.
 */
public interface Observable {
    public void notifyObservers();
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
}

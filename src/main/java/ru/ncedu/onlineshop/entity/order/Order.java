package ru.ncedu.onlineshop.entity.order;

import com.vaadin.ui.Notification;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.observerpattern.Observable;
import ru.ncedu.onlineshop.observerpattern.Observer;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.*;

//@Component
//@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
@Entity
@Table(name = "orders", indexes = {@Index(columnList="user_id")})
//        uniqueConstraints = { @UniqueConstraint(columnNames={"state", "manufacturer_id",
//                "size_id", "price", "weight", "name"})})
@NamedQueries({
        @NamedQuery(name="getOrdersOfUser",
                query="select o from Order o " +
                        "left join fetch o.user " +
                        "left join fetch o.user.addresses " +
                        "left join fetch o.user.contactPhones " +
                        "left join fetch o.user.emails " +
//                        "join o.orderItemList oi " +
//                        "left outer join fetch oi.product " +
                        "where o.user is not null and o.user.id = ?1"),
        @NamedQuery(name="selectAll",
                query="select o from Order o " +
                        "left join fetch o.user " +
                        "left join fetch o.user.addresses " +
                        "left join fetch o.user.contactPhones " +
                        "left join fetch o.user.emails ")
})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Observable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderState state = OrderState.PROCESSING;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "order_orderitem",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_item_id", referencedColumnName = "order_item_id"),
            indexes = {@Index(columnList="order_id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<OrderItem> orderItemList = new ArrayList<OrderItem>();


    @OneToOne (optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar completionDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar confirmationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar deliveryStartDate;

    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    private String phoneNumber;

    private String contactName;

    private String contactEmail;

    @Version
    private int version;

    public Order() {
        creationDate = Calendar.getInstance();
    }

    public Order(User user){
        this.user = user;
        creationDate = Calendar.getInstance();
        orderItemList = new ArrayList<OrderItem>();
    }

    public void confirm() throws IncorrectStateException {

        if (state == OrderState.PROCESSING) {
            // preconfrim actions
            for (Iterator<OrderItem> iterator = orderItemList.iterator(); iterator.hasNext();){
                OrderItem orderItem = iterator.next();
                if (orderItem.getQuantity() == 0)
                    iterator.remove();
            }
            if (orderItemList.size() != 0) {
                state = OrderState.CONFIRMED;
                confirmationDate = Calendar.getInstance();
            } else
                throw new IncorrectStateException("Count of order items must be greater than zero");
        } else {
            throw new IncorrectStateException("Expected order state PROCESSING but found " + state.toString());
        }
    }

    // only from Delivering state to Confirmed State
    public void returnToConfirmed() throws IncorrectStateException {
        if (state == OrderState.DELIVERING) {
            state = OrderState.CONFIRMED;
            confirmationDate = Calendar.getInstance();
        } else {
            throw new IncorrectStateException("Expected order state DELIVERING but found " + state.toString());
        }
    }

    public void deliver() throws IncorrectStateException {
        if (state == OrderState.CONFIRMED){
            state = OrderState.DELIVERING;
            deliveryStartDate = Calendar.getInstance();
        } else {
            throw new IncorrectStateException("Expected order state CONFIRMED but found " + state.toString());
        }
    }

    public void complete() throws IncorrectStateException {
        if (state == OrderState.DELIVERING){
            state = OrderState.COMPLETED;
            completionDate = Calendar.getInstance();
        } else {
            throw new IncorrectStateException("Expected order state DELIVERING but found " + state.toString());
        }
    }

    public void cancel() throws IncorrectStateException {
        if (state == OrderState.DELIVERING || state == OrderState.CONFIRMED){
            state = OrderState.CANCELLED;
            completionDate = Calendar.getInstance();
        } else {
            throw new IncorrectStateException("Expected order state DELIVERING or CONFIRMED but found " + state.toString());
        }
    }

    public boolean findProductInOrder(Product product){
        for (Iterator<OrderItem> iterator = orderItemList.iterator(); iterator.hasNext();){
            if (iterator.next().getProduct().getId()==product.getId())
                return true;
        }
        return false;
    }

    public boolean removeProductFromOrder(Product product){
        for (Iterator<OrderItem> iterator = orderItemList.iterator(); iterator.hasNext();){
            OrderItem item = iterator.next();
            if (item.getProduct().getId()==product.getId()) {
                iterator.remove();
                notifyObservers();
                Notification.show(new Integer(orderItemList.size()).toString());
                return true;
            }
        }
        return false;
    }

    public boolean addProductToOrder(Product product){
        if (findProductInOrder(product))
            return false;
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(1);
        orderItemList.add(item);
        notifyObservers();
        //Notification.show(new Integer(orderItemList.size()).toString());
        return true;
    }

    public int getTotalPrice(){
        int res = 0;
        for (OrderItem item:orderItemList){
            res += item.getProduct().getPrice()*item.getQuantity();
        }
        return res;
    }

    @Transient
    private List<Observer> observers = new ArrayList<Observer>();

    @Override
    public void notifyObservers(){
        for (Observer observer: observers)
            observer.notifyObserver(this, null);
    }

    @Override
    public void registerObserver(Observer observer){
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public Calendar getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Calendar completionDate) {
        this.completionDate = completionDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public DeliveryMethod getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Calendar getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Calendar confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public Calendar getDeliveryStartDate() {
        return deliveryStartDate;
    }

    public void setDeliveryStartDate(Calendar deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        if (user == null)
            return "Order{" +
                    "orderItemList=" + convertOrderItemToString() + "\n"
                    + "null" +
                    '}';
        return "Order{" +
                "orderItemList=" + convertOrderItemToString() + "\n"
                + user.getUsername() + " : " + user.getContactPhones().size() +
                '}';
    }

    private String convertOrderItemToString(){
        StringBuffer stringBuffer = new StringBuffer();
        for (OrderItem orderItem: orderItemList){
            stringBuffer.append("\n");
            stringBuffer.append(orderItem.toString());
        }
        return stringBuffer.toString();
    }

    public void clearContent(){
        id = null;
        orderItemList = new ArrayList<>();
        user = null;
        deliveryAddress = null;
        contactEmail = null;
        deliveryMethod = null;
        contactName = null;
        phoneNumber = null;
        completionDate = null;
        creationDate = Calendar.getInstance();
        confirmationDate = null;
        deliveryStartDate = null;
        version = 0;
        notifyObservers();
        state = OrderState.PROCESSING;
    }

    public void updateFromDB(Order updatedOrder){
        id = updatedOrder.id;
        version = updatedOrder.version;
    }

    public void fillContent(Order order){
        id = null;
        orderItemList = order.getOrderItemList();
        for (int i=0; i<orderItemList.size(); i++) {
            orderItemList.get(i).setId(null);
        }
        user = order.user;
        deliveryAddress = order.deliveryAddress;
        contactEmail = order.contactEmail;
        deliveryMethod = order.deliveryMethod;
        contactName = order.contactName;
        phoneNumber = order.phoneNumber;
        completionDate = order.completionDate;
        creationDate = order.creationDate;
        confirmationDate = order.confirmationDate;
        deliveryStartDate = order.deliveryStartDate;
        version = 0;
        toString();
        notifyObservers();
        state = OrderState.PROCESSING;
    }
}


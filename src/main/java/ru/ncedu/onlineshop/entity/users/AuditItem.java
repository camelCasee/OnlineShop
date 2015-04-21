package ru.ncedu.onlineshop.entity.users;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.sql.Date;

/**
 * Created by Никита on 27.09.14.
 */
@Entity
@Table(name = "audit", indexes = {@Index(columnList="user_id_fk")})
//@NamedQueries({
//        @NamedQuery(name="selectAll",
//                query="select m from AuditItem m")
//})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class AuditItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "audit_item_id")
    private  Long id;
    private Date time;
    private String message;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id_fk",
                referencedColumnName = "user_id")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    User user;

    public AuditItem() {
    }

    public AuditItem(Date time, String message) {
        this.time = time;
        this.message = message;
    }

    public AuditItem(Date time, String message, User user) {
        this.time = time;
        this.message = message;
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

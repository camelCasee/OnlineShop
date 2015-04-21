package ru.ncedu.onlineshop.entity.users;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Никита on 27.09.14.
 */

@Entity
@Table(name = "Authorities",
        uniqueConstraints = { @UniqueConstraint(columnNames={"authority", "priority"})})
//@NamedQueries({
//        @NamedQuery(name="selectAll",
//                query="select a from Authority a")
//})
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authority_id")
    private int id;

    private String authority;
    private int priority;

//    @ManyToOne
//    private User user;

    public Authority() {}

    public Authority(String authority, int priority) {
        this.authority = authority;
        this.priority = priority;
    }

    public void setAuthority(String grantedAuthority) {
        this.authority = grantedAuthority;
    }

    /**
     * If the <code>GrantedAuthority</code> can be represented as a <code>String</code> and that
     * <code>String</code> is sufficient in precision to be relied upon for an access control decision by an {@link
     * org.springframework.security.access.AccessDecisionManager} (or delegate), this method should return such a <code>String</code>.
     * <p/>
     * If the <code>GrantedAuthority</code> cannot be expressed with sufficient precision as a <code>String</code>,
     * <code>null</code> should be returned. Returning <code>null</code> will require an
     * <code>AccessDecisionManager</code> (or delegate) to specifically support the <code>GrantedAuthority</code>
     * implementation, so returning <code>null</code> should be avoided unless actually required.
     *
     * @return a representation of the granted authority (or <code>null</code> if the granted authority cannot be
     * expressed as a <code>String</code> with sufficient precision).
     */
    @Override
    public String getAuthority() {
        return authority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    @Override
    public String toString() {
        return "Authority{" +
                //"user=" + user +
                ", priority=" + priority +
                ", authority='" + authority + '\'' +
                '}';
    }
}

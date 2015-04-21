package ru.ncedu.onlineshop.service.dao.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.dao.GenericDAOImpl;

import javax.persistence.NoResultException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Никита on 28.12.14.
 */
@Repository("userDao")
public class UserDAO extends GenericDAOImpl<User>{
    private static Logger logger = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO() {
        super();
    }

    public User getUserByName(String username) {
        logger.info("Start. Getting user by username = " + username);

//        List<User> users = entityManager.createQuery("select u from User u", User.class).getResultList();
//        logger.info("All users = " + users.toString());

        User user = null;
        try {
            user = entityManager.createQuery("select u from User u " +
                    "left join fetch u.auditItems " +
                    "left join fetch u.addresses " +
                    "left join fetch u.emails " +
                    "left join fetch u.contactPhones " +
                    "left join fetch u.authorities " +
                    "where u.username = ?1", User.class).
                    setParameter(1, username).setHint("org.hibernate.cacheable", true).getSingleResult();
//            user.getAddresses().size();
//            user.getEmails().size();
//            user.getContactPhones().size();
//            user.getAuthorities().size();
//            System.out.println(user.getAddresses().size());

        } catch (NoResultException ex) {
            throw new UsernameNotFoundException("User with such username doesn't exist.", ex);
        }

        logger.info("End of method. Return user: " + user.toString());
        return user;
    }

    @Override
    public List<User> findAll(){
        List<User> res = super.findAll();
        for (Iterator<User> iterator = res.iterator(); iterator.hasNext();){
            User user = iterator.next();
            user.getAddresses().size();
            user.getContactPhones().size();
            user.getEmails().size();
        }
        return res;
    }
}

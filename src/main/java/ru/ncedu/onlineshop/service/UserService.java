package ru.ncedu.onlineshop.service;

import com.vaadin.ui.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.users.Authority;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.dao.users.AuditItemDAO;
import ru.ncedu.onlineshop.service.dao.users.GrantedAuthorityDAO;
import ru.ncedu.onlineshop.service.dao.users.UserDAO;

import java.util.*;

/**
 * Created by Никита on 28.12.14.
 */
@Component
@Scope
public class UserService {
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GrantedAuthorityDAO grantedAuthorityDAO;

    @Autowired
    private AuditItemDAO auditItemDAO;

    private boolean isDatabaseInitialized = false;


    @Transactional
    public void initialize() {
//        Notification.show(isDatabaseInitialized+"");
        if (isDatabaseInitialized == true)
            return;
        isDatabaseInitialized = true;

        if (getUserByName("admin")!= null)
            return;
        initializeAuthorities();
        initializeUsers();
    }

    @Transactional
    private void initializeAuthorities()
    {
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new Authority("superuser", 0));
        authorities.add(new Authority("admin", 1));
        authorities.add(new Authority("manager", 5));
        authorities.add(new Authority("user", 10));
        authorities.add(new Authority("guest", 100));

        for (Authority authority: authorities) {
            grantedAuthorityDAO.save(authority);
        }
    }

    @Transactional
    private void initializeUsers()
    {
        List<User> users = new ArrayList<User>();
        Set<Authority> authorities = new LinkedHashSet<Authority>();
        authorities.add(grantedAuthorityDAO.getAdminAuthority());

        users.add(new User("admin", "admin", true, true, true, true, authorities));
        Set<Authority> managerAuthorities = new LinkedHashSet<>();
        managerAuthorities.add(grantedAuthorityDAO.getManagerAuthority());
        users.add(new User("manager", "manager", true, true, true, true, managerAuthorities));

        for (User user: users) {
            user = userDAO.save(user);
        }
    }

    @Transactional
    public User addUser(User user) {
        logger.info("Start. Adding the user = " + user.toString());

        User addedUser = userDAO.save(user);

        logger.info("End of method. Return added: " + addedUser.toString());
        return addedUser;
    }

    @Transactional
    public User addUser(String username, String password) {
        logger.info("Start. Adding the user with username = " + username);

        Set<Authority> authorities =  new LinkedHashSet<>();
        authorities.add(grantedAuthorityDAO.getUserAuthority());

        logger.info("User authorities = " + authorities.toString());

        User newUser = new User(username, password, true, true, true, true, authorities);
        User addedUser = userDAO.save(new User(username, password, true, true, true, true, authorities));

        logger.info("End of method. Return added: " + addedUser.toString());
        return addedUser;
    }


    @Transactional
    public User addAdmin(String username, String password) {
        logger.info("Start. Adding the admin with username = " + username);

        Set<Authority> authorities =  new LinkedHashSet<>();
        authorities.add(grantedAuthorityDAO.getAdminAuthority());

        logger.info("Admin authorities = " + authorities.toString());

        User addedUser = userDAO.save(new User(username, password, true, true, true, true, authorities));

        logger.info("End of method. Return added: " + addedUser.toString());
        return addedUser;
    }

    @Transactional
    public boolean isUsernameAlreadyExist(String username) {
        try {
            User user = userDAO.getUserByName(username);
        } catch (UsernameNotFoundException exception) {
            return false;
        }
        return true;
    }

    @Transactional
    public User getUserByName(String username){
        if (username == null)
            return null;
        try {
            return userDAO.getUserByName(username);
        } catch (UsernameNotFoundException exception) {
            return null;
        }
    }

    @Transactional
    public List<User> getAllUsers(){
        return userDAO.findAll();
    }

//    @Transactional
//    public boolean isUserHasAdminAuthority(User user) {
//        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();
//        for (GrantedAuthority role: roles) {
//            if (role.getAuthority().toLowerCase().equals("admin")) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    @Transactional
//    public boolean isUserHasManagerAuthority(User user) {
//        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();
//        for (GrantedAuthority role: roles) {
//            if (role.getAuthority().toLowerCase().equals("manager")) {
//                return true;
//            }
//        }
//
//        return false;
//    }
}

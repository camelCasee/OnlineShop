package ru.ncedu.onlineshop.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.dao.users.UserDAO;

/**
 * Created by Никита on 17.09.14.
 */
@Repository("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserDAO userDAO;
    /**
     * Locates the user based on the username.
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws org.springframework.security.core.userdetails.UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Start. Username = " + username);

        User user = userDAO.getUserByName(username);

        logger.info("End of method. Return user: ", user.toString());
        return user;
    }
}

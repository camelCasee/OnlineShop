package ru.ncedu.onlineshop.security;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.ncedu.onlineshop.CustomMessageSender;
import ru.ncedu.onlineshop.entity.users.AuditItem;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.UserService;

import javax.jms.JMSException;
import javax.servlet.http.Cookie;
import java.util.Collection;
import java.util.List;

/**
 * Created by ali on 30.01.15.
 */
//@Component
//@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class AuthenticationService {

//    @Autowired
//    private CustomMessageSender messageSender;

    @Autowired
    private AuthenticationManager authenticationManager;

    private User currentUser;

    @Autowired
    private UserService userService;

    public UserDetails authenticate(String login, String password) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(login, password);

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
//            messageSender.sendMessage(new AuditItem(new java.sql.Date(java.util.Calendar.getInstance().getTimeInMillis()), "User authenticated: username = " + login));
        } catch (AuthenticationException e) {
            Notification.show("Bad authentication!", Notification.Type.HUMANIZED_MESSAGE);
        }
//        catch (JMSException e) {
//            //TODO: log it
//        }

        return (authentication != null ? (UserDetails)authentication.getPrincipal() : null);
    }

    public boolean isAnonymousSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return true; // TODO: create anonymous user
        }
        List<GrantedAuthority> roles = (List<GrantedAuthority>) authentication.getAuthorities();
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().toLowerCase().equals("role_anonymous")) {
                return true;
            }
        }

        return false;
    }

    public void addNameCookie(String username) {
        Cookie nameCookie = getCookieByName("username");
        if (nameCookie == null)
            nameCookie = new Cookie("username", username);
        else
            nameCookie.setValue(username);
        nameCookie.setMaxAge(72000);
        nameCookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(nameCookie);
    }

    private void addCookie(Cookie cookie){
        cookie.setMaxAge(72000);
        cookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public void addPasswordCookie(String userpassword) {
        Cookie passwordCookie = getCookieByName("userpassword");
        if (passwordCookie == null)
            passwordCookie = new Cookie("userpassword", userpassword);
        else
            passwordCookie.setValue(userpassword);
        passwordCookie.setMaxAge(72000);
        passwordCookie.setPath("/");
        VaadinService.getCurrentResponse().addCookie(passwordCookie);
    }

    public static Cookie getCookieByName(String name) {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        System.out.println(cookies.length);
        for (int i=0; i<cookies.length; i++) {
            if (name.equals(cookies[i].getName())) {
                return cookies[i];
            }
        }
        return null;
    }

    public static void removeCookie(Cookie cookie){
        cookie.setMaxAge(0);
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    public boolean signIn(String username, String userpassword) {
        authenticate(username, userpassword);
        if (!isAnonymousSession()) {
            addNameCookie(username);
            addPasswordCookie(userpassword);
            setCurrentUser(userService.getUserByName(username));
            return true;
        }
        return false;
    }

    public void logout(){
        SecurityContextHolder.clearContext();
        setCurrentUser(null);
    }

    public boolean isManager(){
        if (getCurrentUser() == null)
            return false;

        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) getCurrentUser().getAuthorities();
        for (GrantedAuthority role: roles) {
            if (role.getAuthority().toLowerCase().equals("manager")) {
                return true;
            }
        }

        return false;
//        if (userService.getCurrentUser().getUsername().equals("manager"))
//            return true;
//        return false;
    }

    public boolean isAdmin(){
        if (getCurrentUser() == null)
            return false;

        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) getCurrentUser().getAuthorities();
        for (GrantedAuthority role: roles) {
            if (role.getAuthority().toLowerCase().equals("admin")) {
                return true;
            }
        }

        return false;
    }

    public boolean checkCookies(){
        Cookie nameCookie = getCookieByName("username");
        Cookie passwordCookie = getCookieByName("userpassword");

        if (nameCookie != null && passwordCookie != null) {
            addCookie(nameCookie);
            addCookie(passwordCookie);
            return true;
        } else {
            return false;
        }
    }

    public String getUsernameFromCookie(){
        Cookie nameCookie = getCookieByName("username");
        return nameCookie != null ? nameCookie.getValue() : null;
    }

    public String getPasswordFromCookie(){
        Cookie passwordCookie = getCookieByName("userpassword");
        return passwordCookie != null ? passwordCookie.getValue() : null;
    }

    public boolean usernameNonexistenceCheckedSuccessfully(String username) {
        if (userService.isUsernameAlreadyExist(username)) {
            Notification.show("User with such username already exist :(", Notification.Type.HUMANIZED_MESSAGE);
            return false;
        }
        return true;
    }

    public void addNewUserAndAuthenticate(String username,String password) {
        userService.addUser(username, password);
        signIn(username, password);
    }

//    public String getCurrentUsername(){
////        Cookie usernameCookie = getCookieByName("username");
////        if (usernameCookie!=null)
////        return us.getValue();
//        if (isAnonymousSession())
//            return null;
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        System.out.println(username+"|asd");
//        return username;
//    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.security;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;
import ru.ncedu.onlineshop.security.AuthenticationService;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.page.BasePage;
import ru.ncedu.onlineshop.vaadin.page.UserPage;

import javax.servlet.http.Cookie;

enum AuthenticationComponentState {NONE, LOGIN, LOGOUT, LOGINNOEMPTYFIELD}

public class AuthenticationComponent extends HorizontalLayout implements Button.ClickListener {
//    private UserService userService;
    private AuthenticationService authenticationService;
//    private CustomMessageSender messageSender;

    private Button signInButton;
    private Button signUpButton;
    private Button logoutButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField repeatPasswordField;
    private TextField firstNameField;
    private TextField secondNameField;
    private Navigator navigator;
    private BasePage parentPage;
    private static AuthenticationComponentState state = AuthenticationComponentState.NONE;
    boolean signingUp = false;

    public AuthenticationComponent(ApplicationContext context, Navigator navigator, BasePage parentPage) {
        this.navigator = navigator;
        this.parentPage = parentPage;
        initializeBeans(context);
        applyBasicLayoutStyle();
    }

    public void updateState() {
        if (state == AuthenticationComponentState.NONE){
            if (authenticationService.isAnonymousSession() && !authenticationService.checkCookies()) {
                addComponent(createSignInForm());
            } else {
                if (authenticationService.checkCookies()) {
                    if (authenticationService.signIn(authenticationService.getUsernameFromCookie(), authenticationService.getPasswordFromCookie()))
//                    System.out.println(authenticationService.getUsernameFromCookie() + ":" + authenticationService.getPasswordFromCookie());
                        addComponent(createLogoutForm());
                    else
                        addComponent(createSignInForm());
                }
            }
            setSizeFull();
        }
        else {
            switch (state) {
                case LOGIN:
                    addComponent(createSignInForm());
                    break;
                case LOGOUT:
                    addLogoutFormAfterAuthentication();
                    break;
                case LOGINNOEMPTYFIELD:
                    addSingInComponentAfterLogout();
                    break;
            }
        }
    }


    private void initializeBeans(ApplicationContext context) {
//        userService = (UserService) context.getBean("userService");
//        messageSender = (CustomMessageSender) context.getBean("customMessageSender");
        authenticationService = (AuthenticationService)context.getBean("authenticationService");
    }

    private void applyBasicLayoutStyle() {
        setSizeFull();
    }

    private VerticalLayout createSignInForm() {
        state = AuthenticationComponentState.LOGIN;

        VerticalLayout signInForm = new VerticalLayout();
        signInForm.setMargin(true);
        signInForm.setSpacing(true);
        signInForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        signInForm.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        signInForm.addStyleName(ShopUI.Styles.SMALL_SPACING);
        signInForm.setSizeFull();

        usernameField = new TextField("Username:", "");
        passwordField = new PasswordField("Password:", "");

        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.setSizeFull();
        buttonsLayout.setSpacing(true);
        buttonsLayout.addStyleName(ShopUI.Styles.SMALL_SPACING);
        signInButton = new Button("Sign In", this);
        signUpButton = new Button("Sign Up", this);
        buttonsLayout.addComponents(signInButton, signUpButton);

        signInForm.addComponents(usernameField, passwordField, buttonsLayout);

        return signInForm;
    }

    private VerticalLayout createLogoutForm() {
        state = AuthenticationComponentState.LOGOUT;

        VerticalLayout logoutForm = new VerticalLayout();
        logoutForm.setSizeFull();
        logoutForm.setMargin(true);
        logoutForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        logoutButton = new Button("Logout", this);
        logoutForm.addComponent(logoutButton);

        return logoutForm;
    }

    private VerticalLayout createSignUpForm() {
        VerticalLayout signUpForm = new VerticalLayout();
        signUpForm.setSizeFull();
        signUpForm.setMargin(true);
        signUpForm.setSpacing(true);
        signUpForm.addStyleName(ShopUI.Styles.SMALL_MARGINS);
        signUpForm.addStyleName(ShopUI.Styles.SMALL_SPACING);
        signUpForm.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        usernameField = new TextField("Username:", "");
        passwordField = new PasswordField("Password:", "");
        repeatPasswordField = new PasswordField("Repeat password:", "");
        firstNameField = new TextField("First name:");
        secondNameField = new TextField("Second name:");

        signUpForm.addComponents(
                usernameField,
                passwordField,
                repeatPasswordField,
                firstNameField,
                secondNameField,
                signUpButton
        );

        return signUpForm;
    }

    /**
     * Called when a {@link com.vaadin.ui.Button} has been clicked. A reference to the
     * button is given by {@link com.vaadin.ui.Button.ClickEvent#getButton()}.
     *
     * @param event An event containing information about the click.
     */
    @Override
    public synchronized void buttonClick(Button.ClickEvent event) {
        if (event.getButton() == logoutButton) {
            logout();
        } else if (event.getButton() == signInButton) {
            signIn();
        } else if (event.getButton() == signUpButton) {
            signUp();
        }
    }

    private void logout() {
        boolean isManager = authenticationService.isManager();
        boolean isAdmin = authenticationService.isAdmin();
        authenticationService.logout();
        addSingInComponentAfterLogout();

        if (isManager) {
            navigator.navigateTo("");
        } else if (isAdmin) {
            navigator.navigateTo("");
        } else {
            ((UserPage)parentPage).removePersonalOffice();
        }
    }

    private void addSingInComponentAfterLogout(){
        removeAllComponents();
        addComponent(createSignInForm());
        state = AuthenticationComponentState.LOGINNOEMPTYFIELD;
        Cookie nameCookie = AuthenticationService.getCookieByName("username");
        Cookie passwordCookie = AuthenticationService.getCookieByName("userpassword");
        if (nameCookie != null) usernameField.setValue(nameCookie.getValue());
        if (passwordCookie != null) passwordField.setValue(passwordCookie.getValue());
    }

    private void signIn() {
        if (authenticationService.signIn(usernameField.getValue(), passwordField.getValue())) {
            addLogoutFormAfterAuthentication();
            if (authenticationService.isManager()) {
                navigator.navigateTo("managerPage");
            } else if (authenticationService.isAdmin()) {
                navigator.navigateTo("adminPage");
            } else {
                System.out.println("OPEN PERSONAL OFFICE" + authenticationService.getCurrentUser());
                ((UserPage) parentPage).addPersonalOffice(authenticationService.getCurrentUser());
            }
        }
    }

//    public void navigateToProperView() {
//        User currentUser = userService.getUserByName(authenticationService.getCurrentUsername());
//        if (userService.isUserHasAdminAuthority(currentUser)) {
//            //Notification.show("Navigating from Authentication component!");
//            navigator.navigateTo("adminPage");
//        } else if (userService.isUserHasManagerAuthority(currentUser)) {
//            navigator.navigateTo("managerPage");
//        } else {
//            navigator.navigateTo("");
//        }
//    }

    private void addLogoutFormAfterAuthentication() {
        removeAllComponents();
        addComponent(createLogoutForm());
    }

    private void signUp() {
        if (isSigningUp()) {
            performSigningUp();
        } else {
            prepareForSigningUp();
        }
    }

    private boolean isSigningUp() {
        return signingUp;
    }

    private void performSigningUp() {
        if (inputFieldsValidatedSuccessfully() && usernameNonexistenceCheckedSuccessfully()) {
            addNewUserAndAuthenticate();
            addLogoutFormAfterAuthentication();
            signingUp = false;
        }
    }

    private boolean inputFieldsValidatedSuccessfully() {
        if (usernameField.getValue().equals("")) {
            Notification.show("You must input username");
        } else if (!passwordField.getValue().equals(repeatPasswordField.getValue())) {
            Notification.show("Passwords aren't the same");
        } else {
            return true;
        }

        return false;
    }

    private boolean usernameNonexistenceCheckedSuccessfully() {
        if (!usernameField.getValue().equals(""))
            return authenticationService.usernameNonexistenceCheckedSuccessfully(usernameField.getValue());
        return true;
    }

    private void addNewUserAndAuthenticate() {
        if (!usernameField.getValue().equals("") && !passwordField.getValue().equals(""))
            authenticationService.addNewUserAndAuthenticate(usernameField.getValue(), passwordField.getValue());
    }

    private void prepareForSigningUp() {
        removeAllComponents();
        addComponent(createSignUpForm());
        Notification.show("Input, please, registration information", Notification.Type.HUMANIZED_MESSAGE);
        signingUp = true;
    }

//    private static AuthenticationComponent instance;
//
//    public synchronized static AuthenticationComponent getInstance(ApplicationContext context, Navigator navigator) {
//        if (instance == null) {
//            instance = new AuthenticationComponent(context, navigator);
//            System.out.println("enter in singleton constructor");
//        }
//        return instance;
//    }
}
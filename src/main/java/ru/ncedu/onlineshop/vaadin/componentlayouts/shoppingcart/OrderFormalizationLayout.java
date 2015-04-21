package ru.ncedu.onlineshop.vaadin.componentlayouts.shoppingcart;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import ru.ncedu.onlineshop.entity.order.DeliveryMethod;
import ru.ncedu.onlineshop.entity.order.Order;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.exception.IncorrectStateException;
import ru.ncedu.onlineshop.security.AuthenticationService;
import ru.ncedu.onlineshop.service.OrderService;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.OrderProductListLayout;

import java.util.*;

/**
 * Created by ali on 30.01.15.
 */
public class OrderFormalizationLayout extends VerticalLayout {
    private ServiceAPI serviceAPI;
    private OrderService orderService;
    private User currentUser;
    private UserService userService;
    private Order currentOrder;

    private static boolean isOpen = false;

    public OrderFormalizationLayout(ServiceAPI serviceAPI, OrderService orderService, UserService userService) throws IncorrectStateException {
        if (isOpen == true)
            throw new IncorrectStateException("You cannot create more than one Formalization Layout!");
        else
            isOpen = true;

        this.serviceAPI = serviceAPI;
        this.orderService = orderService;
        this.orderService = orderService;
        this.userService = userService;

        currentOrder = orderService.getCurrentOrder();
        if (currentOrder.getOrderItemList().size()==0)
            throw new IncorrectStateException("You cannot formalize empty order!");
        this.currentUser = currentOrder.getUser();
        initComponents();
    }

    protected void initComponents(){
        removeAllComponents();
        serviceAPI.updateProductStoragesOfOrder(currentOrder);
        final OrderProductListLayout productOrderViewLayout = new OrderProductListLayout(currentOrder, serviceAPI);
        Button confirmOrderButton = new Button("Formalization order");
        confirmOrderButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (productOrderViewLayout.checkBeforeFormalizationOrder())
                    formalizationOrder();
                else
                    Notification.show("Order items quantity are incorrect!");
            }
        });
        addComponent(productOrderViewLayout);
        addComponent(confirmOrderButton);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        isOpen = true;
    }



    public static void allowFormalizationLayout(){
        isOpen = false;
    }

    private CheckBox pickupCheckBox = new CheckBox("Pickup", true);
    private ComboBox addressesComboBox;
    private ComboBox contactPhoneComboBox;
    private ComboBox emailComboBox;
    private TextField contactNameField;

    private void formalizationOrder() {
        removeAllComponents();
        pickupCheckBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (pickupCheckBox.getValue()) {
                    addShopAddressesComboBox();
                } else {
                    addUserAddressesComboBox(currentUser);
                }
            }
        });
        addComponent(pickupCheckBox);
        if (currentOrder.getDeliveryMethod() == null || currentOrder.getDeliveryMethod() == DeliveryMethod.DELIVERY)
            pickupCheckBox.setValue(false);
        else
            addShopAddressesComboBox();
        addContactPhoneComboBox(currentUser);
        addEmailComboBox(currentUser);
        addContactNameTextField(currentOrder.getContactName() != null ? currentOrder.getContactName() : currentUser != null ? currentUser.getUsername() : "");
        addConfirmAndBackButtons();
    }

    protected void addUserAddressesComboBox(User user) {
        ComboBox newAddressesComboBox = addComboBox(user != null? user.getAddresses(): new ArrayList<>(), "User addresses:", "Input delivery address", true, true);

        if (currentOrder.getDeliveryMethod() == DeliveryMethod.PICKUP)
            newAddressesComboBox.addItem(currentOrder.getDeliveryAddress());

        if (addressesComboBox == null){
            addressesComboBox = newAddressesComboBox;
            addComponent(addressesComboBox);
        } else {
            replaceComponent(addressesComboBox, newAddressesComboBox);
            addressesComboBox = newAddressesComboBox;
        }
    }

    protected static List<String> shopAddresses = Arrays.asList("Сокол", "Грут", "Бауманская", "Павелецкая");

    protected void addShopAddressesComboBox(){
        ComboBox newAddressesComboBox = addComboBox(shopAddresses, "Shop addresses:", "Input pickup address", false, false);

        if (addressesComboBox == null){
            addressesComboBox = newAddressesComboBox;
            addComponent(addressesComboBox);
        } else {
            replaceComponent(addressesComboBox, newAddressesComboBox);
            addressesComboBox = newAddressesComboBox;
        }

        if (currentOrder.getDeliveryMethod() == DeliveryMethod.DELIVERY )
            addressesComboBox.select(currentOrder.getDeliveryAddress());
    }

    protected ComboBox addComboBox(Collection<?> items, String caption, String inputPrompt, boolean nullSelectionAllowed, boolean allowNewItems){
        ComboBox comboBox = new ComboBox(caption);
        comboBox.setInputPrompt(inputPrompt);

        comboBox.addItems(items);
        comboBox.setTextInputAllowed(true);
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setImmediate(true);
        comboBox.setNullSelectionAllowed(nullSelectionAllowed);
        comboBox.setNewItemsAllowed(allowNewItems);
        return comboBox;
    }

    protected void addContactPhoneComboBox(User user){
        contactPhoneComboBox = addComboBox(user != null? user.getContactPhones(): new ArrayList<>(), "Contact phone:", "Input contact phone", true, true);
        if (currentOrder.getPhoneNumber() != null)
            contactPhoneComboBox.addItem(currentOrder.getPhoneNumber());
        contactPhoneComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                try {
                    Long buf = Long.valueOf((String) contactPhoneComboBox.getValue());
                    if (buf > 0)
                        contactPhoneComboBox.setComponentError(null);
                    else
                        contactPhoneComboBox.setComponentError(new UserError("Input only integer values"));
                } catch (NumberFormatException e){
                    contactPhoneComboBox.setComponentError(new UserError("Input only integer values"));
                }
            }
        });
        addComponent(contactPhoneComboBox);
    }

    protected void addEmailComboBox(User user){
        emailComboBox = addComboBox(user != null? user.getEmails(): new ArrayList<>(), "Email:", "Input email", true, true);
        if (currentOrder.getContactEmail() != null)
            emailComboBox.addItem(currentOrder.getContactEmail());
        EmailValidator emailValidator = new EmailValidator("Bad email");
        emailComboBox.addValidator(emailValidator);
        addComponent(emailComboBox);
    }

    protected void addContactNameTextField(String username){
        contactNameField = new TextField("Contact name:",username);
        contactNameField.addValidator(new StringLengthValidator("Contact name lenght must be greater than zero",
                0, 10000, false));
        addComponent(contactNameField);
    }

    protected void addConfirmAndBackButtons(){
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button confirmOrderButton = new Button("Confirm order");
        confirmOrderButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                confirmOrder();
            }
        });
        buttonsLayout.addComponent(confirmOrderButton);

        Button backButton = new Button("Go back");
        backButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                initComponents();
            }
        });
        buttonsLayout.addComponent(backButton);

        addComponent(buttonsLayout);
    }

    private void confirmOrder(){
        String deliveryAddress, contactPhone, email, contactName;

        try{
            addressesComboBox.validate();
            contactPhoneComboBox.validate();
            emailComboBox.validate();
            contactNameField.validate();
            deliveryAddress = (String)addressesComboBox.getValue();
            contactPhone = (String)contactPhoneComboBox.getValue();
            email = (String)emailComboBox.getValue();
            contactName = contactNameField.getValue();
        } catch (Validator.InvalidValueException e){
            Notification.show("Entered data are incorrect!");
            return;
        }
        DeliveryMethod deliveryMethod = pickupCheckBox.getValue() == true ? DeliveryMethod.PICKUP : DeliveryMethod.DELIVERY;
        String orderDescription = currentOrder.toString();
        String totalPrice = String.valueOf(currentOrder.getTotalPrice());

        if (currentUser != null) {
            // save input data in user object
            if (deliveryMethod == DeliveryMethod.PICKUP)
                currentUser.getAddresses().add(deliveryAddress);
            currentUser.getContactPhones().add(contactPhone);
            currentUser.getEmails().add(email);
            userService.addUser(currentUser);
        }

        // final confirmation
        Order order = null;
        try {
            currentOrder = orderService.getCurrentOrder();
            currentOrder.setDeliveryAddress(deliveryAddress);
            currentOrder.setPhoneNumber(contactPhone);
            currentOrder.setContactEmail(email);
            currentOrder.setDeliveryMethod(deliveryMethod);
            currentOrder.setContactName(contactName);

            order = orderService.confirmOrder(currentOrder);
            currentOrder.updateFromDB(order);

        } catch (IncorrectStateException e) {
            e.printStackTrace();
            Notification.show(e.getMessage());
            return;
        }
        isOpen = false;
        TextArea textArea = new TextArea("Order info", "Order №: " + order.getId() + "\n" + "Delivery method: "+ deliveryMethod.toString() +"\n"+"Delivery address: " + deliveryAddress+"\n"+"Contact phone: " + contactPhone+"\n"+"Email: " + email+"\n"+"Contact name: " + contactName+"\n"+"Order description: " + orderDescription+"\n"+"Total price: " + totalPrice);
        textArea.setRows(10);
        textArea.setWidth("60%");

        textArea.setReadOnly(true);
        removeAllComponents();
        addComponent(textArea);
        currentOrder.clearContent();
    }
}
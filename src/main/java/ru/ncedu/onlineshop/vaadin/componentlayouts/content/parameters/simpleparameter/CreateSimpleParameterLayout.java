package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductField;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

import javax.servlet.ServletContext;
import java.util.Collection;
import java.util.List;

/**
 * Created by nikita on 04.02.15.
 */
public class CreateSimpleParameterLayout extends ParameterLayout {
    protected Product product;
    protected Button addButton;
    // TODO: add ComboBox with existing parameters
    //protected TextField nameTextField;
    protected ComboBox nameComboBox;
    protected ProductField nullSelectionProductField;
    protected TextField valueTextField;
    protected ServiceAPI serviceAPI;

    public CreateSimpleParameterLayout(Product product, ServiceAPI serviceAPI) {
        this.product = product;
        nullSelectionProductField = new ProductField("", product.getType());
        setupComponents();
        addComponents(nameComboBox, valueTextField, addButton);

        //addButtonClickListener();
        this.serviceAPI = serviceAPI;
    }

    protected void setupComponents() {
        addButton = new Button("Add");
        setupComboBox();
        valueTextField = new TextField("Parameter value:");

    }

    private void setupComboBox() {
        nameComboBox = new ComboBox("Parameter:");
        nameComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        fillComboBox();
        nameComboBox.setImmediate(true);
        nameComboBox.setNewItemsAllowed(true);
        //nameComboBox.setTextInputAllowed(true);
        addValueChangeListenerForComboBox();
        setNewItemHandlerForComboBox();
        nameComboBox.select(nullSelectionProductField);
    }

    private void fillComboBox() {
        List<ProductField> availableProductFields = serviceAPI.getProductFieldsOfType(product.getType());
        List<ProductField> fieldsOfProduct = product.getListOfProductFields();
        for (ProductField productField: availableProductFields) {
            if (!fieldsOfProduct.contains(productField)) {
                nameComboBox.addItem(productField);
                nameComboBox.setItemCaption(productField, productField.getName());
            }
        }
        nameComboBox.setNullSelectionAllowed(true);
        nameComboBox.setNullSelectionItemId(nullSelectionProductField);
        nameComboBox.addItem(nullSelectionProductField);
        nameComboBox.setItemCaption(nullSelectionProductField, "Add new...");
    }

    private void addValueChangeListenerForComboBox() {
        nameComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() == null) {
                    nameComboBox.setTextInputAllowed(true);
                } else {
                    nameComboBox.setTextInputAllowed(false);
                }
            }
        });
    }

    private void setNewItemHandlerForComboBox() {
        nameComboBox.setNewItemHandler(new AbstractSelect.NewItemHandler() {
            @Override
            public void addNewItem(String newItemCaption) {
                ProductField productField = new ProductField(newItemCaption, product.getType());
                for (ProductField field : (Collection<ProductField>) nameComboBox.getContainerPropertyIds()) {
                    if (field.getName().equals(productField.getName())) {
                        Notification.show("Product field with such name already exist!");
                        return;
                    }
                }
                productField = serviceAPI.addProductField(productField);
                nameComboBox.addItem(productField);
                nameComboBox.setItemCaption(productField, newItemCaption);
            }
        });
    }

//    private void addButtonClickListener() {
//        addButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                getNewParameter();
//            }
//        });
//    }

    /**
     *
     * @return created parameter or null, if parameter with such field exist
     */
    public Parameter getNewParameter() {
        ProductField productField = (ProductField)nameComboBox.getValue();
        if (product.getListOfProductFields().contains(productField)) {
            return null;
        }

        if (productField == null) {
            return null;
        }
//
//        if (productField == nullSelectionProductField) {
//            return null;
//        }

        Parameter parameter = new Parameter(productField, product, valueTextField.getValue());
        return parameter;
    }

    public Button getAddButton() {
        return addButton;
    }

//    protected void getServiceApi() {
//        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
//        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
//        serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
//    }
}

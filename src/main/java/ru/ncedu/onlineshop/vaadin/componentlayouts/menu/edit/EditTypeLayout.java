package ru.ncedu.onlineshop.vaadin.componentlayouts.menu.edit;

import com.vaadin.data.Property;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.ShopUI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.common.DoubleButtonsDialog;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.ProductTypeTreeLayout;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by nikita on 08.02.15.
 */
public class EditTypeLayout extends VerticalLayout {
    protected ServiceAPI serviceAPI;

    protected ProductType type;
    protected ProductType nullSelectionProductType;
    protected TextField typeTextField;
    protected ComboBox parentTypeComboBox;
    protected DoubleButtonsDialog buttonsDialog;
    protected ProductTypeTreeLayout parentLayout;

    public EditTypeLayout(ProductType type, ProductTypeTreeLayout parentLayout) {
        this.type = type;
        nullSelectionProductType = new ProductType("");
        this.parentLayout = parentLayout;
        getServiceApi();
        setupComponents();
        setupLayout();
        setupButtonsDialog();
    }

    private void getServiceApi() {
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        serviceAPI = (ServiceAPI) context.getBean("serviceAPI");
    }

    private void setupComponents() {
        setupTypeTextField();
        setupParentTypeComboBox();
        setupButtonsDialog();
        addComponents(typeTextField, parentTypeComboBox, buttonsDialog);
    }

    protected void setupTypeTextField() {
        typeTextField = new TextField("Type:", type.getName());
        typeTextField.setReadOnly(false);
    }

    private void setupParentTypeComboBox() {
        parentTypeComboBox = new ComboBox("Type:");
        parentTypeComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT);
        fillComboBox();
        addValueChangedListenerForComboBox();
        parentTypeComboBox.setTextInputAllowed(false);
        parentTypeComboBox.setImmediate(true);
        parentTypeComboBox.select(type.getParentType());
    }

    private void fillComboBox() {
        List<ProductType> types = serviceAPI.getProductTypes();
        for (ProductType type: types) {
            if (!type.equals(this.type)) {
                parentTypeComboBox.addItem(type);
                parentTypeComboBox.setItemCaption(type, type.getName());
            }
        }

        parentTypeComboBox.setNullSelectionAllowed(true);
        parentTypeComboBox.setNullSelectionItemId(nullSelectionProductType);
        parentTypeComboBox.addItem(nullSelectionProductType);
        parentTypeComboBox.setItemCaption(nullSelectionProductType, "No parent");
    }

    private void addValueChangedListenerForComboBox() {
        parentTypeComboBox.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                ProductType newParentType = (ProductType) event.getProperty().getValue();
                if (newParentType != null && newParentType.equals(type)) {
                    parentTypeComboBox.select(type.getParentType());
                    Notification.show("Editing type and parent type are the same!");
                } else {
                    type.setParentType(newParentType);
                }
            }
        });
    }

    private void setupLayout() {
        setImmediate(true);
        setMargin(true);
        setSpacing(true);
        addStyleName(ShopUI.Styles.SMALL_MARGINS);
        addStyleName(ShopUI.Styles.SMALL_SPACING);
        setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        setWidth("100%");
        setHeightUndefined();
    }

    private void setupButtonsDialog() {
        buttonsDialog = new DoubleButtonsDialog("Confirm", "Cancel");
        addConfirmationListener();
        addCancelingListener();
    }

    protected void addConfirmationListener() {
        buttonsDialog.getConfirmButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (typeTextField.getValue().equals("")) {
                    Notification.show("Input, please, the name for editing type!");
                    return;
                }

                type.setName(typeTextField.getValue());

                if (type.getParentType() == nullSelectionProductType) {
                    type.setParentType(null);
                }

                type = serviceAPI.updateProductType(type);
                replaceThisLayoutWithEditLayout();
                parentLayout.updateTree();
            }
        });
    }

    protected void replaceThisLayoutWithEditLayout() {
        parentLayout.removeComponent(this);
        parentLayout.addComponentAsFirst(new EditLayout(parentLayout));
    }

    protected void addCancelingListener() {
        buttonsDialog.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                type = serviceAPI.getProductType(type.getId());
                replaceThisLayoutWithEditLayout();
            }
        });
    }

}

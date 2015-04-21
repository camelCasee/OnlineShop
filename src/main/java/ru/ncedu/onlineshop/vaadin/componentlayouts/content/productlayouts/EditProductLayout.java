package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.vaadin.componentlayouts.common.DoubleButtonsDialog;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer.EditableManufacturerLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name.EditableNameLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price.EditablePriceLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter.SimpleParameterLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter.CreateSimpleParameterLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter.EditableSimpleParameterLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size.EditableSizeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage.EditableStorageLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productgrouplyaouts.ProductListLayout;

/**
 * Created by nikita on 06.02.15.
 */
public class EditProductLayout extends ProductLayout {
    protected CreateSimpleParameterLayout createParameterLayout;
    protected DoubleButtonsDialog doubleButtonsDialog;
    private ProductListLayout parent;

    public EditProductLayout(Product product, ProductListLayout productListLayout) {
        super(product);
        parent = productListLayout;
        fillLayoutWithComponents();
    }

    private void fillLayoutWithComponents() {
        addNameLayout();
        addPriceLayout();
        addSizeLayout();
        addManufacturerLayout();
        addStorageLayout();
        addNameLayoutClickListener();
        addLayoutWithCreatingTheParameter();
        addListOfProductParameterLayouts();
        addLayoutWithDialogButtons();
    }

    @Override
    protected void addNameLayout() {
        nameLayout = new EditableNameLayout(product.getName());
        addComponent(nameLayout);
    }

    @Override
    protected void addPriceLayout() {
        priceLayout = new EditablePriceLayout(product.getPrice());
        addComponent(priceLayout);
    }

    @Override
    protected void addSizeLayout() {
        sizeLayout = new EditableSizeLayout(product.getSize().getStringValue());
        addComponent(sizeLayout);
    }

    @Override
    protected void addListOfProductParameterLayouts() {
        //parameterLayoutsList.add(new EditableParameterLayout("Manufacturer", product.getManufacturer().getStringValue()));
        for (Parameter parameter : product.getParameterList()) {
            final EditableSimpleParameterLayout parameterLayout = new EditableSimpleParameterLayout(parameter, this);
            parametersLayoutsList.add(parameterLayout);
            addComponent(parameterLayout);
            addDeleteParameterClickListener(parameterLayout);
        }
    }

    @Override
    protected void addManufacturerLayout() {
        manufacturerLayout = new EditableManufacturerLayout(product.getManufacturer());
        addComponent(manufacturerLayout);
    }

    @Override
    protected void addStorageLayout() {
        storageLayout = new EditableStorageLayout(product.getProductStorage());
        addComponent(storageLayout);
    }

    @Override
    protected void addNameLayoutClickListener() {
        // no need to add click listener
        parametersListIsVisible = true; // all parameters are always visible
    }

    private void addDeleteParameterClickListener(final EditableSimpleParameterLayout parameterLayout) {
        parameterLayout.getDeleteButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                product.getParameterList().remove(parameterLayout.getParameter());
                parametersLayoutsList.remove(parameterLayout);
                if (parametersListIsVisible) {
                    EditProductLayout.this.removeComponent(parameterLayout);
                }
            }
        });
    }

    private void addLayoutWithCreatingTheParameter() {
        createParameterLayout = new CreateSimpleParameterLayout(product, serviceAPI);
        addComponent(createParameterLayout);
        addClickListenerForAddButton();
    }

    private void addClickListenerForAddButton() {
        createParameterLayout.getAddButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                createNewParameterAndAddNewLayout();
            }
        });
    }

    private void createNewParameterAndAddNewLayout() {
        Parameter parameter = createParameterLayout.getNewParameter();
        if (parameter == null) {
            Notification.show("Can't create the parameter!");
            return;
        }
        product.getParameterList().add(parameter);

        EditableSimpleParameterLayout parameterLayout = new EditableSimpleParameterLayout(parameter, this);
        parametersLayoutsList.add(parameterLayout);

        int index = getComponentIndex(doubleButtonsDialog) - 1;
        addComponent(parameterLayout, index);
        addDeleteParameterClickListener(parameterLayout);
    }

    private void addLayoutWithDialogButtons() {
        doubleButtonsDialog = new DoubleButtonsDialog("Confirm", "Cancel");
        addComponent(doubleButtonsDialog);
        addConfirmationListener();
        addCancelingListener();
    }

    protected void addConfirmationListener() {
        doubleButtonsDialog.getConfirmButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                saveChanges();
            }
        });
    }

    protected void saveChanges() {
        updateProductFields();
        product = serviceAPI.updateProduct(product);
        replaceEditProductLayoutWithEditableProductLayout();
    }

    protected void updateProductFields() {
        product.setName(nameLayout.getName());
        // TODO: Set size
        product.setPrice(priceLayout.getPrice());
        product.getProductStorage().setQuantity(storageLayout.getProductQuantity());
        product.getManufacturer().setName(manufacturerLayout.getManufacturerName());
        product.getManufacturer().setCountry(manufacturerLayout.getManufacturerCountry());
        for (SimpleParameterLayout parameterLayout: parametersLayoutsList) {
            parameterLayout.updateParameter();
        }
        //product = serviceAPI.updateProduct(product);
    }

    protected void addCancelingListener() {
        doubleButtonsDialog.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                product = serviceAPI.getProduct(product.getId());
                replaceEditProductLayoutWithEditableProductLayout();
            }
        });
    }

    protected void replaceEditProductLayoutWithEditableProductLayout() {
        Integer index = parent.getComponentIndex(this);
        parent.addComponent(new EditableProductLayout(product, parent), index);
        parent.removeComponent(this);
    }
}

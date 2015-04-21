package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts.ProductLayout;

/**
 * Created by nikita on 03.02.15.
 */
public class EditableSimpleParameterLayout extends SimpleParameterLayout {
    protected Parameter parameter;
    protected Label nameLabel;
    protected TextField valueTextField;
    protected Button deleteButton;
    protected ProductLayout parentProductLayout;

    public EditableSimpleParameterLayout(Parameter parameter, ProductLayout parentProductLayout) {
        super(parameter);
        this.parameter = parameter;
        this.parentProductLayout = parentProductLayout;
        setupComponents();
        fillLayoutWithComponents();
    }

    private void setupComponents() {
        nameLabel = new Label(parameter.getField().getName());
        valueTextField = new TextField("", parameter.getValue());
        valueTextField.setReadOnly(false);
        deleteButton = new Button("Delete");
        //addDeleteParameterClickListener();
    }

//    private void addDeleteParameterClickListener() {
//        deleteButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                //serviceAPI.removeParameter(parameter);
//                parentProductLayout.getProduct().getParameterList().remove(parameter);
//                parentProductLayout.getParametersLayoutsList().remove(EditableSimpleParameterLayout.this);
//                if (parentProductLayout.isParametersListVisible()) {
//                    parentProductLayout.removeComponent(EditableSimpleParameterLayout.this);
//                }
//            }
//        });
//    }

    private void fillLayoutWithComponents() {
        addComponent(nameLabel);
        addComponent(valueTextField);
        addComponent(deleteButton);
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    @Override
    public String getParameterName() {
        return nameLabel.getValue();
    }

    @Override
    public void setParameterName(String parameterName) {
        nameLabel.setValue(parameterName);
    }

    @Override
    public String getParameterValue() {
        return valueTextField.getValue();
    }

    @Override
    public void setParameterValue(String parameterValue) {
        valueTextField.setValue(parameterValue);
    }

    @Override
    public void updateParameter() {
        parameter.setValue(valueTextField.getValue());
    }
}

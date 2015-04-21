package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter;

import com.vaadin.ui.Label;
import ru.ncedu.onlineshop.entity.product.Parameter;

/**
 * Created by nikita on 03.02.15.
 */
public class InformationalSimpleParameterLayout extends SimpleParameterLayout {
    protected Label nameLabel;
    protected Label valueLabel;

    public InformationalSimpleParameterLayout(Parameter parameter) {
        super(parameter);
        initializeLayout(parameter.getField().getName(), parameter.getValue());
    }

//    public InformationalSimpleParameterLayout(String parameterName, String value) {
//        super();
//        initializeLayout(parameterName, value);
//    }

    protected void initializeLayout(String parameterName, String value) {
        //setupLayout();
        setupComponents(parameterName, value);
        fillLayoutWithComponents();
    }

    private void setupComponents(String parameterName, String value) {
        nameLabel = new Label(parameterName);
        valueLabel = new Label(value);
    }

    private void fillLayoutWithComponents() {
        addComponent(nameLabel);
        addComponent(valueLabel);
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
        return valueLabel.getValue();
    }

    @Override
    public void setParameterValue(String parameterValue) {
        valueLabel.setValue(parameterValue);
    }

    @Override
    public void updateParameter() {
        //parameter.setValue(valueTextField.getValue());
    }
}

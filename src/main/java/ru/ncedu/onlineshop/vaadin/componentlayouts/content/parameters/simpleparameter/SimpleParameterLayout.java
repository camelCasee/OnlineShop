package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter;

import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class SimpleParameterLayout extends ParameterLayout {
    Parameter parameter;

    public SimpleParameterLayout(Parameter parameter) {
        super();
        this.parameter = parameter;
    }

    public abstract String getParameterName();
    public abstract void setParameterName(String parameterName);
    public abstract String getParameterValue();
    public abstract void setParameterValue(String parameterValue);
    public abstract void updateParameter();

    public Parameter getParameter() {
        return parameter;
    }
}

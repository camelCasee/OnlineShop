package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name;

import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class NameLayout extends ParameterLayout {
    protected NameLayout() {
        super();
    }

    public abstract String getName();
    public abstract void setName(String name);
}

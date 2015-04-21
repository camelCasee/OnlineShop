package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size;

import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public abstract class SizeLayout extends ParameterLayout {
    public SizeLayout() {
        super();
    }

    public abstract String getSize();
    public abstract void setSize(String size);
}

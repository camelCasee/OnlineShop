package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name;

import com.vaadin.ui.Label;

/**
 * Created by nikita on 04.02.15.
 */
public class InformationalNameLayout extends NameLayout {
    protected Label nameLabel;

    public InformationalNameLayout(String name) {
        super();
        nameLabel = new Label(name);
        addComponent(nameLabel);
    }

    @Override
    public String getName() {
        return nameLabel.getValue();
    }

    @Override
    public void setName(String name) {
        nameLabel.setValue(name);
    }
}

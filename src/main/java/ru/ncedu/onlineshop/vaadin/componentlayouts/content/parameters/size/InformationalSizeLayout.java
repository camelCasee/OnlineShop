package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size;

import com.vaadin.ui.Label;

/**
 * Created by nikita on 04.02.15.
 */
public class InformationalSizeLayout extends SizeLayout {
    protected Label sizeLabel;

    public InformationalSizeLayout(String size) {
        super();
        sizeLabel = new Label("Size:" + size);
        addComponent(sizeLabel);
    }

    @Override
    public String getSize() {
        return sizeLabel.getValue();
    }

    @Override
    public void setSize(String size) {
        sizeLabel.setValue(size);
    }
}

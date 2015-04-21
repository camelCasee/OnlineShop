package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size;

import com.vaadin.ui.TextField;

/**
 * Created by nikita on 04.02.15.
 */
public class EditableSizeLayout extends SizeLayout {
    protected TextField sizeTextfield;

    public EditableSizeLayout(String size) {
        super();
        sizeTextfield = new TextField("Size:", size);
        sizeTextfield.setReadOnly(false);
        addComponent(sizeTextfield);
    }

    @Override
    public String getSize() {
        return sizeTextfield.getValue();
    }

    @Override
    public void setSize(String size) {
        sizeTextfield.setValue(size);
    }
}

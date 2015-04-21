package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name;

import com.vaadin.ui.TextField;

/**
 * Created by nikita on 04.02.15.
 */
public class EditableNameLayout extends NameLayout {
    protected TextField nameTextField;

    public EditableNameLayout(String name) {
        super();
        nameTextField = new TextField("Name:", name);
        nameTextField.setReadOnly(false);
        addComponent(nameTextField);
    }

    @Override
    public String getName() {
        return nameTextField.getValue();
    }

    @Override
    public void setName(String name) {
        nameTextField.setValue(name);
    }
}

package ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.quantity;

import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.ui.TextField;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.ParameterLayout;

/**
 * Created by nikita on 04.02.15.
 */
public class QuantityLayout extends ParameterLayout {
    private TextField quantityTextEdit;
    private IntegerRangeValidator validator = new IntegerRangeValidator("Input integer value!", 0, 10000);

    public QuantityLayout(Integer productQuantity) {
        super();
        setupComponents();
        setupLayout();
        addComponents(quantityTextEdit);
        quantityTextEdit.setValue(productQuantity.toString());
    }

    private void setupComponents() {
        quantityTextEdit = new TextField("Quantity:", "");
        quantityTextEdit.setConverter(new StringToIntegerConverter());
        quantityTextEdit.addValidator(validator);
        quantityTextEdit.setReadOnly(false);
    }

    private void setupLayout() {

    }

    public TextField getQuantityTextEdit() {
        return quantityTextEdit;
    }

    public IntegerRangeValidator getValidator() {
        return validator;
    }
}

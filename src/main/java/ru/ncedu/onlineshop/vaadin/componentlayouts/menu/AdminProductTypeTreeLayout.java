package ru.ncedu.onlineshop.vaadin.componentlayouts.menu;

import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.vaadin.componentlayouts.menu.edit.EditLayout;

/**
 * Created by nikita on 04.02.15.
 */
public class AdminProductTypeTreeLayout extends  ProductTypeTreeLayout {
    public AdminProductTypeTreeLayout(ServiceAPI serviceAPI) {
        super(serviceAPI);
        addComponentAsFirst(new EditLayout(this));
    }
}

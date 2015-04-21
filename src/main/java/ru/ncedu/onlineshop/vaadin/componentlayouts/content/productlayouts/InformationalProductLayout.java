package ru.ncedu.onlineshop.vaadin.componentlayouts.content.productlayouts;

import ru.ncedu.onlineshop.entity.product.Parameter;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.product.ProductStorage;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.manufacturer.InformationManufacturerLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.name.InformationalNameLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.price.InformationalPriceLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.simpleparameter.InformationalSimpleParameterLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.size.InformationalSizeLayout;
import ru.ncedu.onlineshop.vaadin.componentlayouts.content.parameters.storage.InformationStorageLayout;

import java.awt.*;

/**
 * Created by nikita on 04.02.15.
 */
public class InformationalProductLayout extends ProductLayout {

    public InformationalProductLayout(Product product) {
        super(product);
        fillLayoutWithComponents();
    }

    protected void fillLayoutWithComponents() {
        setStyleName("outlined");
        addNameLayout();
        addPriceLayout();
//        addSizeLayout();
//        addStorageLayout();
        addNameLayoutClickListener();
        addListOfProductParameterLayouts();
    }

    @Override
    protected void addNameLayout() {
        ProductStorage productStorage = product.getProductStorage();
        nameLayout = new InformationalNameLayout(product.getName() + (productStorage.getQuantity() != 0 ? "" : " (out of stock)"));
        addComponent(nameLayout);
    }

    @Override
    protected void addPriceLayout() {
        priceLayout = new InformationalPriceLayout(product.getPrice());
        addComponent(priceLayout);
    }

    @Override
    protected void addSizeLayout() {
        sizeLayout = new InformationalSizeLayout(product.getSize().getStringValue());
        addComponent(sizeLayout);
    }

//    @Override
//    protected void addNameLayoutClickListener() {
//        nameLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                parametersListIsVisible = !parametersListIsVisible;
//                if (parametersListIsVisible == true) {
//                    for (AbstractSimpleParameterLayout parameter: parametersLayoutsList)
//                        addComponent(parameter);
//                } else {
//                    for (AbstractSimpleParameterLayout parameter: parametersLayoutsList)
//                        removeComponent(parameter);
//                }
//            }
//        });
//    }

    @Override
    protected void addListOfProductParameterLayouts() {
        //parametersLayoutsList.add(new InformationalSimpleParameterLayout("Manufacturer", product.getManufacturer().getStringValue()));
        for (Parameter parameter : product.getParameterList())
            parametersLayoutsList.add(new InformationalSimpleParameterLayout(parameter));
    }

    @Override
    protected void addManufacturerLayout() {
        manufacturerLayout = new InformationManufacturerLayout(product.getManufacturer());
        addComponent(manufacturerLayout);
    }

    @Override
    protected void addStorageLayout() {
        storageLayout = new InformationStorageLayout(product.getProductStorage());
        addComponent(storageLayout);
    }


}

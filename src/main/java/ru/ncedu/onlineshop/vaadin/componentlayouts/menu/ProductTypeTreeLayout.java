package ru.ncedu.onlineshop.vaadin.componentlayouts.menu;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import ru.ncedu.onlineshop.entity.product.ProductType;
import ru.ncedu.onlineshop.service.ServiceAPI;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ali on 28.12.14.
 */
public abstract class ProductTypeTreeLayout extends VerticalLayout{
    protected Tree tree = new Tree("Categories:");
    protected ServiceAPI serviceAPI;
    protected List<ProductType> productTypeList;

    public ProductTypeTreeLayout(final ServiceAPI serviceAPI) {
        this.serviceAPI = serviceAPI;
        productTypeList =  serviceAPI.getProductTypeTree();
        setupTree();
        setupLayout();
        addComponent(tree);
    }

    protected void setupTree() {
        tree.setImmediate(true);
        tree.setNewItemsAllowed(false);
        fillTreeWithTypes();
    }

    protected void fillTreeWithTypes() {
        for (ProductType iterator: productTypeList) {
            fillTree(iterator);
        }
    }

    private void fillTree(ProductType root){
        tree.addItem(root);
        tree.setItemCaption(root, root.getName());
        if (root.getChildTypes().size() == 0){
            tree.setChildrenAllowed(root, false);
        } else {
            Iterator<ProductType> iterator = root.getChildTypes().iterator();
            while(iterator.hasNext()) {
                ProductType type = iterator.next();
                fillTree(type);
                tree.setParent(type, root);
            }
        }
    }

    private void setupLayout() {
        setImmediate(true);
        setStyleName("outlined");
        //setMargin(true);
        setSpacing(true);
        setDefaultComponentAlignment(Alignment.TOP_LEFT);
        //setSizeFull();
        setWidth("100%");
        setHeightUndefined();
    }

    public ProductType getDefaultProductType() {
        return productTypeList.get(0);
    }

    public void updateTree() {
        tree.removeAllItems();
        productTypeList = serviceAPI.getProductTypeTree();
        System.out.println("Roots list:" + productTypeList);
        System.out.println("all:" + serviceAPI.getProductTypes());
        fillTreeWithTypes();
    }

    public Tree getTree() {
        return tree;
    }
}

package ru.ncedu.onlineshop;

//<<<<<<< Updated upstream
//import org.eclipse.jetty.server.Server;
//=======
////import org.eclipse.jetty.server.Server;
////import org.eclipse.jetty.servlet.ServletContextHandler;
////import org.eclipse.jetty.servlet.ServletHolder;
////import org.eclipse.jetty.webapp.WebAppContext;
//>>>>>>> Stashed changes
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

//import org.eclipse.jetty.server.Server;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import ru.ncedu.onlineshop.entity.product.Manufacturer;
import ru.ncedu.onlineshop.entity.product.Product;
import ru.ncedu.onlineshop.entity.users.User;
import ru.ncedu.onlineshop.service.ServiceAPI;
import ru.ncedu.onlineshop.service.UserService;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext("src/main/webapp/WEB-INF/config/spring-config.xml");
        ServiceAPI api = (ServiceAPI) context.getBean("serviceAPI");
        UserService userService = (UserService) context.getBean("userService");

        System.out.println("Hello!");
        api.initializeTestDB();

        List<Product> products = api.getProducts();
        Product product = null;
        for (int j=0; j<products.size(); j++)
            product = api.getProduct(products.get(j).getId());

        int iter = 10;
        long begin = System.nanoTime();
        for (int i=0; i<iter; i++){
//            List<ProductType> productTypeTree = api.getProductTypeTree();
//            List<Product> products1 = api.getProductOfType(productTypeTree.get(0));
            products = api.getProducts();
            List<Manufacturer> manufacturers = api.getManufacturers();
            for (int j=0; j<products.size(); j++) {
                product = api.getProduct(products.get(j).getId());
            }

            System.out.println(i);
        }

        product.setName(product.getName()+"_Ext");
        api.addProduct(product);

        api.getProduct(1l);

        for (int i=0; i<iter; i++){
            products = api.getProducts();
            for (int j=0; j<products.size(); j++) {
                product = api.getProduct(products.get(j).getId());
            }
            System.out.println(product.getName());
            System.out.println(i);
        }
        long end = System.nanoTime();

        api.printStatistics();
        System.out.println(String.valueOf(end - begin));

        userService.initialize();
        User user = userService.getUserByName("admin");
        System.out.println(user.toString());

        System.out.println("begin of main");
        //113.154.573.919
        //122.616.405.712
        //12.121.772.307
        //10.740034602
        //17.892119636
        //8.849.280.298




        //115114380705 cache

        //100905900044 nocache
        //99569429066
        //129027891111

        //318775556213
        //348824868275
        //20210980460




//            ApplicationContext context = new ClassPathXmlApplicationContext("server.xml");
//            Server server = context.getBean("server", Server.class);
//            server.start();
//            server.join();

//        Server server = new Server(8080);
//
//        WebAppContext context = new WebAppContext();
//        context.setDescriptor("/WEB-INF/web.xml");
//        context.setResourceBase("/src/main/resources");
//        context.setContextPath("/");
//        context.setParentLoaderPriority(true);
//
//        server.setHandler(context);
//
//        server.start();
//        server.join();
    }
}

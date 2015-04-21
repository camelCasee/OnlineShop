package ru.ncedu.onlineshop.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import ru.ncedu.onlineshop.service.dao.orders.OrderDAO;
import ru.ncedu.onlineshop.service.dao.products.ManufacturerDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductFieldDAO;
import ru.ncedu.onlineshop.service.dao.products.ProductTypeDAO;

import java.util.Properties;

/**
 * Created by Али on 13.09.14.
 */
@Configuration
public class SpringBeans {

    @Bean
    public DriverManagerDataSource dataSource(){
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:db_test");
        dataSource.setUsername("admin");
        dataSource.setPassword("admin");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryTest(){
        final LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
        entityManagerFactoryBean.setPersistenceUnitName("onlineshopPersistenceTest");
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapterTest());
        entityManagerFactoryBean.setJpaDialect(jpaDialectTest());
        Properties props = new Properties();
        props.put("hibernate.hbm2ddl.auto", "create");
        entityManagerFactoryBean.setJpaProperties(props);
        return entityManagerFactoryBean;
    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapterTest (){
        final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabase(Database.H2);
        adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
        return adapter;
    }

    @Bean
    public JpaTransactionManager transactionManagerTest (){
        final JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryTest().getObject());
        manager.setDataSource(dataSource());
        manager.setJpaDialect(jpaDialectTest());
        return manager;
    }

    @Bean
    public HibernateJpaDialect jpaDialectTest (){
        return new HibernateJpaDialect();
    }

    @Bean
    public ProductDAO productDAO(){
        // TODO Отсюда я убрал сеттер - аннотации Autowired и PersistenceContext не требуют ручного присваивания поля
        return new ProductDAO();
    }

    @Bean
    public ProductFieldDAO productFieldDAO(){
        // TODO Отсюда я убрал сеттер - аннотации Autowired и PersistenceContext не требуют ручного присваивания поля
        return new ProductFieldDAO();
    }
    @Bean
    public ProductTypeDAO productTypeDAO(){
        // TODO Отсюда я убрал сеттер - аннотации Autowired и PersistenceContext не требуют ручного присваивания поля
        return new ProductTypeDAO();
    }

    @Bean
    public ManufacturerDAO manufacturerDAO(){
        // TODO Отсюда я убрал сеттер - аннотации Autowired и PersistenceContext не требуют ручного присваивания поля
        return new ManufacturerDAO();
    }

    @Bean
    public OrderDAO orderDAO(){
        return new OrderDAO();
    }
}

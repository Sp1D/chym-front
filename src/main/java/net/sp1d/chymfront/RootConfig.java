/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront;

import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;



/**
 *
 * @author sp1d
 */
@Configuration
@ComponentScan(basePackages = {"net.sp1d.chymfront","net.sp1d.chymfront.controllers","net.sp1d.chym.abstractclasses","net.sp1d.chym.trackers"})
@EnableJpaRepositories("net.sp1d.chym.repos")
@EnableTransactionManagement
public class RootConfig {
    @Bean
    DataSource dataSource() {
        DataSource ds = null;
        try {            
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("jdbc/MySQLDataSource");            
            
        } catch (NamingException namingException) {
            System.out.println("can't init datasource (configure me to use logging)");            
        }
        return ds;
    }
    
    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        
        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        va.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");                    
        
        emf.setJpaVendorAdapter(va);
        emf.setDataSource(dataSource());        
        emf.setPackagesToScan("net.sp1d.chym.entities","net.sp1d.chym.trackers","net.sp1d.chym.abstractclasses","net.sp1d.chymfront.repos");
        emf.setPersistenceUnitName("net.sp1d.chymfront_PU");
        
        Properties properties = new Properties();
        properties.setProperty("hibernate.event.merge.entity_copy_observer", "allow");
        
        emf.setJpaProperties(properties);
        
        
        
        return emf;
    }
    
    @Bean
    JpaTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setPersistenceUnitName("net.sp1d.chymfront_PU");
        return tm;
    }
}

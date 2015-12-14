/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chymfront;

import java.util.Date;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sp1d.chym.FSaver;
import net.sp1d.chym.FetchNSave;
import net.sp1d.chym.MovieFetcherOMDB;
import net.sp1d.chym.Service;
import net.sp1d.chym.abstractclasses.AbstractFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;



/**
 *
 * @author sp1d
 */
@Configuration
@ComponentScan(basePackages = {"net.sp1d.chym.abstractclasses","net.sp1d.chym.trackers"})
@EnableJpaRepositories("net.sp1d.chym.repos")
@EnableTransactionManagement
public class RootConfig {
    
    Logger log = LoggerFactory.getLogger(RootConfig.class);
    
    @Bean
    DataSource dataSource() {
        DataSource ds = null;
        try {            
            InitialContext ctx = new InitialContext();
            Context envCtx = (Context) ctx.lookup("java:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/MySQLDataSource");            
            
        } catch (NamingException namingException) {
            log.error("can't init datasource", namingException);                     
        }
        return ds;
    }
    
    @Bean            
    LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        
        HibernateJpaVendorAdapter va = new HibernateJpaVendorAdapter();
        va.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");    
        va.setGenerateDdl(true);        
        
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
    
    @Bean
    AbstractFetcher fetcher() {
        AbstractFetcher fetcher = new MovieFetcherOMDB();
        return fetcher;
    }
    
    @Bean
    FetchNSave fetchSaver() {
        return new FSaver();
    }
    
    @Bean
    Service service() {
        return new Service();
    }
    
    @Bean
    TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        
        
        ts.setDaemon(true);
        ts.initialize();
        
//        Check new titles at all trackers
        
        ts.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                service().checkNewTitlesAllTrackers();
//                System.out.println("TESTING OUTPUT");
            }
        }, new Date(System.currentTimeMillis()+10000), 7200);
        
        return ts;
    }
}

package com.odeyalo.analog.auth.integration.configs;

import com.odeyalo.analog.auth.repository.UserRepository;
import com.odeyalo.analog.auth.service.support.BcryptEncoderPasswordRecoverySaverSupport;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

@EnableJpaRepositories(basePackages = {"com.odeyalo.analog.auth.repository"})
@EnableTransactionManagement
@EntityScan(basePackages = "com.odeyalo.analog.auth.entity")
@TestPropertySource(locations = "classpath:application-test.properties")
@Configuration
public class BcryptEncoderPasswordRecoverySaverSupportTestConfiguration {

    @Autowired
    private DataSource dataSource;
    @Bean
    public BcryptEncoderPasswordRecoverySaverSupport bcryptEncoderPasswordRecoverySaverSupport(UserRepository userRepository, PasswordEncoder encoder) {
        return new BcryptEncoderPasswordRecoverySaverSupport(encoder, userRepository);
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws SQLException {
        //JpaVendorAdapteradapter can be autowired as well if it's configured in application properties.
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);

        //Add package to scan for entities.
        factory.setPackagesToScan("com.odeyalo.analog.auth.entity");
        factory.setDataSource(dataSource);
        factory.setJpaProperties(additionalProperties());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.ejb.naming_strategy","org.hibernate.cfg.ImplicitNamingStrategy");
        jpaProperties.put("hibernate.ejb.physical_naming_strategy","org.hibernate.cfg.PhysicalNamingStrategyImpl");
        jpaProperties.put("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
        return jpaProperties;
    }
}

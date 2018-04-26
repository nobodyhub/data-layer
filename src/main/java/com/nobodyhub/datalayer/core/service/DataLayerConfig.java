package com.nobodyhub.datalayer.core.service;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.nobodyhub.datalayer.core.service.common.AvroSchemaLoaderConfiguration;
import org.hibernate.SessionFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

/**
 * @author Ryan
 */
@AvroSchemaLoaderConfiguration(basePackages = "com.nobodyhub.datalayer.core")
public class DataLayerConfig {

    private String driverClassName;
    private String url;
    private String username;
    private String password;

    private String hibernateDialect;
    private String hibernateShowSql;
    private String hibernateHbm2ddlAuto;

    private String[] packageToScan;

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.put("hibernate.dialect", hibernateDialect);
        props.put("hibernate.show_sql", hibernateShowSql);
        props.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        return props;
    }

    private DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverClassName);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}

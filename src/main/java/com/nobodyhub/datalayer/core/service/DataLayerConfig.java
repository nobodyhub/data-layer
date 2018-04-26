package com.nobodyhub.datalayer.core.service;

import com.google.common.collect.Sets;
import com.nobodyhub.datalayer.core.avro.AvroData;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.common.AvroSchemaLoaderConfiguration;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.Entity;
import java.util.Properties;
import java.util.Set;

/**
 * @author Ryan
 */
public class DataLayerConfig {

    static {
        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        Set<Class<?>> avroClass = Sets.newHashSet((configurationReflection.getSubTypesOf(AvroEntity.class)));
        Set<Class<?>> entityClass = configurationReflection.getTypesAnnotatedWith(Entity.class);

        Configuration hibernateConfig = new Configuration();
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.show_sql", true);
        props.put("hibernate.hbm2ddl.auto", true);
        props.put("hibernate.connection.provider_class", C3P0ConnectionProvider.class);
        props.put("hibernate.connection.driver_class", "org.h2.Driver");
        props.put("hibernate.connection.url", "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:init.sql';DB_CLOSE_ON_EXIT=FALSE");
        props.put("hibernate.connection.username", "sa");
        props.put("hibernate.connection.password", "");
        hibernateConfig.setProperties(props);
        //TODO: add C3p0 settings
        //add entity class
        for (Class cls : entityClass) {
            hibernateConfig.addClass(cls);
        }
        avroClass.addAll(entityClass);
        for (Class cls : avroClass) {
            AvroData.get().getSchema(cls);
        }
    }
}

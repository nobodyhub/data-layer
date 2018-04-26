package com.nobodyhub.datalayer.core.service;

import com.google.common.collect.Sets;
import com.nobodyhub.datalayer.core.avro.AvroData;
import com.nobodyhub.datalayer.core.proto.DataLayerService;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.Getter;
import org.hibernate.c3p0.internal.C3P0ConnectionProvider;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author yan_h
 * @since 2018-04-25.
 */
public class DataLayerServer {

    @Getter
    private final int port;
    private final Server server;

    @Getter
    private final DataLayerService dataLayerService;

    public DataLayerServer(int port) {
        this(port, null);
    }

    public DataLayerServer(int port, Properties hibernateProps) {
        this.port = port;
        Configuration configuration = configuration(hibernateProps);
        preload(configuration);
        this.dataLayerService = new DataLayerService(configuration.buildSessionFactory());
        this.server = ServerBuilder.forPort(port).addService(dataLayerService).build();
    }

    protected Properties getHibernateProps(Properties userDefined) {
        //default settings
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.show_sql", true);
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.connection.provider_class", C3P0ConnectionProvider.class);
        props.put("hibernate.connection.driver_class", "org.h2.Driver");
        props.put("hibernate.connection.url", "jdbc:h2:mem:test;INIT=RUNSCRIPT FROM 'classpath:init.sql';DB_CLOSE_ON_EXIT=FALSE");
        props.put("hibernate.connection.username", "sa");
        props.put("hibernate.connection.password", "");
        //override with user-defined
        if (userDefined != null) {
            props.putAll(userDefined);
        }
        return props;
    }

    public Configuration configuration(Properties hibernateProps) {
        Properties props = getHibernateProps(hibernateProps);
        return new Configuration().setProperties(props);
    }

    protected void preload(Configuration configuration) {
        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        Set<Class<?>> schemaCls = Sets.newHashSet((configurationReflection.getSubTypesOf(AvroEntity.class)));
        Set<Class<?>> entityCls = configurationReflection.getTypesAnnotatedWith(Entity.class);
        //add @Entity class to Hibernate
        for (Class<?> cls : entityCls) {
            configuration.addAnnotatedClass(cls);
        }
        //load avor schema for AvroEntity
        schemaCls.addAll(entityCls);
        for (Class cls : schemaCls) {
            AvroData.get().getSchema(cls);
        }
    }

    public void start() throws IOException {
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                DataLayerServer.this.stop();
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
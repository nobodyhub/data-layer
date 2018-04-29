package com.nobodyhub.datalayer.core.proto;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nobodyhub.datalayer.core.avro.AvroData;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.repository.DataService;
import io.grpc.stub.StreamObserver;
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
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Ryan
 */
public class DataLayerServerService extends DataLayerServiceGrpc.DataLayerServiceImplBase {

    @Getter
    private final DataService service;

    public DataLayerServerService() {
        this(null);
    }

    public DataLayerServerService(Properties hibernateProps) {
        Configuration configuration = configuration(hibernateProps);
        preload(configuration);
        service = new DataService(configuration.buildSessionFactory());
    }

    @Override
    public StreamObserver<DataLayerProtocol.Request> execute(StreamObserver<DataLayerProtocol.Response> responseObserver) {
        return new StreamObserver<DataLayerProtocol.Request>() {
            List<DataLayerProtocol.Request> operations = Lists.newArrayList();

            @Override
            public void onNext(DataLayerProtocol.Request value) {
                operations.add(value);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                        .setStatusCode(DataLayerProtocol.StatusCode.ERROR)
                        .setMessage(t.getMessage())
                        .build());
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                try {
                    responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                            .setStatusCode(DataLayerProtocol.StatusCode.OK)
                            .setEntity(service.execute(operations))
                            .build());
                    responseObserver.onCompleted();
                } catch (IOException | ClassNotFoundException e) {
                    onError(e);
                }
            }
        };
    }

    @Override
    public void query(DataLayerProtocol.Request request, StreamObserver<DataLayerProtocol.Response> responseObserver) {
        try {
            List<DataLayerProtocol.Entity> entities = service.query(Class.forName(request.getEntityClass()), request.getCriteria());
            for (DataLayerProtocol.Entity entity : entities) {
                responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                        .setStatusCode(DataLayerProtocol.StatusCode.OK)
                        .setEntity(entity)
                        .build());
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            responseObserver.onNext(DataLayerProtocol.Response.newBuilder()
                    .setStatusCode(DataLayerProtocol.StatusCode.ERROR)
                    .setMessage(e.getMessage())
                    .build());
        } finally {
            responseObserver.onCompleted();
        }
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
}
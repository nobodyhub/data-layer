package com.nobodyhub.datalayer.core.entity;

import avro.shaded.com.google.common.base.Joiner;
import com.nobodyhub.datalayer.core.entity.common.AvroSchemaLoaderConfiguration;
import com.nobodyhub.datalayer.core.entity.data.AvroData;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Load {@link javax.persistence.Entity} class and create Avro Schema
 *
 * @author Ryan
 */
@Component
public final class AvroSchemaLoader {
    private Logger logger = Logger.getLogger(AvroSchemaLoader.class.getSimpleName());

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void setup() throws ClassNotFoundException {
        preload();
    }

    /**
     * Scan the classloader to load classes for Avro Schemas
     *
     * @throws ClassNotFoundException
     */
    public void preload() throws ClassNotFoundException {
        logger.info("Start to preload classes for AvroSchema");
        //get annotation settings
//        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
//                .addUrls(ClasspathHelper.forClassLoader())
//                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
//        List<Class<?>> configurations = Lists.newArrayList(configurationReflection.getTypesAnnotatedWith(AvroSchemaLoaderConfiguration.class));

        Map<String, Object> configurations = applicationContext.getBeansWithAnnotation(AvroSchemaLoaderConfiguration.class);
        if (configurations.isEmpty()) {
            throw new RuntimeException(String.format("Can not find configuration %s within classloaders %s!",
                    AvroSchemaLoaderConfiguration.class.getName(),
                    Joiner.on(", ").join(ClasspathHelper.classLoaders())));
        }
        for (String clsName : configurations.keySet()) {
            Class cls = Class.forName(clsName);
            logger.info(AvroSchemaLoaderConfiguration.class.getSimpleName() + " found on Class: " + cls.getSimpleName());
            AvroSchemaLoaderConfiguration configuration = (AvroSchemaLoaderConfiguration) cls.getAnnotation(AvroSchemaLoaderConfiguration.class);

            for (String path : configuration.basePackages()) {
                //get the target classes in base package
                Reflections targetClassReflection = new Reflections(new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(path))
                        .setScanners(new SubTypesScanner(false)));
                Set<Class<?>> targetCls = targetClassReflection.getSubTypesOf(Object.class);
                //filter by annotation
//            Set<Class<?>> targetCls = targetClassReflection.getTypesAnnotatedWith(configuration.annotatedWith());
                //filter by subType
//            targetCls.retainAll(targetClassReflection.getSubTypesOf(configuration.subTypesOf()));
                //load target as schema
                load(targetCls.toArray(new Class<?>[0]));
            }
        }
    }

    protected void load(Class<?>... classes) {
        for (Class<?> cls : classes) {
            AvroData.get().getSchema(cls);
        }
    }
}

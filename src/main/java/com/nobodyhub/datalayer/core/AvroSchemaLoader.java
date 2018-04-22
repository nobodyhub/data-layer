package com.nobodyhub.datalayer.core;

import avro.shaded.com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.nobodyhub.datalayer.core.annotation.AvroSchemaLoaderConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Load {@link javax.persistence.Entity} class and create Avro Schema
 *
 * @author Ryan
 */
public final class AvroSchemaLoader {
    private Logger logger = Logger.getLogger(AvroSchemaLoader.class.getSimpleName());

    /**
     * Scan the classloader to load classes for Avro Schemas
     *
     * @throws ClassNotFoundException
     */
    public void preload() {
        logger.info("Start to preload classes for AvroSchema");
        //get annotation settings
        Reflections configurationReflection = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClassLoader())
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
        List<Class<?>> configurations = Lists.newArrayList(configurationReflection.getTypesAnnotatedWith(AvroSchemaLoaderConfiguration.class));
        if (configurations.isEmpty()) {
            throw new RuntimeException(String.format("Can not find configuration %s within classloaders %s!",
                    AvroSchemaLoaderConfiguration.class.getName(),
                    Joiner.on(", ").join(ClasspathHelper.classLoaders())));
        }
        for (Class<?> configCls : configurations) {
            logger.info(AvroSchemaLoaderConfiguration.class.getSimpleName() + " found on Class: " + configCls.getSimpleName());
            AvroSchemaLoaderConfiguration configuration = configCls.getAnnotation(AvroSchemaLoaderConfiguration.class);
            //get the target classes in base package
            Reflections targetClassReflection = new Reflections(new ConfigurationBuilder()
                    .addUrls(ClasspathHelper.forPackage(configuration.basePackage()))
                    .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false)));
            //filter by annotation
            Set<Class<?>> targetCls = targetClassReflection.getTypesAnnotatedWith(configuration.annotatedWith());
            //filter by subType
            targetCls.retainAll(targetClassReflection.getSubTypesOf(configuration.subTypesOf()));
            //load target as schema
            load(targetCls.toArray(new Class<?>[0]));
        }
    }

    protected void load(Class<?>... classes) {
        for (Class<?> cls : classes) {
            AvroData.get().getSchema(cls);
        }
    }
}

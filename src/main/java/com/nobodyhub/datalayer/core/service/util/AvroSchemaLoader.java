package com.nobodyhub.datalayer.core.service.util;

import avro.shaded.com.google.common.base.Joiner;
import com.nobodyhub.datalayer.core.avro.AvroData;
import com.nobodyhub.datalayer.core.service.common.AvroEntity;
import com.nobodyhub.datalayer.core.service.common.AvroSchemaLoaderConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Load {@link javax.persistence.Entity} class and create Avro Schema
 *
 * @author Ryan
 */
public final class AvroSchemaLoader {
    private Logger logger = Logger.getLogger(AvroSchemaLoader.class.getSimpleName());

    @PostConstruct
    public void setup() {
        preload();
    }

    /**
     * Scan the classloader to load classes for Avro Schemas
     *
     * @throws ClassNotFoundException
     */
    public void preload() {
        logger.info("Start to preload classes for AvroSchema");
//        String[] configurations = applicationContext.getBeanNamesForAnnotation(AvroSchemaLoaderConfiguration.class);
        String[] configurations = null;
        if (configurations.length == 0) {
            throw new RuntimeException(String.format("Can not find configuration %s within classloaders %s!",
                    AvroSchemaLoaderConfiguration.class.getName(),
                    Joiner.on(", ").join(ClasspathHelper.classLoaders())));
        }
        for (String beanName : configurations) {
            logger.info(AvroSchemaLoaderConfiguration.class.getSimpleName() + " found on Bean: " + beanName);
//            AvroSchemaLoaderConfiguration configuration = applicationContext.findAnnotationOnBean(beanName, AvroSchemaLoaderConfiguration.class);
            AvroSchemaLoaderConfiguration configuration = null;
            for (String path : configuration.basePackages()) {
                //get the target classes in base package
                Reflections targetClassReflection = new Reflections(new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(path))
                        .setScanners(new SubTypesScanner(false)));
                Set<Class<? extends AvroEntity>> targetCls = targetClassReflection.getSubTypesOf(AvroEntity.class);
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

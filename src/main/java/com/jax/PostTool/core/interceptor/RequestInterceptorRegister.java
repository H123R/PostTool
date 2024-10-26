package com.jax.PostTool.core.interceptor;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

import static com.jax.PostTool.core.constant.ClasspathConstants.BASE_PACKAGE;

public class RequestInterceptorRegister {

    static List<RequestInterceptor> interceptorList = new ArrayList<>();

    static {
        try {
            Set<Class<?>> classes = listAllSubclasses(BASE_PACKAGE, RequestInterceptor.class);
            for (Class<?> aClass : classes) {
                Constructor<?> constructor = aClass.getConstructor();
                RequestInterceptor interceptor = (RequestInterceptor) constructor.newInstance();
                register(interceptor);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static Set<Class<?>> getClasses(String pack, Predicate<Class<?>> filter) {
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metaFactory = new SimpleMetadataReaderFactory(patternResolver);

        String path = ClassUtils.convertClassNameToResourcePath(pack);
        String location = ResourceUtils.CLASSPATH_URL_PREFIX + path + "/**/*.class";
        Resource[] resources;

        Set<Class<?>> result = new HashSet<>();
        try {
            resources = patternResolver.getResources(location);
            for (Resource resource : resources) {
                MetadataReader metaReader = metaFactory.getMetadataReader(resource);
                if (resource.isReadable()) {
                    String clazzName = metaReader.getClassMetadata().getClassName();
                    Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzName);
                    if (filter.test(clazz)) {
                        result.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Set<Class<?>> listAllSubclasses(String scanPackage, Class<?> parent) {
        return getClasses(scanPackage, (clazz) ->
                parent.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers()));
    }

    public static List<RequestInterceptor> getInterceptor() {
        interceptorList.sort(Comparator.comparing(RequestInterceptor::getOrder));
        return interceptorList;
    }

    public static void register(RequestInterceptor interceptor) {
        interceptorList.add(interceptor);
    }



}

package com.darren.cloud.common.utils;

import com.darren.cloud.common.exception.ServiceException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;

/**
 * 根据包名称扫描所有的Class文件
 *
 * @author: LI.YUANBO
 * @create: 2018-04-28
 **/
public class ClassScanUtils implements ResourceLoaderAware {

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    /**
     * 根据多个包扫描所有的class文件
     * 
     * @param basePackages 基础包路径数组，支持通配符:com/amily/※※/task，包括扫描子包
     * @return class文件集合
     */
    public static Set<Class<?>> scan(String[] basePackages) {
        ClassScanUtils cs = new ClassScanUtils();
        Set<Class<?>> classes = new HashSet<>();

        for (String s : basePackages) {
            classes.addAll(cs.doScan(s));
        }
        return classes;
    }

    /**
     * 根据1个包扫描所有的class文件
     * 
     * @param basePackages 基础包路径，支持通配符:com/amily/※※/task，包括扫描子包
     * @return class文件集合
     */
    public static Set<Class<?>> scan(String basePackages) {
        return ClassScanUtils.scan(StringUtils.tokenizeToStringArray(basePackages, ",; \t\n"));
    }

    /**
     * 设置资源加载
     * 
     * @param resourceLoader 资源加载
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    /**
     * 根据包扫描所有的class文件实现
     * 
     * @param basePackage 包路径
     * @return 返回所有扫描的class文件集合
     */
    private Set<Class<?>> doScan(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + org.springframework.util.ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage))
                    + "/**/*.class";
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);

            for (int i = 0; i < resources.length; i++) {
                Resource resource = resources[i];
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
                }
            }
        } catch (IOException ex) {
            throw new ServiceException("I/O failure during classpath scanning", ex);
        } catch (ClassNotFoundException ex) {
            throw new ServiceException("Class Not Fount Exception classpath scanning", ex);
        }
        return classes;
    }

}

package org.prcsteel.rest.proxy;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.prcsteel.rest.annotation.RestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * @author zhoukun
 */
public class RestAppContextAware implements ApplicationContextAware {
	
	private static final Logger logger = LoggerFactory.getLogger(RestAppContextAware.class);

	private ApplicationContext applicationContext;
	private ConfigurableApplicationContext configurableApplicationContext;
	private List<String> basePackages;
	
    private BeanDefinitionRegistry beanDefinitonRegistry;
    private ResourcePatternResolver resourcePatternResolver;
    private MetadataReaderFactory metadataReaderFactory;
	
	public void setBasePackages(List<String> basePackages) {
		this.basePackages = basePackages;
	}

	public void setBeanDefinitonRegistry(BeanDefinitionRegistry beanDefinitonRegistry) {
		this.beanDefinitonRegistry = beanDefinitonRegistry;
	}

	public void setResourcePatternResolver(ResourcePatternResolver resourcePatternResolver) {
		this.resourcePatternResolver = resourcePatternResolver;
	}

	public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory) {
		this.metadataReaderFactory = metadataReaderFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		this.configurableApplicationContext = (ConfigurableApplicationContext) this.applicationContext;
		setBeanDefinitonRegistry((BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory());
	}

	@PostConstruct
	public void init(){
		if(basePackages == null || basePackages.size() == 0){
			return;
		}
		if(resourcePatternResolver == null){
			resourcePatternResolver = new PathMatchingResourcePatternResolver();
		}
		if(metadataReaderFactory == null){
			metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
		}
		
		for (String bp : basePackages) {
			logger.info("Search rest api in package: {}",bp);
			String baskPkg = String.format("%s%s/**/*.class", ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX,ClassUtils.convertClassNameToResourcePath(bp));
			try {
				Resource[] resources = resourcePatternResolver.getResources(baskPkg);
				if(resources == null){
					continue;
				}
				for (Resource r : resources) {
					MetadataReader metaReader = metadataReaderFactory.getMetadataReader(r);
					if(!metaReader.getClassMetadata().isInterface()){
						continue;
					}
					Class<?> apiClass = Class.forName(metaReader.getClassMetadata().getClassName());
					RestApi api = apiClass.getAnnotation(RestApi.class);
					if(api == null){
						continue;
					}
					registerBean(api, apiClass);
				}
			} catch (IOException | ClassNotFoundException e) {
				logger.error("Parse resource failed.",e);
			}
		}
	}
	
	private void registerBean(RestApi api,Class<?> apiClass){
		String beanName = api.value();
		if(StringUtils.isEmpty(beanName)){
			beanName = apiClass.getName();
			if(beanName.contains(".")){
				beanName = beanName.substring(beanName.lastIndexOf(".") + 1);
			}
			beanName = Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);					
		}
		
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(apiClass);
		AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		beanDefinition.setFactoryBeanName("restApiFactoryBean");
		beanDefinition.setFactoryMethodName("buildApi");
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(apiClass);
		beanDefinitonRegistry.registerBeanDefinition(beanName, beanDefinition);
		
		logger.info("Register rest api service: {},Interface: {}",beanName,apiClass.getName());
	}
}

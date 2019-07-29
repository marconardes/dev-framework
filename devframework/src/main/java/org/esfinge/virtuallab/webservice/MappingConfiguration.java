package org.esfinge.virtuallab.webservice;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.esfinge.virtuallab.*")
public class MappingConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		if (!registry.hasMappingForPattern("/webjars/**")) {
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
		}
		
		if (!registry.hasMappingForPattern("/resources/**")) {
			registry
		      .addResourceHandler("/resources/**")
		      .addResourceLocations("classpath:/statics/")
		      .setCachePeriod(3600)
		      .resourceChain(true)
		      .addResolver(new PathResourceResolver());
		}

		registry.setOrder(1);
	}
}
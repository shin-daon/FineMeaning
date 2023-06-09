package com.fin.proj.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver dotSuResolver() {
		ClassLoaderTemplateResolver dotDo = new ClassLoaderTemplateResolver();
		
		dotDo.setPrefix("templates/views/support/");
		dotDo.setSuffix(".html");
		dotDo.setTemplateMode(TemplateMode.HTML);
		dotDo.setCharacterEncoding("UTF-8");
		dotDo.setOrder(1);
		dotDo.setCacheable(false);
		dotDo.setCheckExistence(true);
		
		return dotDo;
	}
}

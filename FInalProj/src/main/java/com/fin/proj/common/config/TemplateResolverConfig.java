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
	
	@Bean
	public ClassLoaderTemplateResolver dotBoResolver() {
		ClassLoaderTemplateResolver dotBo = new ClassLoaderTemplateResolver();
		dotBo.setPrefix("templates/views/board/");
		dotBo.setSuffix(".html");
		dotBo.setTemplateMode(TemplateMode.HTML);
		dotBo.setCharacterEncoding("UTF-8");
		dotBo.setOrder(1);
		dotBo.setCacheable(false);
		dotBo.setCheckExistence(true);
		
		return dotBo;

		
	}

	@Bean
	public ClassLoaderTemplateResolver dotMeResolver() {
		ClassLoaderTemplateResolver dotMe = new ClassLoaderTemplateResolver();
		
		dotMe.setPrefix("templates/views/member/");
		dotMe.setSuffix(".html");
		dotMe.setTemplateMode(TemplateMode.HTML);
		dotMe.setCharacterEncoding("UTF-8");
		dotMe.setOrder(1);
		dotMe.setCacheable(false);
		dotMe.setCheckExistence(true);
		
		return dotMe;
	}
}

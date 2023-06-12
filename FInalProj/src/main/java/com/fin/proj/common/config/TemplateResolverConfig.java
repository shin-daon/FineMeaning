package com.fin.proj.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class TemplateResolverConfig {
	
	@Bean
	public ClassLoaderTemplateResolver dotVoResolver() {
		ClassLoaderTemplateResolver dotVo = new ClassLoaderTemplateResolver();
		
		dotVo.setPrefix("templates/views/volunteer/");
		dotVo.setSuffix(".html");
		dotVo.setTemplateMode(TemplateMode.HTML);
		dotVo.setCharacterEncoding("UTF-8");
		dotVo.setOrder(1);
		dotVo.setCacheable(false);
		dotVo.setCheckExistence(true);
		
		return dotVo;
	}
	
	@Bean
	public ClassLoaderTemplateResolver dotSuResolver() {
		ClassLoaderTemplateResolver dotSu = new ClassLoaderTemplateResolver();
		
		dotSu.setPrefix("templates/views/support/");
		dotSu.setSuffix(".html");
		dotSu.setTemplateMode(TemplateMode.HTML);
		dotSu.setCharacterEncoding("UTF-8");
		dotSu.setOrder(1);
		dotSu.setCacheable(false);
		dotSu.setCheckExistence(true);
		
		return dotSu;
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

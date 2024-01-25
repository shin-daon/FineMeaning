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
	public ClassLoaderTemplateResolver dotUsResolver() {
		ClassLoaderTemplateResolver dotUs = new ClassLoaderTemplateResolver();
		
		dotUs.setPrefix("templates/views/user/");
		dotUs.setSuffix(".html");
		dotUs.setTemplateMode(TemplateMode.HTML);
		dotUs.setCharacterEncoding("UTF-8");
		dotUs.setOrder(1);
		dotUs.setCacheable(false);
		dotUs.setCheckExistence(true);
		
		return dotUs;
	}
	
}

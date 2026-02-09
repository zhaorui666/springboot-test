package com.zr.config.mvc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
//		configurer.addPathPrefix("/api", HandlerTypePredicate.forAnnotation(Controller.class));
	}

	@Override
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		RequestMappingHandlerMapping mapping = new CustomRequestMappingInfoHandlerMapping();
		return mapping;
	}

//	@Bean
//	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//		return new CustomRequestMappingInfoHandlerMapping();
//	}
}

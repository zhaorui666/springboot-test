package com.zr.config.mvc;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

public class CustomRequestMappingInfoHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {
		ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
		if (apiVersion != null) {
			return new ApiVersionRequestCondition(apiVersion.value());
		}
		return null;
	}
}

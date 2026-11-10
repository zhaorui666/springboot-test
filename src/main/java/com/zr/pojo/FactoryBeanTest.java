package com.zr.pojo;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class FactoryBeanTest implements FactoryBean<FactoryBeanActually> {

	@Override
	public FactoryBeanActually getObject() {
		return new FactoryBeanActually();
	}

	@Override
	public Class<?> getObjectType() {
		return FactoryBeanActually.class;
	}
}

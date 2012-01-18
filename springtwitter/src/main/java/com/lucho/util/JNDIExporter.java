package com.lucho.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public final class JNDIExporter implements InitializingBean {
	
	private String name;

	private Object objectToBind;

	public String getName() {
		return name;
	}

	@Required
	public void setName(String name) {
		this.name = name;
	}

	public Object getObjectToBind() {
		return objectToBind;
	}

	@Required
	public void setObjectToBind(Object objectToBind) {
		this.objectToBind = objectToBind;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Context initialContext = new InitialContext();
		Context compContext;
		try { 
			compContext = (Context) initialContext.lookup("comp");
		} catch(NameNotFoundException e) {
			compContext = initialContext.createSubcontext("comp");
		}
		compContext.bind(this.getName(), this.getObjectToBind());		
	}

}

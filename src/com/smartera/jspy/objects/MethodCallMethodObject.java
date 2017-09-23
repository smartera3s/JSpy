package com.smartera.jspy.objects;

import javassist.expr.MethodCall;

public class MethodCallMethodObject extends MethodObject{

	private MethodCall actualMethodCallObject;
	
	public MethodCallMethodObject(final MethodCall method){
		this.nameWithoutMethodSignature	=	method.getMethodName();
		this.nameWithMethodSignature	=	this.nameWithoutMethodSignature + "." + method.getSignature();
		this.ClassThatContainsTheMethod	=	method.getClassName();
		this.setActualMethodCallObject(method);
	}

	public MethodCall getActualMethodCallObject() {
		return actualMethodCallObject;
	}

	public void setActualMethodCallObject(MethodCall actualMethodCallObject) {
		this.actualMethodCallObject = actualMethodCallObject;
	}

}

package com.smartera.jspy.objects;

import javassist.CtBehavior;

public class CtBehaviorMethodObject extends MethodObject{
	
	public CtBehavior	actualMethodInstance;

	private CtBehaviorMethodObject() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CtBehaviorMethodObject(final CtBehavior method, final String ClassThatContainsTheMethod){
		this.nameWithoutMethodSignature	=	method.getName();
		this.nameWithMethodSignature	=	this.nameWithoutMethodSignature + "." + method.getSignature();
		this.ClassThatContainsTheMethod	=	ClassThatContainsTheMethod;
		this.actualMethodInstance		=	method;
	}

}

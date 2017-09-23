package com.smartera.jspy.objects;

import javassist.CtBehavior;
import javassist.expr.MethodCall;

import org.json.JSONException;
import org.json.JSONObject;

import exception.InvalidQueryOperationException;
import exception.QueryException;
import PersistenceManagerReturnTypes.ResultList;
import Query.Query;

public class MethodObject {

	protected String 	nameWithoutMethodSignature;
	protected String 	nameWithMethodSignature;
	protected String 	ClassThatContainsTheMethod;
	
	protected MethodObject() {
	}
	
	public MethodObject(final CtBehavior method, final String ClassThatContainsTheMethod){
		this.nameWithoutMethodSignature	=	method.getName();
		this.nameWithMethodSignature	=	this.nameWithoutMethodSignature + "." + method.getSignature();
		this.ClassThatContainsTheMethod	=	ClassThatContainsTheMethod;
	}
		
	public String getNameWithoutMethodSignature() {
		return nameWithoutMethodSignature;
	}
	
	public void setNameWithoutMethodSignature(String nameWithoutMethodSignature) {
		this.nameWithoutMethodSignature = nameWithoutMethodSignature;
	}
	
	public String getNameWithMethodSignature() {
		return nameWithMethodSignature;
	}
	
	public void setNameWithMethodSignature(String nameWithMethodSignature) {
		this.nameWithMethodSignature = nameWithMethodSignature;
	}
	
	public String getClassThatContainsTheMethod() {
		return ClassThatContainsTheMethod;
	}
	
	public void setClassThatContainsTheMethod(String classThatContainsTheMethod) {
		ClassThatContainsTheMethod = classThatContainsTheMethod;
	}
	
	public JSONObject	toJson(){	
		JSONObject	methodInstanceInJsonFormat	=	new JSONObject();
		
		try {
			methodInstanceInJsonFormat.put("nameWithoutMethodSignature", nameWithoutMethodSignature);
			methodInstanceInJsonFormat.put("nameWithMethodSignature", nameWithMethodSignature);
			methodInstanceInJsonFormat.put("ClassThatContainsTheMethod", ClassThatContainsTheMethod);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return methodInstanceInJsonFormat;	
	}
	
	public  static boolean doesMethodExists(JSONObject methodInstance) throws InvalidQueryOperationException, QueryException, JSONException{
		ResultList	result	=	Query.builder()
									 .table("methods")
									 .find("nameWithMethodSignature")
									 .where("nameWithMethodSignature", "=", methodInstance.getString("nameWithMethodSignature"))
									 .execute();

		return (result.size() > 0) ? true : false;
	}
	
	
	
	
}

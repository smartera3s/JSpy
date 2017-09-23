package com.smartera.jspy.objects;

import org.json.JSONException;
import org.json.JSONObject;

import PersistenceManagerReturnTypes.ResultList;
import Query.Query;
import exception.ConditionException;
import exception.InvalidQueryOperationException;
import exception.MissingConditionParameterException;
import exception.QueryException;

public class IntraMethodCallObject {

	private static	MethodObject	fromMethod;
	private	static 	MethodObject	toMethod;
	private static	String			interactionID;
	
	public IntraMethodCallObject(MethodObject fromMethod, MethodObject toMethod){
		IntraMethodCallObject.fromMethod	=	fromMethod;
		IntraMethodCallObject.toMethod		=	toMethod;
		IntraMethodCallObject.interactionID	=	IntraMethodCallObject.setInteractionID(); //TODO-b : get smarter Idea for generating IDs for Interactions
	}
	
	private static String	setInteractionID(){
		return	IntraMethodCallObject.fromMethod.ClassThatContainsTheMethod +"."+ IntraMethodCallObject.fromMethod.getNameWithMethodSignature()+"-to-"+IntraMethodCallObject.toMethod.getNameWithMethodSignature();
	}
	
	public static JSONObject	toJson() throws JSONException{
		
		JSONObject	IntraMethodCallInstanceAsJson	=	new JSONObject();
		IntraMethodCallInstanceAsJson.put("interactionID", IntraMethodCallObject.interactionID);

		IntraMethodCallInstanceAsJson.put("fromMethod", IntraMethodCallObject.fromMethod.getNameWithoutMethodSignature());
		IntraMethodCallInstanceAsJson.put("fromClass", 	IntraMethodCallObject.fromMethod.getClassThatContainsTheMethod());
		
		IntraMethodCallInstanceAsJson.put("toMethod",	IntraMethodCallObject.toMethod.getNameWithoutMethodSignature());
		IntraMethodCallInstanceAsJson.put("toClass",	IntraMethodCallObject.toMethod.getClassThatContainsTheMethod());
		
		return IntraMethodCallInstanceAsJson;
	}
	
	public static boolean doesInteractionExist(JSONObject intraMethodCallInstace) throws InvalidQueryOperationException, QueryException, JSONException, MissingConditionParameterException, ConditionException{
			
		//TODO-b: using Statement is just a work around to supply multiple Where Conditions Until its implmeneted with a where(JSONObject)
		ResultList	result											=	Query.builder()
																			 .table("intraMethodCalls")
																			 .find()
																			 .where("interactionID","=", IntraMethodCallObject.interactionID)
																			 .execute();

		return (result.size() > 0) ? true : false;
	}
}

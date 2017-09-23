package com.smartera.jspy.processor;

import org.json.JSONException;
import org.json.JSONObject;

import com.smartera.jspy.objects.ClassObject;
import com.smartera.jspy.objects.IntraMethodCallObject;
import com.smartera.jspy.objects.MethodObject;

import Query.Query;
import exception.ConditionException;
import exception.InvalidQueryOperationException;
import exception.MissingConditionParameterException;
import exception.QueryException;

public class DependenciesMapper {
	
	
	
	public static void persisteClass(JSONObject classInstance) throws JSONException, InvalidQueryOperationException, QueryException{
		if(!ClassObject.doesClassExists(classInstance)){
			
			Query.builder()
			 .table("classes")
			 .insert(classInstance)
			 .execute();
		}	
	}
	
	public static void persisteMethod(JSONObject methodInstance) throws InvalidQueryOperationException, QueryException, JSONException{
		if(!MethodObject.doesMethodExists(methodInstance)){
			Query.builder()
			 .table("methods")
			 .insert(methodInstance)
			 .execute();
		}
	}
	
	public static void registerIntraMethodInteraction(JSONObject IntraMethodCallInstance) throws InvalidQueryOperationException,
								QueryException, JSONException, MissingConditionParameterException, ConditionException{
		
		if(!IntraMethodCallObject.doesInteractionExist(IntraMethodCallInstance)){
			Query.builder()
				 .table("intraMethodCalls")
				 .insert(IntraMethodCallInstance)
				 .execute();
		}
	}
}

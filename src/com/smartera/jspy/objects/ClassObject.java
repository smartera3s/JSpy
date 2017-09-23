package com.smartera.jspy.objects;

import org.json.JSONException;
import org.json.JSONObject;

import exception.InvalidQueryOperationException;
import exception.QueryException;
import PersistenceManagerReturnTypes.ResultList;
import Query.Query;

public class ClassObject {

	private static String 	name;
	
	public ClassObject(String name){
		this.name	=	name;
	}
	
	public static String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	
	
	public static JSONObject toJson(){
		
		JSONObject	classObjectInJsonFormat	=	new	JSONObject();
		try {
			classObjectInJsonFormat.put("className", ClassObject.getName());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//must be handled with a custom exception
			e.printStackTrace();
		}
		
		return classObjectInJsonFormat;
	}
	
	public static boolean doesClassExists(JSONObject classInstance) throws InvalidQueryOperationException, QueryException, JSONException{
		
		ResultList	result	=	Query.builder()
									 .table("classes")
									 .find("className")
									 .where("className", "=", classInstance.getString("className"))
									 .execute();
		
		return (result.size() > 0) ? true : false;
	}
}

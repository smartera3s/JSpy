package com.smartera.monitor.spy;

import java.lang.instrument.Instrumentation;

import com.smartera.monitor.transformers.ClassesDescriptor;

import configuration.PersistenceConfiguration;


public class Agent {
	static{
		PersistenceConfiguration.initConnection("/home/bmostafa/jspyDatabase.properties");
	}

	public static void premain(String agentArguments,Instrumentation instrumentation){
			instrumentation.addTransformer(new ClassesDescriptor());
	}
	
	
}

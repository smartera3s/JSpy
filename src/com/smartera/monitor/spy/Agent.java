package com.smartera.monitor.spy;

import java.lang.instrument.Instrumentation;

import com.smartera.monitor.transformers.ClassesDescriptor;

public class Agent {

	public static void premain(String agentArguments,Instrumentation instrumentation){
			System.out.println("Agent Started");
			instrumentation.addTransformer(new ClassesDescriptor());
	}
	
}

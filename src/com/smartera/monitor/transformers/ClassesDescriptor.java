package com.smartera.monitor.transformers;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ClassesDescriptor implements ClassFileTransformer{

    static final String[] DEFAULT_EXCLUDES = new String[] {"com/sun/", "sun/", "java/", 
    	"javax/", "org/slf4j","org/apache","com/mongodb","org/bson","org/codehaus",
    	"com/thoughtworks","org/sonatype", "org/xml", "org/json", "com/goebl", "jdk", "org/jcp"};

    static final String[] INCLUDE	=	new String[]{"com/smartera"};
    @Override
	public byte[] transform( final ClassLoader loader, final String className, final Class clazz,
	        final java.security.ProtectionDomain domain, final byte[] bytes ) {
    	
		for( int i = 0; i < INCLUDE.length; i++ ) {

	            if( className.startsWith( INCLUDE[i] ))  {
	            	return doClass( className, clazz, bytes, loader);
	               
	            }
	        }
		
		 return bytes;
        
	}
	
	  private byte[] doClass( final String name, final Class clazz, byte[] b, final ClassLoader loader ) {
	        
		  	ClassPool pool = ClassPool.getDefault();
		  	pool.childFirstLookup = true;
           
		  	pool.insertClassPath(new LoaderClassPath(loader));
            
	        CtClass cl = null;

	        try {
	        	
	        	String qualifiedClassName = name.replace("/", ".");

	            cl	=	pool.get(qualifiedClassName);
	            
	            if( cl.isInterface() == false ) {

	            
	               CtBehavior[] methods = cl.getDeclaredBehaviors();
	               
	                
	               for( int i = 0; i < methods.length; i++ ) {

	                    if( methods[i].isEmpty() == false ) {
	                        doMethod( methods[i] );
	                    }
	                }
//	                b = cl.toBytecode();
	            }
	        } catch( Exception e ) {
	            System.err.println( "Could not instrument  " + name + ",  exception : " + e.getMessage() );
	        } finally {

	            if( cl != null ) {
	                cl.detach();
	            }
	        }

	        return b;
	    }
	  
	    private void doMethod( final CtBehavior method ) throws NotFoundException, CannotCompileException {
	    	String methodName	=	method.getName();

	    	 method.instrument(
	    		        new ExprEditor() {
	    		            public void edit(MethodCall m)
	    		                          throws CannotCompileException
	    		            {
	    		                System.out.println(" [Method]  " + m.getClassName() + "." + m.getMethodName() + "." + m.getSignature());
	    		            }
	    		        });
	    	
	    
	    }

}

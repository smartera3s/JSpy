package com.smartera.monitor.transformers;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.UUID;

import org.json.JSONException;

import com.smartera.jspy.objects.ClassObject;
import com.smartera.jspy.objects.CtBehaviorMethodObject;
import com.smartera.jspy.objects.IntraMethodCallObject;
import com.smartera.jspy.objects.MethodCallMethodObject;
import com.smartera.jspy.objects.MethodObject;
import com.smartera.jspy.processor.DependenciesMapper;

import exception.ConditionException;
import exception.InvalidQueryOperationException;
import exception.MissingConditionParameterException;
import exception.QueryException;
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

    static final String[] INCLUDE	=	new String[]{"com/smartera","configurations","LogsHandlers","webAppServlets", "conditionsManager",
    												"dataHandlers","exception","fbMessages","mongojdbc","persistenceManager","persistenceManagerReturnTypes",
    												"persistenceObject","Query","storageLayerUtils","utilites"};
    
    @Override
	public byte[] transform( final ClassLoader loader, final String className, final Class clazz,
	        final java.security.ProtectionDomain domain, final byte[] bytes ) {
    	
		for( int i = 0; i < INCLUDE.length; i++ ) {

	            if( className.startsWith( INCLUDE[i] ))  {
	            	return doClass(domain, className, clazz, bytes, loader);	               
	            }
	        }
		
		 return bytes; 
	}
	
	  private byte[] doClass(final java.security.ProtectionDomain domain, final String name, final Class clazz, byte[] bytes, final ClassLoader loader ) {
	        
		  	ClassPool pool = ClassPool.getDefault();
		  	pool.childFirstLookup = true;
		  	pool.insertClassPath(new LoaderClassPath(loader));    
		  	
	        CtClass cl = null;

	        try {
	        	
	        	String qualifiedClassName = name.replace("/", ".");
	            cl	=	pool.get(qualifiedClassName);
	            
	            DependenciesMapper.persisteClass(new ClassObject(qualifiedClassName).toJson());
	            
	            if( cl.isInterface() == false ) {
	               CtBehavior[] methods = cl.getDeclaredBehaviors();

	               for( int i = 0; i < methods.length; i++ ) {

	                    if( methods[i].isEmpty() == false ) {
	                        doMethod(qualifiedClassName, methods[i] );
	                    }
	                }
	            }
	        } catch( Exception e ) {
	            System.err.println( "Could not instrument  " + name);
	            e.printStackTrace();
	        } finally {

	            if( cl != null ) {
	                cl.detach();
	            }
	        }
	        return bytes;
	    }
	  
	    private void doMethod(final String currentClassName, final CtBehavior method ) throws NotFoundException, CannotCompileException, InvalidQueryOperationException, QueryException, JSONException {
	    	
	    	final CtBehaviorMethodObject	currentMethodfrom	=	new CtBehaviorMethodObject(method, currentClassName);
	    	DependenciesMapper.persisteMethod(currentMethodfrom.toJson());
	    	this.instrumentMethod(currentMethodfrom);	
	    }
	    
	    private void instrumentMethod(final CtBehaviorMethodObject currentMethodFrom ) throws CannotCompileException{
	    	//TODO-b: this expression needs to be simplified
	    	currentMethodFrom.actualMethodInstance.instrument(
    		        new ExprEditor() {
    		            public void edit(MethodCall m) throws  CannotCompileException
    		            {
    		            	for( int i = 0; i < INCLUDE.length; i++ ) {

    		    	            if( m.getClassName().startsWith( INCLUDE[i] ))  {
    		    	            	
    		    	            	final	MethodCallMethodObject	currentMethodTo	=	new	MethodCallMethodObject(m);
    		    	            	
    		    	            	try {
    		    	            		DependenciesMapper.persisteMethod(currentMethodTo.toJson());
    		    	            		DependenciesMapper.registerIntraMethodInteraction(new IntraMethodCallObject(currentMethodFrom, currentMethodTo).toJson());
									
    		    	            	} catch (InvalidQueryOperationException
											| QueryException | JSONException | MissingConditionParameterException | ConditionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
    		    	            }
    		    	        }
    		            }
    		        });
	    }

}

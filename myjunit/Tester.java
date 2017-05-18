/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjunit;

import annotations.Test;
import annotations.Before;
import annotations.After;

import assertions.TestAssertionError;
import java.lang.reflect.Method;
import java.util.LinkedList;


/**
 *
 * @author Vladislav Oshkanov
 */


public class Tester extends Thread{
    private final LinkedList<Class> testingClasses;
    private LinkedList<LinkedList<String>> testingResults;
    private Method[] methods;
    private Class testingClass;
    private int pass;
    private int fail;
    private LinkedList<String> result;
    private Object testingClassObject; 
    
    
    public Tester (LinkedList<Class> itestingClasses, LinkedList<LinkedList <String>> itestingResults)
    {
        testingClasses = itestingClasses;
        testingResults = itestingResults;
        
    }
    
    public void run()
    {
        pass = 0;
        fail = 0;
        
        while (!testingClasses.isEmpty()){
            testingClass = testingClasses.removeLast();
            
            if (testingClass == null) break; // if another thread got class after checking
            
            methods = testingClass.getMethods();
            result = new LinkedList();
            try {
                testingClassObject = testingClass.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        
            
            runTestMethods();
            
            result.addLast ("Successed:" + pass + "   Failed:" + fail);
            testingResults.addLast(result);
            
            
            pass = 0;
            fail = 0;
        }
        
        
    }
    
    private void printResult(){
        result.addLast ("Successed:" + pass + "   Failed:" + fail);
        while (!result.isEmpty()){
            System.out.println (result.removeFirst());
        }
    }
    
    private void runMethodWithAnnotation (Class annotation){
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                try {
                    method.invoke(testingClassObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void addResultToList (boolean isSuccessed, String exceptionName, 
                                            String methodName, boolean print){
        
        String tmpRes = "";
        
        tmpRes += (isSuccessed) ? "Success. " : "FAIL!!! "; 
        tmpRes += (exceptionName.equals("")) ? "No exceptions" : exceptionName;
        tmpRes += " have(s) been thrown in method ";
        tmpRes += methodName;
        
        result.addLast(tmpRes);
       
        if (print){
            System.out.println(tmpRes + " of class " + testingClass.getCanonicalName());
        }
        
    }
    private void runTestMethods() 
    {
    
        for (Method method : methods){
            
            if (method.isAnnotationPresent(Test.class)) {
                Test test = method.getAnnotation(Test.class);
                Class expected = test.expected();
                    
                runMethodWithAnnotation (Before.class);
                
                    
                
                try {
                    method.invoke(testingClassObject);
                    if (expected == Exception.class) {
                        pass++;
                        addResultToList (true, "", method.getName(), true);
                    }
                    else {
                        fail++;
                        addResultToList (false, "", method.getName(), true);
                    }
                }
                catch (Exception e) {
                    Class thrownException = e.getCause().getClass();
                    if (thrownException == TestAssertionError.class) {
                        
                        addResultToList (false, "TestAssertionException", method.getName(), true);
                        fail++;
                    }
                    else if (thrownException != expected) {
                        addResultToList (false, thrownException.getName(), method.getName(), true);
                        fail++;
                    }
                    else {
                        addResultToList (false, "Excepted exception", method.getName(), true);
                        pass++;
                    }
                }
                
                runMethodWithAnnotation (After.class);
            }
        }
    }
      
}

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
* Runner of tests for classes of testing Queue. 
* This class receives links to LinkedList of classes for testing and
* to LinkedList of Linked Lists of String's with results of testing. Method run() 
* gets Classes in cycle and implements methods with Before, Test and After
* annotations. After each test it appends string with result of this test
* to LinkedList of Strings. After implementing of all methods it append LinkedList
* of results of testing each class to common queue. 
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
    
    /*
    * Constructor  receives links to LinkedList of classes for testing and
    * to LinkedList of Linked Lists of String's with results of testing.
    * @param itestingClasses classes for testing.
    * @param itestingResults queue of results.
    */
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
            synchronized (testingClasses){
                if (testingClasses.isEmpty()) break;
                testingClass = testingClasses.removeLast();
            }            
            methods = testingClass.getMethods();
            result = new LinkedList();
            try {
                testingClassObject = testingClass.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        
            
            runTestMethods();
            
            result.addFirst ("Testing of class " + testingClass.getName() + " finished!");
            result.addLast ("Successed:" + pass + "   Failed:" + fail);
            testingResults.addLast(result);
            
            
            pass = 0;
            fail = 0;
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
        
        StringBuffer strBuf = new StringBuffer ("");
        
        strBuf.append((isSuccessed) ? "Success. " : "FAIL!!! "); 
        strBuf.append((exceptionName.equals("")) ? "No exceptions" : exceptionName);
        strBuf.append(" have(s) been thrown in method ");
        strBuf.append(methodName);
        
        result.addLast(strBuf.toString());
       
        if (print){
            System.out.println(strBuf + " of class " + testingClass.getCanonicalName());
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
                        addResultToList (false, "Expected exception", method.getName(), true);
                        pass++;
                    }
                }
                
                runMethodWithAnnotation (After.class);
            }
        }
    }
      
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjunit;

import annotations.Test;
import assertions.TestAssertionError;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vladislav Oshkanov
 */


public class Tester 
{
    public Tester (Class testingClass)
    {
        pass_ = 0;
        fail_ = 0;
        testingClass_ = testingClass;
        methods_ = testingClass_.getMethods();
        result_ = new PriorityQueue(methods_.length);
    }
    public void run()
    {
        runTestMethods();
        printResult();
        
    }
    private void printResult()
    {
        while (!result_.isEmpty()){
            System.out.println (result_.remove());
        }
    }
    private void runTestMethods() 
    {
    
        for (Method method : methods_){
            
                
                
                if (method.isAnnotationPresent(Test.class)) 
                {
                    System.out.println("ok");
                    Test test = method.getAnnotation(Test.class);
                    Class expected = test.expected();
                    
                    try
                    {
                        method.invoke(testingClass_.newInstance());
                        pass_++;
                    }
                    catch (Exception e) 
                    {
                        Class thrownException = e.getCause().getClass();
                        if (thrownException == TestAssertionError.class)
                        {
                            result_.add("FAIL!!! TestAssertionException have been thrown in method: " 
                                    + method.getName() + "\n");
                            e.printStackTrace();
                            fail_++;
                        }
                        else if (thrownException != expected)
                        {
                            result_.add("FAIL!!!." + 
                                    thrownException.getName() + "have been thrown in method: " 
                                    + method.getName());
                            e.printStackTrace();
                            fail_++;
                        }
                        else 
                        {
                            result_.add("Success. Expected expection have been thrown in method" 
                                    + method.getName());
                            pass_++;
                        }
                    }
                }
        }
    }
    private Queue<String> getResult()
    {
        result_.add ( "Passed" + Integer.toString(pass_) 
                + ". Failed:" + Integer.toString(fail_));
        return result_;
        
    }

    private Method[] methods_;
    private Class testingClass_;
    private int pass_;
    private int fail_;
    private Queue<String> result_;
}

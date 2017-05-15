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
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
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
        try {
            testingClassObject_ = testingClass.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void run()
    {
        runMethodWithAnnotation (Before.class);
        runTestMethods();
        runMethodWithAnnotation (After.class);
        printResult();
        
    }
    private void printResult()
    {
        while (!result_.isEmpty()){
            System.out.println (result_.remove());
        }
        System.out.println("Successed:" + pass_ + "   Failed:" + fail_);
    }
    private void runMethodWithAnnotation (Class annotation)
    {
        for (Method method : methods_)
        {
            if (method.isAnnotationPresent(annotation))
            {
                try {
                    method.invoke(testingClassObject_);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Tester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    private void runTestMethods() 
    {
    
        for (Method method : methods_)
        {
            
                
                
                if (method.isAnnotationPresent(Test.class)) 
                {
                    Test test = method.getAnnotation(Test.class);
                    Class expected = test.expected();
                    
                    try
                    {
                        method.invoke(testingClassObject_);
                        if (expected == Exception.class)
                        {
                            pass_++;
                            result_.add("Success. No exceptions have been thrown in method: " 
                                + method.getName());
                        }
                        else 
                        {
                            fail_++;
                            result_.add("FAIL!!! No exceptions have been thrown in method: "
                                + method.getName());
                        }
                    }
                    catch (Exception e) 
                    {
                        Class thrownException = e.getCause().getClass();
                        if (thrownException == TestAssertionError.class)
                        {
                            result_.add("FAIL!!! TestAssertionException have been thrown in method: " 
                                    + method.getName() + "\n");
                            fail_++;
                        }
                        else if (thrownException != expected)
                        {
                            result_.add("FAIL!!!." + 
                                    thrownException.getName() + " have been thrown in method: " 
                                    + method.getName());
                            fail_++;
                        }
                        else 
                        {
                            result_.add("Success. Expected expection have been thrown in method: " 
                                    + method.getName());
                            pass_++;
                        }
                    }
                }
        }
    }
    

    private Method[] methods_;
    private Class testingClass_;
    private int pass_;
    private int fail_;
    private Queue<String> result_;
    private Object testingClassObject_; 
}

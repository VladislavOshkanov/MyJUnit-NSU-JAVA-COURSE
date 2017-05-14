/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjunit;

import annotations.Test;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    }
    public void run()
    {
        runTestMethods();
        System.out.println(this.getResult());
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
                        if (e.getCause().getClass() != expected)
                        {
                            e.printStackTrace();
                            fail_++;
                        }
                        else
                        {
                            pass_++;
                        }
                    }
                }
        }
    }
    private String getResult()
    {
        return "Passed" + Integer.toString(pass_) 
                + ". Failed:" + Integer.toString(fail_);
    }

    private Method[] methods_;
    private Class testingClass_;
    private int pass_;
    private int fail_;
    private String result_;
}

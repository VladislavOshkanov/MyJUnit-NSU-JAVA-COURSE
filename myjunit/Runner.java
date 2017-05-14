/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjunit;
import java.lang.ClassLoader;
import java.lang.Class;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Queue;

/**
 *
 * @author Vladislav Oshkanov
 */
public class Runner {
    public static void main (String [] args){
        loadClasses(args);
        System.out.println(classesForTesting.length);
        
        Tester t1 = new Tester(classesForTesting[0]);
        t1.run();
        
    }
    
    
    
    
    private static void loadClasses (String [] classNames){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        classesForTesting = new Class[classNames.length];
        for (int i = 0; i < classNames.length; ++i)
        {
            try 
            {
                classesForTesting[i] = classLoader.loadClass (classNames[i]);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }
    private static Class[] classesForTesting;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjunit;
import java.util.LinkedList;

/**
 *
 * @author Vladislav Oshkanov
 */
public class Runner {
    private static LinkedList<Class> classesForTesting;
    private static LinkedList<LinkedList<String>> testingResults;
    
    public static void main (String [] args){
        loadClasses(args, args.length - 1);
        testingResults = new LinkedList();
              
        int numberOfThreads = Integer.parseInt(args[args.length - 1]);
        for (int i = 0; i < numberOfThreads; i++){
            new Tester(classesForTesting, testingResults).start();
        }
        
        System.out.println (classesForTesting.isEmpty());
        System.out.println (testingResults.size());
    }
    
    
    private static void loadClasses (String [] args, int size){
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        classesForTesting = new LinkedList();
        for (int i = 0; i < size; ++i)
        {
            try {
                classesForTesting.add(classLoader.loadClass (args[i]));
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    } 
    
}

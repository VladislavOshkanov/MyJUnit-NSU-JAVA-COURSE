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
/**
 * My implementation of java unit test system.
 * This class have the same to JUnit functionality.
 * It loads classes specified in command line options and implements
 * it's methods with Before, Test and After annotations, catches thrown 
 * exceptions and prints results after it. 
 * 
 * Command line example: java -cp "MyJUnit.jar;ClassForTesting1.jar"
 *  myjunit.Runner classfortesting1.ClassForTesting1 class2.Class2 5
 * where 5 is number of threads, in which each Tester class runs.
 * 
 * Packages annotations and assertions of current package should be
 * imported in your class.
 * 
 */
public class Runner {
    private static LinkedList<Class> classesForTesting;
    private static LinkedList<LinkedList<String>> testingResults;
    
    public static void main (String [] args){
        loadClasses(args, args.length - 1);
        int numberOfClasses = classesForTesting.size();
        testingResults = new LinkedList();
              
        int numberOfThreads = Integer.parseInt(args[args.length - 1]);
        for (int i = 0; i < numberOfThreads; i++){
            new Tester(classesForTesting, testingResults).start();
        }
        while (testingResults.size() != numberOfClasses){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        printResults();
        
    }
    private static void printResults(){
        while (!testingResults.isEmpty()){
            System.out.println();
            LinkedList <String> res = testingResults.removeFirst();
            while (!res.isEmpty()){
                System.out.println (res.removeFirst());
            }
        }
        
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classfortesting1;

import assertions.Exception2;
import annotations.Test;
import annotations.Before;
import annotations.After;
import assertions.Assertions;
import assertions.Exception1;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Lenovo
 */
public class ClassForTesting1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception1 {
        throw new Exception1();
    }
    private void randomDelay (){
        Random random = new Random();
        int d = random.nextInt(500);
        try {
            Thread.sleep(d);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClassForTesting1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Before
    public void before () {
        
        
        
        System.out.println("i am before");
    }
    @Test ()
    public void doSomething () {
        randomDelay();
    }
    
    @Test ()
    public void doSomething2 () throws Exception1 {
        randomDelay();
        throw new Exception1();
    }
    
    @Test (expected = Exception1.class)
    public void doSomething3() throws Exception1 {
        randomDelay();
        throw new Exception1();
    }
    
    @Test (expected = Exception1.class)
    public void printSomething () throws Exception {
        randomDelay();
        Assertions.assertTrue(false);
    }

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assertions;

/**
 *
 * @author Lenovo
 */
 public class TestAssertionError extends Exception
    {
        public TestAssertionError() {}
    
        public TestAssertionError(String message)
        {
            super (message);
        }
    }
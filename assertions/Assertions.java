/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assertions;

/**
 *
 * @author Vladislav Oshkanov
 */
public class Assertions extends Exception
{

    
    public static void assertTrue(boolean cond) throws Exception
    {
        if (cond == false)
        {
            throw new TestAssertionError("test assertion");
        }
    }
    public static class TestAssertionError extends Exception
    {
        public TestAssertionError() {}
    
        public TestAssertionError(String message)
        {
            super (message);
        }
    }
}

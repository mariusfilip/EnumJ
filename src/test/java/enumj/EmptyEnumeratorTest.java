/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.NoSuchElementException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marius Filip
 */
public class EmptyEnumeratorTest {
    
    public EmptyEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        enumerator = new EmptyEnumerator();
    }
    EmptyEnumerator enumerator;
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class EmptyEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        assertTrue(!enumerator.hasNext());
    }

    /**
     * Test of nextValue method, of class EmptyEnumerator.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextValue() {
        System.out.println("nextValue");
        assertNull(enumerator.nextValue());
        enumerator.next();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Iterator;
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
public class ConcatEnumeratorTest {
    
    public ConcatEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        iter1 = Arrays.asList(strings1).iterator();
        iter2 = Arrays.asList(strings2).iterator();
        enumerator = new ConcatEnumerator(iter1, iter2);
    }
    String[] strings1 = { "a", "b" };
    String[] strings2 = { "c", "d"};
    String[] strings = { "a", "b", "c", "d" };
    Iterator<String> iter1;
    Iterator<String> iter2;
    ConcatEnumerator<String> enumerator;
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class ConcatEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        for(String s : strings1) {
            assertTrue(enumerator.hasNext());
        }
        for(String s : strings2) {
            assertTrue(enumerator.hasNext());
        }
    }

    /**
     * Test of nextValue method, of class ConcatEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        for(String s : strings) {
            assertEquals(enumerator.nextValue(), s);
        }
    }
}

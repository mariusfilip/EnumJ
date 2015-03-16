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
public class FlatteningEnumeratorTest {
    
    public FlatteningEnumeratorTest() {
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
        iter3 = Arrays.asList(strings3).iterator();
        iterIter = Arrays.asList(iter1, iter2, iter3).iterator();
        enumerator = new FlatteningEnumerator<>(iterIter);
    }
    String[] strings1 = { "a", "b", "c" };
    String[] strings2 = { "d", "e", "f" };
    String[] strings3 = { "g", "h", "i" };
    String[] strings =  { "a", "b", "c",
                          "d", "e", "f",
                          "g", "h", "i" };
    Iterator<String> iter1;
    Iterator<String> iter2;
    Iterator<String> iter3;
    Iterator<Iterator<String>> iterIter;
    FlatteningEnumerator<String> enumerator;

    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class FlatteningEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        for(String s : strings) {
            assertTrue(enumerator.hasNext());
            enumerator.next();
        }
        assertTrue(!enumerator.hasNext());
    }

    /**
     * Test of internalNext method, of class FlatteningEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        for(String s : strings) {
            assertEquals(enumerator.next(), s);
        }
    }
}

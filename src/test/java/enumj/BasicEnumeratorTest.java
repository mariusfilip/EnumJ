/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
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
public class BasicEnumeratorTest {
    
    public BasicEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        iterator = Arrays.asList(strings).iterator();
        enumerator = new BasicEnumerator(iterator);
    }
    String[] strings = { "a", "b", "c" };
    Iterator<String> iterator;
    BasicEnumerator<String> enumerator;
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class BasicEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        assertTrue(enumerator.hasNext());
    }

    /**
     * Test of nextValue method, of class BasicEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        assertEquals(enumerator.nextValue(), strings[0]);
        assertEquals(enumerator.nextValue(), strings[1]);
        assertEquals(enumerator.nextValue(), strings[2]);
    }
}

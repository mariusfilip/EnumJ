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
public class MapEnumeratorTest {
    
    public MapEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        iterator = Arrays.asList(nums).iterator();
    }
    Integer[] nums = { 0, 1, 2, 3, 4 };
    Iterator<Integer> iterator;
    MapEnumerator<Integer, Integer> enumerator;
    
    @After
    public void tearDown() {
    }

    /**
     * Test of hasNext method, of class MapEnumerator.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        enumerator = new MapEnumerator<>(iterator, i -> i*2);
        for(int j : nums) {
            assertTrue(enumerator.hasNext());
            enumerator.next();
        }
        assertTrue(!enumerator.hasNext());
    }

    /**
     * Test of nextValue method, of class MapEnumerator.
     */
    @Test
    public void testNextValue() {
        System.out.println("nextValue");
        enumerator = new MapEnumerator<>(iterator, i -> i*2);
        for(int j : nums) {
            assertEquals(enumerator.next(), Integer.valueOf(j*2));
        }
    }
}

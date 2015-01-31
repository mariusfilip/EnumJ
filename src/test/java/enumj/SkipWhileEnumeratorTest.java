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
public class SkipWhileEnumeratorTest {
    
    public SkipWhileEnumeratorTest() {
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
        enumerator = new SkipWhileEnumerator<>(iterator, i -> i<5);
    }
    Integer[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Iterator<Integer> iterator;
    SkipWhileEnumerator<Integer> enumerator;

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        // TODO review the generated test code and remove the default call to fail.
        for(int i=5; i<=10; ++i) {
            assertTrue(enumerator.hasNext());
            assertEquals(enumerator.next(), Integer.valueOf(i));
        }
    }
}

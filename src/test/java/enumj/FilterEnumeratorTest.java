/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.ArrayList;
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
public class FilterEnumeratorTest {
    
    public FilterEnumeratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        List list = new ArrayList();
        for(int i : values) {
            list.add(i);
        }
        iterator = list.iterator();
    }
    int[] values = { 0, 1, 2, 3, 4, 5, 6 };
    Iterator<Integer> iterator;
    FilterEnumerator<Integer> enumerator;
    
    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        enumerator = new FilterEnumerator<>(iterator, i -> 0 == i%2);
        int[] evens = { 0, 2, 4, 6 };
        for(int j : evens) {
            assertEquals(enumerator.next(), Integer.valueOf(j));
        }
    }    
}

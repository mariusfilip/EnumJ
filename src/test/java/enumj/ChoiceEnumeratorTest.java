/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.Iterator;
import java.util.stream.IntStream;
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
public class ChoiceEnumeratorTest {

    public ChoiceEnumeratorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        shortIterator = IntStream.range(0, shortLen).iterator();
        longIterator = IntStream.range(0, longLen).iterator();
        veryLongIterator = IntStream.range(0, veryLongLen).iterator();
        zipped = new ChoiceEnumerator(this::indexSupply,
                                      shortIterator,
                                      longIterator,
                                      veryLongIterator);
    }
    int shortLen = 3;
    int longLen = 5;
    int veryLongLen = 7;
    Iterator<Integer> shortIterator;
    Iterator<Integer> longIterator;
    Iterator<Integer> veryLongIterator;
    ChoiceEnumerator<Integer> zipped;

    int index = 0;
    int indexSupply() {
        int crt = index;
        index = (index+1)%3;
        return crt;
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testNext() {
        for(int i=0; i<shortLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertEquals(val, Integer.valueOf(i));
        }
        for(int i=shortLen; i<longLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertTrue(val >= shortLen);
            assertTrue(zipped.hasNext());
            val = zipped.next();
            assertTrue(val >= shortLen);
        }
        for(int i=longLen; i<veryLongLen; ++i) {
            assertTrue(zipped.hasNext());
            Integer val = zipped.next();
            assertTrue(val >= longLen);
        }
        assertTrue(!zipped.hasNext());
        assertTrue(!zipped.hasNext());
    }    
}

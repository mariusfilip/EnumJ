/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumj;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.mutable.Mutable;
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
public class FilterValueRetrieverTest {
    
    public FilterValueRetrieverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        strings.add("a");
    }
    List<String> strings = new ArrayList<>();
    FilterValueRetriever<String> retriever;

    @After
    public void tearDown() {
    }

    /**
     * Test of getValue method, of class FilterValueRetriever.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        retriever = new FilterValueRetriever(strings.iterator(), e -> false);
        assertTrue(!retriever.get().isPresent());
        retriever = new FilterValueRetriever(strings.iterator(), e -> true);
        Optional<Mutable<String>> e = retriever.get();
        assertTrue(e.isPresent());
        assertEquals(e.get().getValue(), "a");
    }
    
}

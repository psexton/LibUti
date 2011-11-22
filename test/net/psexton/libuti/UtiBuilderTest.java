/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author PSexton
 */
public class UtiBuilderTest {
    
    public UtiBuilderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of fromFile method, of class UtiBuilder.
     */
    @Test
    public void testFromFile() {
        System.out.println("fromFile");
        File file = null;
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = null;
        Uti result = instance.fromFile(file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromSuffix method, of class UtiBuilder.
     */
    @Test
    public void testFromSuffix() {
        System.out.println("fromSuffix");
        String fileSuffix = "";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = null;
        Uti result = instance.fromSuffix(fileSuffix);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fromString method, of class UtiBuilder.
     */
    @Test
    public void testFromString() {
        System.out.println("fromString");
        String utiName = "";
        UtiBuilder instance = new UtiBuilder();
        Uti expResult = null;
        Uti result = instance.fromString(utiName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}

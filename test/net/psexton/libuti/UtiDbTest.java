/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author psexton
 */
public class UtiDbTest {
    private UtiDb instance;
    
    @Before
    public void setUp() {
        // Initialize instance with the default set of XML files
        instance = UtiDb.getInstance();
    }
    
    @After
    public void tearDown() {
        
    }

    /**
     * Test of importXmlData method, of class UtiDb.
     */
    @Test
    public void testImportXmlData() throws Exception {
        // We're already calling importXmlData in setup
        // Test that without that call, a "docx" suffix lookup returns null,
        // and that with it, it returns non-null.
        String suffix = "docx";
        assertNotNull(instance.utiForSuffix(suffix));
        instance = UtiDb.getCleanInstance(); // no calls to importXmlData
        assertNull(instance.utiForSuffix(suffix));
    }

    /**
     * Test of utiForSuffix method, of class UtiDb.
     */
    @Test
    public void testUtiForSuffix() {
        // Test that an extension that should be there maps properly
        String suffix = "xls";
        String expResult = "com.microsoft.excel.xls";
        String result = instance.utiForSuffix(suffix);
        assertEquals(expResult, result);
        
        // Test that a nonsense extension maps to null
        suffix = "totally-not-an-actual-file-suffix";
        expResult = null;
        result = instance.utiForSuffix(suffix);
        assertEquals(expResult, result);
    }

    /**
     * Test of isUtiInDb method, of class UtiDb.
     */
    @Test
    public void testIsUtiInDb() {
        // Test that a UTI that should be there is
        String utiName = "com.microsoft.powerpoint.pptx";
        Boolean expResult = true;
        Boolean result = instance.isUtiInDb(utiName);
        assertEquals(expResult, result);
        
        // Test that a UTI that shouldn't be there isn't
        utiName = "totally.not.an.actual.uti";
        expResult = false;
        result = instance.isUtiInDb(utiName);
        assertEquals(expResult, result);
        
        // Test that a UTI that doesn't have any file suffixes but should still be there is
        utiName = "public.data";
        expResult = true;
        result = instance.isUtiInDb(utiName);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of conformsTo method, of class UtiDb.
     */
    @Test
    public void testConformsTo() {
        // msWord and msExcel should conform to publicData
        // msWord should not conform to msExcel
        String msWord = "com.microsoft.word.doc";
        String msExcel = "com.microsoft.excel.xls";
        String publicData = "public.data";
        
        assertTrue(instance.conformsTo(msWord, publicData));
        assertTrue(instance.conformsTo(msExcel, publicData));
        assertFalse(instance.conformsTo(msWord, msExcel));
    }
}

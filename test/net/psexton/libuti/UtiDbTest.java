/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.util.HashSet;
import java.util.Set;
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
     * @throws java.lang.Exception
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
    
    /**
     * Test that a UTI with multiple suffixes is initialized properly
     */
    @Test
    public void testParseMultipleSuffixes() {
        String suffix = "wav";
        String expResult = "com.microsoft.waveform-audio";
        String result = instance.utiForSuffix(suffix);
        assertEquals(expResult, result);
        suffix = "wave";
        result = instance.utiForSuffix(suffix);
        assertEquals(expResult, result);
    }
    
    /**
     * Test that a UTI with multiple conformances is initialized properly
     */
    @Test
    public void testParseMultipleParents() {
        // msPowerpoint should conform to both publicData and publicPresentation
        String msPowerpoint = "com.microsoft.powerpoint.ppt";
        String publicData = "public.data";
        String publicPresentation = "public.presentation";
        
        assertTrue(instance.conformsTo(msPowerpoint, publicData));
        assertTrue(instance.conformsTo(msPowerpoint, publicPresentation));
    }
    
    @Test
    public void testReverseMappingSingle() {
        // UTI with a single suffix that maps to it
        String uti = "public.tar-archive";
        String expResult = "tar";
        String result = instance.preferredSuffixForUti(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testReverseMappingMultiple() {
        // UTI with a multiple suffixs that maps to it
        String uti = "public.jpeg";
        String expResult = "jpeg";
        String result = instance.preferredSuffixForUti(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testReverseMappingNull() {
        // UTI with no suffixs that maps to it
        String uti = "public.audio";
        String expResult = null;
        String result = instance.preferredSuffixForUti(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformancesForRoot() {
        String uti = "public.item";
        Set<String> expResult = new HashSet<String>();
        expResult.add(uti);
        Set<String> result = instance.conformancesFor(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformancesForLeaf() {
        String uti = "public.png";
        Set<String> expResult = new HashSet<String>();
        // self
        expResult.add(uti);
        // physical hierarchy
        expResult.add("public.image");
        expResult.add("public.data");
        expResult.add("public.item");
        // document hierarchy
        expResult.add("public.content");
        Set<String> result = instance.conformancesFor(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformersForMid() {
        String uti = "public.image";
        Set<String> expResult = new HashSet<String>();
        // self
        expResult.add(uti);
        // physical hierarchy
        expResult.add("public.png");
        expResult.add("public.jpeg");
        expResult.add("public.tiff");
        expResult.add("com.compuserve.gif");
        expResult.add("com.microsoft.bmp");
        Set<String> result = instance.conformersFor(uti);
        assertEquals(expResult, result);
    }
    
    @Test
    public void conformersForLeaf() {
        String uti = "public.png";
        Set<String> expResult = new HashSet<String>();
        // self
        expResult.add(uti);
        Set<String> result = instance.conformersFor(uti);
        assertEquals(expResult, result);
    }
}

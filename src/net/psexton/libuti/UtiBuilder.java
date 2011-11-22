/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder for Uniform Type Identifiers
 * @author PSexton
 */
public class UtiBuilder {
    private static final Logger logger = Logger.getLogger("net.psexton.libuti");
    private Map<String, String> extensionTable;
    private final Uti PUBLIC_DATA;
    private final Uti PUBLIC_FOLDER;
    
    public UtiBuilder() {
        // Initialize base types
        PUBLIC_DATA = new Uti("public.data");
        PUBLIC_FOLDER = new Uti("public.folder");
        
        // Initialize lookup table - maps extensions to UTIs
        extensionTable = new HashMap<String, String>();
        // Microsoft Office
        extensionTable.put("pdf", "com.adobe.pdf");
        extensionTable.put("doc", "com.microsoft.word.doc");
        extensionTable.put("docx", "com.microsoft.word.docx");
        extensionTable.put("xls", "com.microsoft.excel.xls");
        extensionTable.put("xlsx", "com.microsoft.excel.xlsx");
        extensionTable.put("ppt", "com.microsoft.powerpoint.ppt");
        extensionTable.put("pptx", "com.microsoft.powerpoint.pptx");
        
    }
    
    /**
     * Given a file, tries to determine the correct UTI.
     * @param file File to find UTI for
     * @return Instance of UTI (public.data if a match can not be found)
     */
    public Uti fromFile(final File file) {
        if(file.isDirectory()) {
            return PUBLIC_FOLDER;
        }
        else if(file.isFile()) {
            String filename = file.getName();
            String[] nameParts = filename.split("\\.");
            if(nameParts.length < 2) { // filename does not end with a suffix
                logger.log(Level.WARNING, "No file suffix found in filename \"{0}\", using public.data", filename);
                return PUBLIC_DATA;
            }
            else {
                final String suffix = nameParts[nameParts.length - 1];
                return fromSuffix(suffix);
            }
        }
        else {
            //@TODO log this & return null instead of throwing exception
            throw new AssertionError(file + " is not a directory and not a file");
        }
    }
    
    /**
     * Given a file suffix, tries to determine the correct UTI.
     * @param file File to find UTI for
     * @return Instance of UTI, or opaque dynamic UTI if a match can not be found
     */
    public Uti fromSuffix(final String fileSuffix) {
        if(extensionTable.containsKey(fileSuffix)) {
            return new Uti(extensionTable.get(fileSuffix));
        }
        logger.log(Level.WARNING, "Unknown file suffix \"{0}\", using dynamic UTI", fileSuffix);
        return new Uti("dyn." + fileSuffix);
    }
    
    /**
     * Converts the "string form" returned by UTI.toString back into an
     * instance of UTI
     * @param utiName Name of UTI
     * @return Instance of UTI, or null if a match can not be found
     */
    public Uti fromString(final String utiName) {
        if(extensionTable.containsValue(utiName)) {
            return new Uti(utiName);
        }
        logger.log(Level.WARNING, "No matching UTI found for name \"{0}\", returning null", utiName);
        return null;
    }
}

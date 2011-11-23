/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * Builder for Uniform Type Identifiers
 * @author PSexton
 */
public class UtiBuilder {
    private static final Logger logger = Logger.getLogger("net.psexton.libuti");
    private UtiDb utiDb;
    private final Uti PUBLIC_DATA;
    private final Uti PUBLIC_FOLDER;
    
    public UtiBuilder() {
        // Initialize base types
        PUBLIC_DATA = new Uti("public.data");
        PUBLIC_FOLDER = new Uti("public.folder");
        
        // Initialize lookup table - maps extensions to UTIs
        utiDb = new UtiDb();
        try {
            utiDb.importXmlData(this.getClass().getResourceAsStream("/net/psexton/libuti/data/RootsAndBases.xml"));
            utiDb.importXmlData(this.getClass().getResourceAsStream("/net/psexton/libuti/data/MicrosoftOffice.xml"));
        } 
        catch (IOException ex) {
            Logger.getLogger(UtiBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (JDOMException ex) {
            Logger.getLogger(UtiBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String utiName = utiDb.utiForSuffix(fileSuffix);
        if(utiName != null) {
            return new Uti(utiName);
        }
        else {
            logger.log(Level.WARNING, "Unknown file suffix \"{0}\", using dynamic UTI", fileSuffix);
            return new Uti("dyn." + fileSuffix);
        }
    }
    
    /**
     * Converts the "string form" returned by UTI.toString back into an
     * instance of UTI
     * @param utiName Name of UTI
     * @return Instance of UTI, or null if a match can not be found
     */
    public Uti fromString(final String utiName) {
        if(utiDb.isUtiInDb(utiName)) {
            return new Uti(utiName);
        }
        else {
            logger.log(Level.WARNING, "No matching UTI found for name \"{0}\", returning null", utiName);
            return null;
        }
    }
}

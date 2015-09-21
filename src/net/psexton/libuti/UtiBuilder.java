/*
 * This file is part of LibUti.
 * 
 * LibUti is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LibUti is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with LibUti. If not, see <http://www.gnu.org/licenses/>.
 */
package net.psexton.libuti;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Builder for Uniform Type Identifiers
 * @author PSexton
 */
public class UtiBuilder {
    private static final Logger logger = Logger.getLogger("net.psexton.libuti");
    private final UtiDb utiDb;
    private final Uti PUBLIC_DATA;
    private final Uti PUBLIC_FOLDER;
    
    /**
     * An uninteresting standard constructor.
     * Unknown file types will be mapped to "public.data".
     * Folders/directories will be mapped to "public.folder".
     */
    public UtiBuilder() {
        // Initialize base types
        PUBLIC_DATA = new Uti("public.data");
        PUBLIC_FOLDER = new Uti("public.folder");
        
        // Initialize lookup table - maps extensions to UTIs
        utiDb = UtiDb.getInstance();
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
     * @param fileSuffix File extension to find UTI for
     * @return Instance of UTI, or opaque dynamic UTI if a match can not be found
     */
    public Uti fromSuffix(final String fileSuffix) {
        // Normalize fileSuffix to lowercase before making DB call
        String utiName = utiDb.utiForSuffix(fileSuffix.toLowerCase());
        if(utiName != null) {
            return new Uti(utiName);
        }
        else {
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
            return null;
        }
    }
}

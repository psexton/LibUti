/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

import java.util.HashSet;
import java.util.Set;

/**
 * Uniform Type Identifier
 * @author PSexton
 */
public class Uti implements Comparable<Uti> {
    private final String name; // e.g. "public.plain-text"
    
    /**
     * Uti constructor.
     * This should not be called directly. Use UtiBuilder instead.
     * @param name String form of the UTI.
     */
    protected Uti(final String name) {
        if(name == null)
            throw new NullPointerException("name cannot be null");
        else
            this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Uti other = (Uti) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.name.hashCode();
        return hash;
    }
    
    /**
     * Checks if this UTI conforms to another UTI.
     * @param parent The "parent" the check against
     * @return True if this conforms to parent
     * @since 0.2.0
     */
    public boolean conformsTo(Uti parent) {
        if(parent == null) // cannot conform to null
            return false;
        if(parent.equals(this)) // always conform to self
            return true;
        return UtiDb.getInstance().conformsTo(this.toString(), parent.toString());
    }
    
    /**
     * Returns a file suffix that maps to this UTI.
     * If more than one suffix maps to this UTI, then the one marked as 
     * @return String
     * @since 1.0.0
     */
    public String toSuffix() {
        // A dyn.* should map to the * part
        if(name.startsWith("dyn."))
            return name.substring(4);
        else
            return UtiDb.getInstance().preferredSuffixForUti(name);
    }

    @Override
    public int compareTo(Uti o) {
        return this.name.compareTo(o.name);
    }
    
    /**
     * Return conformances.
     * This is the set of UTIs where `this.conformsTo(other) returns true.
     * In other words, where this is a child of the other UTI.
     * @return Set of UTIs that this UTI conforms to (including itself).
     */
    public Set<Uti> getConformances() {
        Set<Uti> conformances = new HashSet<Uti>();
        for(String strConformance : UtiDb.getInstance().conformancesFor(name)) {
            conformances.add(new Uti(strConformance));
        }
        return conformances;
    }
    
    /**
     * Return conforming types.
     * This is the set of UTIs where `other.conformsTo(this)` returns true.
     * In other words, where this is a parent of the other UTI.
     * @return Set of UTIs that conform to this UTI (including itself).
     */
    public Set<Uti> getConformers() {
        Set<Uti> conformers = new HashSet<Uti>();
        for(String strConformer : UtiDb.getInstance().conformersFor(name)) {
            conformers.add(new Uti(strConformer));
        }
        return conformers;
    }
}

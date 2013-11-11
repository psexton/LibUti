/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.psexton.libuti;

/**
 * Uniform Type Identifier
 * @author PSexton
 */
public class Uti {
    
    protected Uti(final String name) {
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
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
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
    
    private final String name; // e.g. "public.plain-text"
}

package crystallography.core;

import ptpshared.core.math.Quaternion;
import ptpshared.utility.xml.ObjectXml;

/**
 * Crystallographic space group as defined in the International Tables for
 * Crystallography.
 * 
 * @author ppinard
 */
public class SpaceGroup implements ObjectXml {

    /** Crystallographic system. */
    public final CrystalSystem crystalSystem;

    /** Array of generators. */
    private final Generator[] generators;

    /** Index/number of the space group (1 to 203). */
    public final int index;

    /** Laue group. */
    public final LaueGroup laueGroup;

    /** Symbol of the space group. */
    public final String symbol;



    /**
     * Creates a new <code>SpaceGroup</code>.
     * 
     * @param index
     *            index/number of the space group (1 to 203)
     * @param symbol
     *            symbol of the space group
     * @param crystalSystem
     *            crystallographic system
     * @param laueGroup
     *            Laue group
     * @param generators
     *            array of generators
     */
    protected SpaceGroup(int index, String symbol, CrystalSystem crystalSystem,
            LaueGroup laueGroup, Generator[] generators) {
        this.index = index;
        this.symbol = symbol;
        this.crystalSystem = crystalSystem;
        this.laueGroup = laueGroup;
        this.generators = generators.clone();
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        SpaceGroup other = (SpaceGroup) obj;
        if (index != other.index)
            return false;

        return true;
    }



    /**
     * Returns the array of generators associated with the space group.
     * 
     * @return generators
     */
    public Generator[] getGenerators() {
        return generators;
    }



    /**
     * Returns the symmetry operators of the Laue group.
     * 
     * @return symmetry operators
     */
    public Quaternion[] getOperators() {
        return laueGroup.getOperators();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }



    @Override
    public String toString() {
        return symbol;
    }
}

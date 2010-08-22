/*
 * EBSD-Image
 * Copyright (C) 2010 Philippe T. Pinard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package crystallography.core;

import static crystallography.core.Constants.*;
import static crystallography.core.CrystalSystem.MONOCLINIC;
import static crystallography.core.CrystalSystem.ORTHORHOMBIC;
import static crystallography.core.CrystalSystem.TETRAGONAL;
import static crystallography.core.CrystalSystem.TRICLINIC;
import static crystallography.core.LaueGroup.*;

/**
 * First series of space group between 1 and 115. The space groups must be split
 * into two classes due to Java's limitation on the size of public static final
 * variables.
 * 
 * @author Dr. Ethan Merritt <merritt@u.washington.edu> of PyMMLib Development
 *         Group
 */
public interface SpaceGroups1 {
    /** Space group 1. */
    public static final SpaceGroup SG1 = new SpaceGroup(1, "P1", TRICLINIC,
            LG1, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0) });

    /** Space group 2. */
    public static final SpaceGroup SG2 = new SpaceGroup(2, "P-1", TRICLINIC,
            LG1, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0) });

    /** Space group 3. */
    public static final SpaceGroup SG3 = new SpaceGroup(3, "P2", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0) });

    /** Space group 4. */
    public static final SpaceGroup SG4 = new SpaceGroup(4, "P21", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_0) });

    /** Space group 5. */
    public static final SpaceGroup SG5 = new SpaceGroup(5, "C2", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0) });

    /** Space group 6. */
    public static final SpaceGroup SG6 = new SpaceGroup(6, "Pm", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0) });

    /** Space group 7. */
    public static final SpaceGroup SG7 = new SpaceGroup(7, "Pc", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12) });

    /** Space group 8. */
    public static final SpaceGroup SG8 = new SpaceGroup(8, "Cm", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0) });

    /** Space group 9. */
    public static final SpaceGroup SG9 = new SpaceGroup(9, "Cc", MONOCLINIC,
            LG2m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12) });

    /** Space group 10. */
    public static final SpaceGroup SG10 = new SpaceGroup(10, "P2/m",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0) });

    /** Space group 11. */
    public static final SpaceGroup SG11 = new SpaceGroup(11, "P21/m",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_0) });

    /** Space group 12. */
    public static final SpaceGroup SG12 = new SpaceGroup(12, "C2/m",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0) });

    /** Space group 13. */
    public static final SpaceGroup SG13 = new SpaceGroup(13, "P2/c",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12) });

    /** Space group 14. */
    public static final SpaceGroup SG14 = new SpaceGroup(14, "P21/c",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12) });

    /** Space group 15. */
    public static final SpaceGroup SG15 = new SpaceGroup(15, "C2/c",
            MONOCLINIC, LG2m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12) });

    /** Space group 16. */
    public static final SpaceGroup SG16 = new SpaceGroup(16, "P222",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0) });

    /** Space group 17. */
    public static final SpaceGroup SG17 = new SpaceGroup(17, "P2221",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0) });

    /** Space group 18. */
    public static final SpaceGroup SG18 = new SpaceGroup(18, "P21212",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0) });

    /** Space group 19. */
    public static final SpaceGroup SG19 = new SpaceGroup(19, "P212121",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0) });

    /** Space group 20. */
    public static final SpaceGroup SG20 = new SpaceGroup(20, "C2221",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0) });

    /** Space group 21. */
    public static final SpaceGroup SG21 = new SpaceGroup(21, "C222",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0) });

    /** Space group 22. */
    public static final SpaceGroup SG22 = new SpaceGroup(22, "F222",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_12_12),
                    new Generator(ROT_X_Y_Z, TR_12_0_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0) });

    /** Space group 23. */
    public static final SpaceGroup SG23 = new SpaceGroup(23, "I222",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12) });

    /** Space group 24. */
    public static final SpaceGroup SG24 = new SpaceGroup(24, "I212121",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12) });

    /** Space group 25. */
    public static final SpaceGroup SG25 = new SpaceGroup(25, "Pmm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 26. */
    public static final SpaceGroup SG26 = new SpaceGroup(26, "Pmc21",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 27. */
    public static final SpaceGroup SG27 = new SpaceGroup(27, "Pcc2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 28. */
    public static final SpaceGroup SG28 = new SpaceGroup(28, "Pma2",
            ORTHORHOMBIC, LGmmm, new Generator[] {

            new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_0_0) });

    /** Space group 29. */
    public static final SpaceGroup SG29 = new SpaceGroup(29, "Pca21",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12) });

    /** Space group 30. */
    public static final SpaceGroup SG30 = new SpaceGroup(30, "Pnc2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12) });

    /** Space group 31. */
    public static final SpaceGroup SG31 = new SpaceGroup(31, "Pmn21",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 32. */
    public static final SpaceGroup SG32 = new SpaceGroup(32, "Pba2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 33. */
    public static final SpaceGroup SG33 = new SpaceGroup(33, "Pna21",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 34. */
    public static final SpaceGroup SG34 = new SpaceGroup(34, "Pnn2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 35. */
    public static final SpaceGroup SG35 = new SpaceGroup(35, "Cmm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 36. */
    public static final SpaceGroup SG36 = new SpaceGroup(36, "Cmc21",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 37. */
    public static final SpaceGroup SG37 = new SpaceGroup(37, "Ccc2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 38. */
    public static final SpaceGroup SG38 = new SpaceGroup(38, "Amm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12) });

    /** Space group 39. */
    public static final SpaceGroup SG39 = new SpaceGroup(39, "Abm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_Z, TR_0_12_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 40. */
    public static final SpaceGroup SG40 = new SpaceGroup(40, "Ama2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_0_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 41. */
    public static final SpaceGroup SG41 = new SpaceGroup(41, "Aba2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12) });

    /** Space group 42. */
    public static final SpaceGroup SG42 = new SpaceGroup(42, "Fmm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12),
                    new Generator(ROT_X_Y_Z, TR_12_0_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 43. */
    public static final SpaceGroup SG43 = new SpaceGroup(43, "Fdd2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_14_14_14),
                    new Generator(ROT_mX_Y_Z, TR_14_14_14),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_14_34_34),
                    new Generator(ROT_mX_Y_Z, TR_14_34_34),
                    new Generator(ROT_X_Y_Z, TR_12_0_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_34_14_34),
                    new Generator(ROT_mX_Y_Z, TR_34_14_34),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_34_34_14),
                    new Generator(ROT_mX_Y_Z, TR_34_34_14) });

    /** Space group 44. */
    public static final SpaceGroup SG44 = new SpaceGroup(44, "Imm2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 45. */
    public static final SpaceGroup SG45 = new SpaceGroup(45, "Iba2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 46. */
    public static final SpaceGroup SG46 = new SpaceGroup(46, "Ima2",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12) });

    /** Space group 47. */
    public static final SpaceGroup SG47 = new SpaceGroup(47, "Pmmm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 48. */
    public static final SpaceGroup SG48 = new SpaceGroup(48, "Pnnn",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 49. */
    public static final SpaceGroup SG49 = new SpaceGroup(49, "Pccm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 50. */
    public static final SpaceGroup SG50 = new SpaceGroup(50, "Pban",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 51. */
    public static final SpaceGroup SG51 = new SpaceGroup(51, "Pmma",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_12_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_0_0) });

    /** Space group 52. */
    public static final SpaceGroup SG52 = new SpaceGroup(52, "Pnna",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12) });

    /** Space group 53. */
    public static final SpaceGroup SG53 = new SpaceGroup(53, "Pmna",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 54. */
    public static final SpaceGroup SG54 = new SpaceGroup(54, "Pcca",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12) });

    /** Space group 55. */
    public static final SpaceGroup SG55 = new SpaceGroup(55, "Pbam",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 56. */
    public static final SpaceGroup SG56 = new SpaceGroup(56, "Pccn",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12) });

    /** Space group 57. */
    public static final SpaceGroup SG57 = new SpaceGroup(57, "Pbcm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_0) });

    /** Space group 58. */
    public static final SpaceGroup SG58 = new SpaceGroup(58, "Pnnm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 59. */
    public static final SpaceGroup SG59 = new SpaceGroup(59, "Pmmn",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0) });

    /** Space group 60. */
    public static final SpaceGroup SG60 = new SpaceGroup(60, "Pbcn",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 61. */
    public static final SpaceGroup SG61 = new SpaceGroup(61, "Pbca",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 62. */
    public static final SpaceGroup SG62 = new SpaceGroup(62, "Pnma",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 63. */
    public static final SpaceGroup SG63 = new SpaceGroup(63, "Cmcm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 64. */
    public static final SpaceGroup SG64 = new SpaceGroup(64, "Cmca",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 65. */
    public static final SpaceGroup SG65 = new SpaceGroup(65, "Cmmm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 66. */
    public static final SpaceGroup SG66 = new SpaceGroup(66, "Cccm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 67. */
    public static final SpaceGroup SG67 = new SpaceGroup(67, "Cmma",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 68. */
    public static final SpaceGroup SG68 = new SpaceGroup(68, "Ccca",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_0_12),
                    new Generator(ROT_X_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12) });

    /** Space group 69. */
    public static final SpaceGroup SG69 = new SpaceGroup(69, "Fmmm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_12_12),
                    new Generator(ROT_X_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_12_12),
                    new Generator(ROT_X_Y_Z, TR_12_0_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_0_12),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0) });

    /** Space group 70. */
    public static final SpaceGroup SG70 = new SpaceGroup(70, "Fddd",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_14_14_14),
                    new Generator(ROT_X_Y_mZ, TR_14_14_14),
                    new Generator(ROT_X_mY_Z, TR_14_14_14),
                    new Generator(ROT_mX_Y_Z, TR_14_14_14),
                    new Generator(ROT_X_Y_Z, TR_0_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_0_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_14_34_34),
                    new Generator(ROT_X_Y_mZ, TR_14_34_34),
                    new Generator(ROT_X_mY_Z, TR_14_34_34),
                    new Generator(ROT_mX_Y_Z, TR_14_34_34),
                    new Generator(ROT_X_Y_Z, TR_12_0_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_34_14_34),
                    new Generator(ROT_X_Y_mZ, TR_34_14_34),
                    new Generator(ROT_X_mY_Z, TR_34_14_34),
                    new Generator(ROT_mX_Y_Z, TR_34_14_34),
                    new Generator(ROT_X_Y_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_34_34_14),
                    new Generator(ROT_X_Y_mZ, TR_34_34_14),
                    new Generator(ROT_X_mY_Z, TR_34_34_14),
                    new Generator(ROT_mX_Y_Z, TR_34_34_14) });

    /** Space group 71. */
    public static final SpaceGroup SG71 = new SpaceGroup(71, "Immm",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 72. */
    public static final SpaceGroup SG72 = new SpaceGroup(72, "Ibam",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 73. */
    public static final SpaceGroup SG73 = new SpaceGroup(73, "Ibca",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_Z, TR_12_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12) });

    /** Space group 74. */
    public static final SpaceGroup SG74 = new SpaceGroup(74, "Imma",
            ORTHORHOMBIC, LGmmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_12_0),
                    new Generator(ROT_X_mY_Z, TR_0_12_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12) });

    /** Space group 75. */
    public static final SpaceGroup SG75 = new SpaceGroup(75, "P4", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0) });

    /** Space group 76. */
    public static final SpaceGroup SG76 = new SpaceGroup(76, "P41", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_0_0_14),
                    new Generator(ROT_Y_mX_Z, TR_0_0_34) });

    /** Space group 77. */
    public static final SpaceGroup SG77 = new SpaceGroup(77, "P42", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12) });

    /** Space group 78. */
    public static final SpaceGroup SG78 = new SpaceGroup(78, "P43", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_0_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_0_14) });

    /** Space group 79. */
    public static final SpaceGroup SG79 = new SpaceGroup(79, "I4", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12) });

    /** Space group 8. */
    public static final SpaceGroup SG80 = new SpaceGroup(80, "I41", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_0_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_0_34),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_12_14) });

    /** Space group 81. */
    public static final SpaceGroup SG81 = new SpaceGroup(81, "P-4", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0) });

    /** Space group 82. */
    public static final SpaceGroup SG82 = new SpaceGroup(82, "I-4", TETRAGONAL,
            LG4m, new Generator[] { new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_mZ, TR_12_12_12),
                    new Generator(ROT_mY_X_mZ, TR_12_12_12) });

    /** Space group 83. */
    public static final SpaceGroup SG83 = new SpaceGroup(83, "P4/m",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0) });

    /** Space group 84. */
    public static final SpaceGroup SG84 = new SpaceGroup(84, "P42/m",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_12),
                    new Generator(ROT_mY_X_mZ, TR_0_0_12) });

    /** Space group 85. */
    public static final SpaceGroup SG85 = new SpaceGroup(85, "P4/n",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_12_0),
                    new Generator(ROT_Y_mX_Z, TR_12_12_0),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_0),
                    new Generator(ROT_X_Y_mZ, TR_12_12_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0) });

    /** Space group 86. */
    public static final SpaceGroup SG86 = new SpaceGroup(86, "P42/n",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0) });

    /** Space group 87. */
    public static final SpaceGroup SG87 = new SpaceGroup(87, "I4/m",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_mZ, TR_12_12_12),
                    new Generator(ROT_Y_mX_mZ, TR_12_12_12),
                    new Generator(ROT_mY_X_mZ, TR_12_12_12) });

    /** Space group 88. */
    public static final SpaceGroup SG88 = new SpaceGroup(88, "I41/a",
            TETRAGONAL, LG4m, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_0_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_0_34),
                    new Generator(ROT_mX_mY_mZ, TR_0_12_14),
                    new Generator(ROT_X_Y_mZ, TR_12_0_34),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_12_12_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_12_14),
                    new Generator(ROT_mX_mY_mZ, TR_12_0_34),
                    new Generator(ROT_X_Y_mZ, TR_0_12_14),
                    new Generator(ROT_Y_mX_mZ, TR_12_12_12),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0) });

    /** Space group 89. */
    public static final SpaceGroup SG89 = new SpaceGroup(89, "P422",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0) });

    /** Space group 90. */
    public static final SpaceGroup SG90 = new SpaceGroup(90, "P4212",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_12_0),
                    new Generator(ROT_Y_mX_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0) });

    /** Space group 91. */
    public static final SpaceGroup SG91 = new SpaceGroup(91, "P4122",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_0_0_14),
                    new Generator(ROT_Y_mX_Z, TR_0_0_34),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_Y_X_mZ, TR_0_0_34),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_14) });

    /** Space group 92. */
    public static final SpaceGroup SG92 = new SpaceGroup(92, "P41212",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_12_34),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_14),
                    new Generator(ROT_X_mY_mZ, TR_12_12_34),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_12) });

    /** Space group 93. */
    public static final SpaceGroup SG93 = new SpaceGroup(93, "P4222",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_Y_X_mZ, TR_0_0_12),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_12) });

    /** Space group 94. */
    public static final SpaceGroup SG94 = new SpaceGroup(94, "P42212",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0) });

    /** Space group 95. */
    public static final SpaceGroup SG95 = new SpaceGroup(95, "P4322",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_0_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_0_14),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_Y_X_mZ, TR_0_0_14),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_34) });

    /** Space group 96. */
    public static final SpaceGroup SG96 = new SpaceGroup(96, "P43212",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_34),
                    new Generator(ROT_Y_mX_Z, TR_12_12_14),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_34),
                    new Generator(ROT_X_mY_mZ, TR_12_12_14),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_12) });

    /** Space group 97. */
    public static final SpaceGroup SG97 = new SpaceGroup(97, "I422",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_Y_X_mZ, TR_12_12_12),
                    new Generator(ROT_mY_mX_mZ, TR_12_12_12) });

    /** Space group 98. */
    public static final SpaceGroup SG98 = new SpaceGroup(98, "I4122",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_0_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_0_34),
                    new Generator(ROT_mX_Y_mZ, TR_12_0_34),
                    new Generator(ROT_X_mY_mZ, TR_0_12_14),
                    new Generator(ROT_Y_X_mZ, TR_12_12_12),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_12_14),
                    new Generator(ROT_mX_Y_mZ, TR_0_12_14),
                    new Generator(ROT_X_mY_mZ, TR_12_0_34),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_12_12_12) });

    /** Space group 99. */
    public static final SpaceGroup SG99 = new SpaceGroup(99, "P4mm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_mY_mX_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_Z, TR_0_0_0) });

    /** Space group 100. */
    public static final SpaceGroup SG100 = new SpaceGroup(100, "P4bm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_mY_mX_Z, TR_12_12_0),
                    new Generator(ROT_Y_X_Z, TR_12_12_0) });

    /** Space group 101. */
    public static final SpaceGroup SG101 = new SpaceGroup(101, "P42cm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_mY_mX_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_Z, TR_0_0_0) });

    /** Space group 102. */
    public static final SpaceGroup SG102 = new SpaceGroup(102, "P42nm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12),
                    new Generator(ROT_mY_mX_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_Z, TR_0_0_0) });

    /** Space group 103. */
    public static final SpaceGroup SG103 = new SpaceGroup(103, "P4cc",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_mY_mX_Z, TR_0_0_12),
                    new Generator(ROT_Y_X_Z, TR_0_0_12) });

    /** Space group 104. */
    public static final SpaceGroup SG104 = new SpaceGroup(104, "P4nc",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12),
                    new Generator(ROT_mY_mX_Z, TR_12_12_12),
                    new Generator(ROT_Y_X_Z, TR_12_12_12) });

    /** Space group 105. */
    public static final SpaceGroup SG105 = new SpaceGroup(105, "P42mc",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_mY_mX_Z, TR_0_0_12),
                    new Generator(ROT_Y_X_Z, TR_0_0_12) });

    /** Space group 106. */
    public static final SpaceGroup SG106 = new SpaceGroup(106, "P42bc",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_12),
                    new Generator(ROT_Y_mX_Z, TR_0_0_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_mY_mX_Z, TR_12_12_12),
                    new Generator(ROT_Y_X_Z, TR_12_12_12) });

    /** Space group 107. */
    public static final SpaceGroup SG107 = new SpaceGroup(107, "I4mm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_mY_mX_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_Z, TR_0_0_0),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12),
                    new Generator(ROT_mY_mX_Z, TR_12_12_12),
                    new Generator(ROT_Y_X_Z, TR_12_12_12) });

    /** Space group 108. */
    public static final SpaceGroup SG108 = new SpaceGroup(108, "I4cm",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_Z, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_mY_mX_Z, TR_0_0_12),
                    new Generator(ROT_Y_X_Z, TR_0_0_12),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_12_12_12),
                    new Generator(ROT_Y_mX_Z, TR_12_12_12),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_mY_mX_Z, TR_12_12_0),
                    new Generator(ROT_Y_X_Z, TR_12_12_0) });

    /** Space group 109. */
    public static final SpaceGroup SG109 = new SpaceGroup(109, "I41md",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_0_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_0_34),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_12_12_12),
                    new Generator(ROT_mY_mX_Z, TR_0_12_14),
                    new Generator(ROT_Y_X_Z, TR_12_0_34),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_12_14),
                    new Generator(ROT_X_mY_Z, TR_12_12_12),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_mY_mX_Z, TR_12_0_34),
                    new Generator(ROT_Y_X_Z, TR_0_12_14) });

    /** Space group 110. */
    public static final SpaceGroup SG110 = new SpaceGroup(110, "I41cd",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_12_12_12),
                    new Generator(ROT_mY_X_Z, TR_0_12_14),
                    new Generator(ROT_Y_mX_Z, TR_12_0_34),
                    new Generator(ROT_X_mY_Z, TR_0_0_12),
                    new Generator(ROT_mX_Y_Z, TR_12_12_0),
                    new Generator(ROT_mY_mX_Z, TR_0_12_34),
                    new Generator(ROT_Y_X_Z, TR_12_0_14),
                    new Generator(ROT_X_Y_Z, TR_12_12_12),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_Z, TR_12_0_34),
                    new Generator(ROT_Y_mX_Z, TR_0_12_14),
                    new Generator(ROT_X_mY_Z, TR_12_12_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_12),
                    new Generator(ROT_mY_mX_Z, TR_12_0_14),
                    new Generator(ROT_Y_X_Z, TR_0_12_34) });

    /** Space group 111. */
    public static final SpaceGroup SG111 = new SpaceGroup(111, "P-42m",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_Z, TR_0_0_0) });

    /** Space group 112. */
    public static final SpaceGroup SG112 = new SpaceGroup(112, "P-42c",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_0_0_12),
                    new Generator(ROT_X_mY_mZ, TR_0_0_12),
                    new Generator(ROT_mY_mX_Z, TR_0_0_12),
                    new Generator(ROT_Y_X_Z, TR_0_0_12) });

    /** Space group 113. */
    public static final SpaceGroup SG113 = new SpaceGroup(113, "P-421m",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_0),
                    new Generator(ROT_X_mY_mZ, TR_12_12_0),
                    new Generator(ROT_mY_mX_Z, TR_12_12_0),
                    new Generator(ROT_Y_X_Z, TR_12_12_0) });

    /** Space group 114. */
    public static final SpaceGroup SG114 = new SpaceGroup(114, "P-421c",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mX_Y_mZ, TR_12_12_12),
                    new Generator(ROT_X_mY_mZ, TR_12_12_12),
                    new Generator(ROT_mY_mX_Z, TR_12_12_12),
                    new Generator(ROT_Y_X_Z, TR_12_12_12) });

    /** Space group 115. */
    public static final SpaceGroup SG115 = new SpaceGroup(115, "P-4m2",
            TETRAGONAL, LG4mmm, new Generator[] {
                    new Generator(ROT_X_Y_Z, TR_0_0_0),
                    new Generator(ROT_mX_mY_Z, TR_0_0_0),
                    new Generator(ROT_Y_mX_mZ, TR_0_0_0),
                    new Generator(ROT_mY_X_mZ, TR_0_0_0),
                    new Generator(ROT_X_mY_Z, TR_0_0_0),
                    new Generator(ROT_mX_Y_Z, TR_0_0_0),
                    new Generator(ROT_Y_X_mZ, TR_0_0_0),
                    new Generator(ROT_mY_mX_mZ, TR_0_0_0) });



    /**
     * Initialises the crystal systems lookup table.
     */
    public void initCrystalSystems();



    /**
     * Initialises the indexes lookup table.
     */
    public void initIndexes();



    /**
     * Initialises the Laue group lookup table.
     */
    public void initLaueGroups();



    /**
     * Initialises the symbols lookup table.
     */
    public void initSymbols();
}

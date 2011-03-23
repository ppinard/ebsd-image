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

import java.util.HashMap;
import java.util.HashSet;

import static crystallography.core.CrystalSystem.*;
import static crystallography.core.LaueGroup.*;

/**
 * Utilities to get and list space groups.
 * 
 * @author Philippe T. Pinard
 */
public final class SpaceGroups implements SpaceGroups1, SpaceGroups2 {

    /**
     * Singleton holder.
     * 
     * @author Philippe T. Pinard
     */
    private static class Holder {
        /** Singleton instance. */
        private static final SpaceGroups INSTANCE = new SpaceGroups();
    }



    /**
     * Return the space group with the specified index/number.
     * 
     * @param index
     *            index/number of the space group
     * @return space group
     * @throws IllegalArgumentException
     *             if the index is unknown
     */
    public static SpaceGroup fromIndex(int index) {
        SpaceGroup result = Holder.INSTANCE.indexes.get(index);

        if (result == null)
            throw new IllegalArgumentException("Unknown space group index ("
                    + index + ").");

        return result;
    }



    /**
     * Return the space group with the specified symbol/name.
     * 
     * @param symbol
     *            symbol/name of the space group
     * @return space group
     * @throws IllegalArgumentException
     *             if the symbol is unknown
     */
    public static SpaceGroup fromSymbol(String symbol) {
        SpaceGroup result = Holder.INSTANCE.symbols.get(symbol);

        if (result == null)
            throw new IllegalArgumentException("Unknown space group symbol ("
                    + symbol + ").");

        return result;
    }



    /**
     * Returns an array of all the space groups with the specified crystal
     * system.
     * 
     * @param crystalSystem
     *            crystal system
     * @return array of space groups in the crystal system
     */
    public static SpaceGroup[] list(CrystalSystem crystalSystem) {
        return Holder.INSTANCE.crystalSystems.get(crystalSystem).toArray(
                new SpaceGroup[0]);
    }



    /**
     * Returns an array of all the space groups with the specified Laue group.
     * 
     * @param laueGroup
     *            Laue Group
     * @return array of space groups in the Laue group
     */
    public static SpaceGroup[] list(LaueGroup laueGroup) {
        return Holder.INSTANCE.laueGroups.get(laueGroup).toArray(
                new SpaceGroup[0]);
    }

    /** Lookup table with the space group's crystal system. */
    private final HashMap<CrystalSystem, HashSet<SpaceGroup>> crystalSystems;

    /** Lookup table with the space group's index. */
    private final HashMap<Integer, SpaceGroup> indexes;

    /** Lookup table with the space group's Laue group. */
    private final HashMap<LaueGroup, HashSet<SpaceGroup>> laueGroups;

    /** Lookup table with the space group's symbol. */
    private final HashMap<String, SpaceGroup> symbols;



    /**
     * Creates a new <code>SpaceGroup</code> and set-ups the lookup tables.
     */
    private SpaceGroups() {
        indexes = new HashMap<Integer, SpaceGroup>();
        symbols = new HashMap<String, SpaceGroup>();
        laueGroups = new HashMap<LaueGroup, HashSet<SpaceGroup>>();
        crystalSystems = new HashMap<CrystalSystem, HashSet<SpaceGroup>>();

        initIndexes();
        initSymbols();
        initLaueGroups();
        initCrystalSystems();
    }



    @Override
    public void initCrystalSystems() {
        crystalSystems.put(TRICLINIC, new HashSet<SpaceGroup>());
        crystalSystems.get(TRICLINIC).addAll(laueGroups.get(LG1));

        crystalSystems.put(MONOCLINIC, new HashSet<SpaceGroup>());
        crystalSystems.get(MONOCLINIC).addAll(laueGroups.get(LG2m));

        crystalSystems.put(ORTHORHOMBIC, new HashSet<SpaceGroup>());
        crystalSystems.get(ORTHORHOMBIC).addAll(laueGroups.get(LGmmm));

        crystalSystems.put(TETRAGONAL, new HashSet<SpaceGroup>());
        crystalSystems.get(TETRAGONAL).addAll(laueGroups.get(LG4m));
        crystalSystems.get(TETRAGONAL).addAll(laueGroups.get(LG4mmm));

        crystalSystems.put(TRIGONAL, new HashSet<SpaceGroup>());
        crystalSystems.get(TRIGONAL).addAll(laueGroups.get(LG3));
        crystalSystems.get(TRIGONAL).addAll(laueGroups.get(LG3m));

        crystalSystems.put(HEXAGONAL, new HashSet<SpaceGroup>());
        crystalSystems.get(HEXAGONAL).addAll(laueGroups.get(LG6m));
        crystalSystems.get(HEXAGONAL).addAll(laueGroups.get(LG6mmm));

        crystalSystems.put(CUBIC, new HashSet<SpaceGroup>());
        crystalSystems.get(CUBIC).addAll(laueGroups.get(LGm3));
        crystalSystems.get(CUBIC).addAll(laueGroups.get(LGm3m));
    }



    @Override
    public void initIndexes() {
        indexes.put(1, SG1);
        indexes.put(2, SG2);
        indexes.put(3, SG3);
        indexes.put(4, SG4);
        indexes.put(5, SG5);
        indexes.put(6, SG6);
        indexes.put(7, SG7);
        indexes.put(8, SG8);
        indexes.put(9, SG9);
        indexes.put(10, SG10);
        indexes.put(11, SG11);
        indexes.put(12, SG12);
        indexes.put(13, SG13);
        indexes.put(14, SG14);
        indexes.put(15, SG15);
        indexes.put(16, SG16);
        indexes.put(17, SG17);
        indexes.put(18, SG18);
        indexes.put(19, SG19);
        indexes.put(20, SG20);
        indexes.put(21, SG21);
        indexes.put(22, SG22);
        indexes.put(23, SG23);
        indexes.put(24, SG24);
        indexes.put(25, SG25);
        indexes.put(26, SG26);
        indexes.put(27, SG27);
        indexes.put(28, SG28);
        indexes.put(29, SG29);
        indexes.put(30, SG30);
        indexes.put(31, SG31);
        indexes.put(32, SG32);
        indexes.put(33, SG33);
        indexes.put(34, SG34);
        indexes.put(35, SG35);
        indexes.put(36, SG36);
        indexes.put(37, SG37);
        indexes.put(38, SG38);
        indexes.put(39, SG39);
        indexes.put(40, SG40);
        indexes.put(41, SG41);
        indexes.put(42, SG42);
        indexes.put(43, SG43);
        indexes.put(44, SG44);
        indexes.put(45, SG45);
        indexes.put(46, SG46);
        indexes.put(47, SG47);
        indexes.put(48, SG48);
        indexes.put(49, SG49);
        indexes.put(50, SG50);
        indexes.put(51, SG51);
        indexes.put(52, SG52);
        indexes.put(53, SG53);
        indexes.put(54, SG54);
        indexes.put(55, SG55);
        indexes.put(56, SG56);
        indexes.put(57, SG57);
        indexes.put(58, SG58);
        indexes.put(59, SG59);
        indexes.put(60, SG60);
        indexes.put(61, SG61);
        indexes.put(62, SG62);
        indexes.put(63, SG63);
        indexes.put(64, SG64);
        indexes.put(65, SG65);
        indexes.put(66, SG66);
        indexes.put(67, SG67);
        indexes.put(68, SG68);
        indexes.put(69, SG69);
        indexes.put(70, SG70);
        indexes.put(71, SG71);
        indexes.put(72, SG72);
        indexes.put(73, SG73);
        indexes.put(74, SG74);
        indexes.put(75, SG75);
        indexes.put(76, SG76);
        indexes.put(77, SG77);
        indexes.put(78, SG78);
        indexes.put(79, SG79);
        indexes.put(80, SG80);
        indexes.put(81, SG81);
        indexes.put(82, SG82);
        indexes.put(83, SG83);
        indexes.put(84, SG84);
        indexes.put(85, SG85);
        indexes.put(86, SG86);
        indexes.put(87, SG87);
        indexes.put(88, SG88);
        indexes.put(89, SG89);
        indexes.put(90, SG90);
        indexes.put(91, SG91);
        indexes.put(92, SG92);
        indexes.put(93, SG93);
        indexes.put(94, SG94);
        indexes.put(95, SG95);
        indexes.put(96, SG96);
        indexes.put(97, SG97);
        indexes.put(98, SG98);
        indexes.put(99, SG99);
        indexes.put(100, SG100);
        indexes.put(101, SG101);
        indexes.put(102, SG102);
        indexes.put(103, SG103);
        indexes.put(104, SG104);
        indexes.put(105, SG105);
        indexes.put(106, SG106);
        indexes.put(107, SG107);
        indexes.put(108, SG108);
        indexes.put(109, SG109);
        indexes.put(110, SG110);
        indexes.put(111, SG111);
        indexes.put(112, SG112);
        indexes.put(113, SG113);
        indexes.put(114, SG114);
        indexes.put(115, SG115);
        indexes.put(116, SG116);
        indexes.put(117, SG117);
        indexes.put(118, SG118);
        indexes.put(119, SG119);
        indexes.put(120, SG120);
        indexes.put(121, SG121);
        indexes.put(122, SG122);
        indexes.put(123, SG123);
        indexes.put(124, SG124);
        indexes.put(125, SG125);
        indexes.put(126, SG126);
        indexes.put(127, SG127);
        indexes.put(128, SG128);
        indexes.put(129, SG129);
        indexes.put(130, SG130);
        indexes.put(131, SG131);
        indexes.put(132, SG132);
        indexes.put(133, SG133);
        indexes.put(134, SG134);
        indexes.put(135, SG135);
        indexes.put(136, SG136);
        indexes.put(137, SG137);
        indexes.put(138, SG138);
        indexes.put(139, SG139);
        indexes.put(140, SG140);
        indexes.put(141, SG141);
        indexes.put(142, SG142);
        indexes.put(143, SG143);
        indexes.put(144, SG144);
        indexes.put(145, SG145);
        indexes.put(146, SG146);
        indexes.put(147, SG147);
        indexes.put(148, SG148);
        indexes.put(149, SG149);
        indexes.put(150, SG150);
        indexes.put(151, SG151);
        indexes.put(152, SG152);
        indexes.put(153, SG153);
        indexes.put(154, SG154);
        indexes.put(155, SG155);
        indexes.put(156, SG156);
        indexes.put(157, SG157);
        indexes.put(158, SG158);
        indexes.put(159, SG159);
        indexes.put(160, SG160);
        indexes.put(161, SG161);
        indexes.put(162, SG162);
        indexes.put(163, SG163);
        indexes.put(164, SG164);
        indexes.put(165, SG165);
        indexes.put(166, SG166);
        indexes.put(167, SG167);
        indexes.put(168, SG168);
        indexes.put(169, SG169);
        indexes.put(170, SG170);
        indexes.put(171, SG171);
        indexes.put(172, SG172);
        indexes.put(173, SG173);
        indexes.put(174, SG174);
        indexes.put(175, SG175);
        indexes.put(176, SG176);
        indexes.put(177, SG177);
        indexes.put(178, SG178);
        indexes.put(179, SG179);
        indexes.put(180, SG180);
        indexes.put(181, SG181);
        indexes.put(182, SG182);
        indexes.put(183, SG183);
        indexes.put(184, SG184);
        indexes.put(185, SG185);
        indexes.put(186, SG186);
        indexes.put(187, SG187);
        indexes.put(188, SG188);
        indexes.put(189, SG189);
        indexes.put(190, SG190);
        indexes.put(191, SG191);
        indexes.put(192, SG192);
        indexes.put(193, SG193);
        indexes.put(194, SG194);
        indexes.put(195, SG195);
        indexes.put(196, SG196);
        indexes.put(197, SG197);
        indexes.put(198, SG198);
        indexes.put(199, SG199);
        indexes.put(200, SG200);
        indexes.put(201, SG201);
        indexes.put(202, SG202);
        indexes.put(203, SG203);
        indexes.put(204, SG204);
        indexes.put(205, SG205);
        indexes.put(206, SG206);
        indexes.put(207, SG207);
        indexes.put(208, SG208);
        indexes.put(209, SG209);
        indexes.put(210, SG210);
        indexes.put(211, SG211);
        indexes.put(212, SG212);
        indexes.put(213, SG213);
        indexes.put(214, SG214);
        indexes.put(215, SG215);
        indexes.put(216, SG216);
        indexes.put(217, SG217);
        indexes.put(218, SG218);
        indexes.put(219, SG219);
        indexes.put(220, SG220);
        indexes.put(221, SG221);
        indexes.put(222, SG222);
        indexes.put(223, SG223);
        indexes.put(224, SG224);
        indexes.put(225, SG225);
        indexes.put(226, SG226);
        indexes.put(227, SG227);
        indexes.put(228, SG228);
        indexes.put(229, SG229);
        indexes.put(230, SG230);

        // 7 extras
        indexes.put(1146, SG1146);
        indexes.put(1148, SG1148);
        indexes.put(1155, SG1155);
        indexes.put(1160, SG1160);
        indexes.put(1161, SG1161);
        indexes.put(1166, SG1166);
        indexes.put(1167, SG1167);
    }



    @Override
    public void initLaueGroups() {
        laueGroups.put(LG1, new HashSet<SpaceGroup>());
        laueGroups.get(LG1).add(SG1);
        laueGroups.get(LG1).add(SG2);

        laueGroups.put(LG2m, new HashSet<SpaceGroup>());
        laueGroups.get(LG2m).add(SG3);
        laueGroups.get(LG2m).add(SG4);
        laueGroups.get(LG2m).add(SG5);
        laueGroups.get(LG2m).add(SG6);
        laueGroups.get(LG2m).add(SG7);
        laueGroups.get(LG2m).add(SG8);
        laueGroups.get(LG2m).add(SG9);
        laueGroups.get(LG2m).add(SG10);
        laueGroups.get(LG2m).add(SG11);
        laueGroups.get(LG2m).add(SG12);
        laueGroups.get(LG2m).add(SG13);
        laueGroups.get(LG2m).add(SG14);
        laueGroups.get(LG2m).add(SG15);

        laueGroups.put(LGmmm, new HashSet<SpaceGroup>());
        laueGroups.get(LGmmm).add(SG16);
        laueGroups.get(LGmmm).add(SG17);
        laueGroups.get(LGmmm).add(SG18);
        laueGroups.get(LGmmm).add(SG19);
        laueGroups.get(LGmmm).add(SG20);
        laueGroups.get(LGmmm).add(SG21);
        laueGroups.get(LGmmm).add(SG22);
        laueGroups.get(LGmmm).add(SG23);
        laueGroups.get(LGmmm).add(SG24);
        laueGroups.get(LGmmm).add(SG25);
        laueGroups.get(LGmmm).add(SG26);
        laueGroups.get(LGmmm).add(SG27);
        laueGroups.get(LGmmm).add(SG28);
        laueGroups.get(LGmmm).add(SG29);
        laueGroups.get(LGmmm).add(SG30);
        laueGroups.get(LGmmm).add(SG31);
        laueGroups.get(LGmmm).add(SG32);
        laueGroups.get(LGmmm).add(SG33);
        laueGroups.get(LGmmm).add(SG34);
        laueGroups.get(LGmmm).add(SG35);
        laueGroups.get(LGmmm).add(SG36);
        laueGroups.get(LGmmm).add(SG37);
        laueGroups.get(LGmmm).add(SG38);
        laueGroups.get(LGmmm).add(SG39);
        laueGroups.get(LGmmm).add(SG40);
        laueGroups.get(LGmmm).add(SG41);
        laueGroups.get(LGmmm).add(SG42);
        laueGroups.get(LGmmm).add(SG43);
        laueGroups.get(LGmmm).add(SG44);
        laueGroups.get(LGmmm).add(SG45);
        laueGroups.get(LGmmm).add(SG46);
        laueGroups.get(LGmmm).add(SG47);
        laueGroups.get(LGmmm).add(SG48);
        laueGroups.get(LGmmm).add(SG49);
        laueGroups.get(LGmmm).add(SG50);
        laueGroups.get(LGmmm).add(SG51);
        laueGroups.get(LGmmm).add(SG52);
        laueGroups.get(LGmmm).add(SG53);
        laueGroups.get(LGmmm).add(SG54);
        laueGroups.get(LGmmm).add(SG55);
        laueGroups.get(LGmmm).add(SG56);
        laueGroups.get(LGmmm).add(SG57);
        laueGroups.get(LGmmm).add(SG58);
        laueGroups.get(LGmmm).add(SG59);
        laueGroups.get(LGmmm).add(SG60);
        laueGroups.get(LGmmm).add(SG61);
        laueGroups.get(LGmmm).add(SG62);
        laueGroups.get(LGmmm).add(SG63);
        laueGroups.get(LGmmm).add(SG64);
        laueGroups.get(LGmmm).add(SG65);
        laueGroups.get(LGmmm).add(SG66);
        laueGroups.get(LGmmm).add(SG67);
        laueGroups.get(LGmmm).add(SG68);
        laueGroups.get(LGmmm).add(SG69);
        laueGroups.get(LGmmm).add(SG70);
        laueGroups.get(LGmmm).add(SG71);
        laueGroups.get(LGmmm).add(SG72);
        laueGroups.get(LGmmm).add(SG73);
        laueGroups.get(LGmmm).add(SG74);

        laueGroups.put(LG4m, new HashSet<SpaceGroup>());
        laueGroups.get(LG4m).add(SG75);
        laueGroups.get(LG4m).add(SG76);
        laueGroups.get(LG4m).add(SG77);
        laueGroups.get(LG4m).add(SG78);
        laueGroups.get(LG4m).add(SG79);
        laueGroups.get(LG4m).add(SG80);
        laueGroups.get(LG4m).add(SG81);
        laueGroups.get(LG4m).add(SG82);
        laueGroups.get(LG4m).add(SG83);
        laueGroups.get(LG4m).add(SG84);
        laueGroups.get(LG4m).add(SG85);
        laueGroups.get(LG4m).add(SG86);
        laueGroups.get(LG4m).add(SG87);
        laueGroups.get(LG4m).add(SG88);

        laueGroups.put(LG4mmm, new HashSet<SpaceGroup>());
        laueGroups.get(LG4mmm).add(SG89);
        laueGroups.get(LG4mmm).add(SG90);
        laueGroups.get(LG4mmm).add(SG91);
        laueGroups.get(LG4mmm).add(SG92);
        laueGroups.get(LG4mmm).add(SG93);
        laueGroups.get(LG4mmm).add(SG94);
        laueGroups.get(LG4mmm).add(SG95);
        laueGroups.get(LG4mmm).add(SG96);
        laueGroups.get(LG4mmm).add(SG97);
        laueGroups.get(LG4mmm).add(SG98);
        laueGroups.get(LG4mmm).add(SG99);
        laueGroups.get(LG4mmm).add(SG100);
        laueGroups.get(LG4mmm).add(SG101);
        laueGroups.get(LG4mmm).add(SG102);
        laueGroups.get(LG4mmm).add(SG103);
        laueGroups.get(LG4mmm).add(SG104);
        laueGroups.get(LG4mmm).add(SG105);
        laueGroups.get(LG4mmm).add(SG106);
        laueGroups.get(LG4mmm).add(SG107);
        laueGroups.get(LG4mmm).add(SG108);
        laueGroups.get(LG4mmm).add(SG109);
        laueGroups.get(LG4mmm).add(SG110);
        laueGroups.get(LG4mmm).add(SG111);
        laueGroups.get(LG4mmm).add(SG112);
        laueGroups.get(LG4mmm).add(SG113);
        laueGroups.get(LG4mmm).add(SG114);
        laueGroups.get(LG4mmm).add(SG115);
        laueGroups.get(LG4mmm).add(SG116);
        laueGroups.get(LG4mmm).add(SG117);
        laueGroups.get(LG4mmm).add(SG118);
        laueGroups.get(LG4mmm).add(SG119);
        laueGroups.get(LG4mmm).add(SG120);
        laueGroups.get(LG4mmm).add(SG121);
        laueGroups.get(LG4mmm).add(SG122);
        laueGroups.get(LG4mmm).add(SG123);
        laueGroups.get(LG4mmm).add(SG124);
        laueGroups.get(LG4mmm).add(SG125);
        laueGroups.get(LG4mmm).add(SG126);
        laueGroups.get(LG4mmm).add(SG127);
        laueGroups.get(LG4mmm).add(SG128);
        laueGroups.get(LG4mmm).add(SG129);
        laueGroups.get(LG4mmm).add(SG130);
        laueGroups.get(LG4mmm).add(SG131);
        laueGroups.get(LG4mmm).add(SG132);
        laueGroups.get(LG4mmm).add(SG133);
        laueGroups.get(LG4mmm).add(SG134);
        laueGroups.get(LG4mmm).add(SG135);
        laueGroups.get(LG4mmm).add(SG136);
        laueGroups.get(LG4mmm).add(SG137);
        laueGroups.get(LG4mmm).add(SG138);
        laueGroups.get(LG4mmm).add(SG139);
        laueGroups.get(LG4mmm).add(SG140);
        laueGroups.get(LG4mmm).add(SG141);
        laueGroups.get(LG4mmm).add(SG142);

        laueGroups.put(LG3, new HashSet<SpaceGroup>());
        laueGroups.get(LG3).add(SG143);
        laueGroups.get(LG3).add(SG144);
        laueGroups.get(LG3).add(SG145);
        laueGroups.get(LG3).add(SG146);
        laueGroups.get(LG3).add(SG1146);
        laueGroups.get(LG3).add(SG147);
        laueGroups.get(LG3).add(SG148);
        laueGroups.get(LG3).add(SG1148);

        laueGroups.put(LG3m, new HashSet<SpaceGroup>());
        laueGroups.get(LG3m).add(SG149);
        laueGroups.get(LG3m).add(SG150);
        laueGroups.get(LG3m).add(SG151);
        laueGroups.get(LG3m).add(SG152);
        laueGroups.get(LG3m).add(SG153);
        laueGroups.get(LG3m).add(SG154);
        laueGroups.get(LG3m).add(SG155);
        laueGroups.get(LG3m).add(SG1155);
        laueGroups.get(LG3m).add(SG156);
        laueGroups.get(LG3m).add(SG157);
        laueGroups.get(LG3m).add(SG158);
        laueGroups.get(LG3m).add(SG159);
        laueGroups.get(LG3m).add(SG160);
        laueGroups.get(LG3m).add(SG1160);
        laueGroups.get(LG3m).add(SG161);
        laueGroups.get(LG3m).add(SG1161);
        laueGroups.get(LG3m).add(SG162);
        laueGroups.get(LG3m).add(SG163);
        laueGroups.get(LG3m).add(SG164);
        laueGroups.get(LG3m).add(SG165);
        laueGroups.get(LG3m).add(SG166);
        laueGroups.get(LG3m).add(SG1166);
        laueGroups.get(LG3m).add(SG167);
        laueGroups.get(LG3m).add(SG1167);

        laueGroups.put(LG6m, new HashSet<SpaceGroup>());
        laueGroups.get(LG6m).add(SG168);
        laueGroups.get(LG6m).add(SG169);
        laueGroups.get(LG6m).add(SG170);
        laueGroups.get(LG6m).add(SG171);
        laueGroups.get(LG6m).add(SG172);
        laueGroups.get(LG6m).add(SG173);
        laueGroups.get(LG6m).add(SG174);
        laueGroups.get(LG6m).add(SG175);
        laueGroups.get(LG6m).add(SG176);

        laueGroups.put(LG6mmm, new HashSet<SpaceGroup>());
        laueGroups.get(LG6mmm).add(SG177);
        laueGroups.get(LG6mmm).add(SG178);
        laueGroups.get(LG6mmm).add(SG179);
        laueGroups.get(LG6mmm).add(SG180);
        laueGroups.get(LG6mmm).add(SG181);
        laueGroups.get(LG6mmm).add(SG182);
        laueGroups.get(LG6mmm).add(SG183);
        laueGroups.get(LG6mmm).add(SG184);
        laueGroups.get(LG6mmm).add(SG185);
        laueGroups.get(LG6mmm).add(SG186);
        laueGroups.get(LG6mmm).add(SG187);
        laueGroups.get(LG6mmm).add(SG188);
        laueGroups.get(LG6mmm).add(SG189);
        laueGroups.get(LG6mmm).add(SG190);
        laueGroups.get(LG6mmm).add(SG191);
        laueGroups.get(LG6mmm).add(SG192);
        laueGroups.get(LG6mmm).add(SG193);
        laueGroups.get(LG6mmm).add(SG194);

        laueGroups.put(LGm3, new HashSet<SpaceGroup>());
        laueGroups.get(LGm3).add(SG195);
        laueGroups.get(LGm3).add(SG196);
        laueGroups.get(LGm3).add(SG197);
        laueGroups.get(LGm3).add(SG198);
        laueGroups.get(LGm3).add(SG199);
        laueGroups.get(LGm3).add(SG200);
        laueGroups.get(LGm3).add(SG201);
        laueGroups.get(LGm3).add(SG202);
        laueGroups.get(LGm3).add(SG203);
        laueGroups.get(LGm3).add(SG204);
        laueGroups.get(LGm3).add(SG205);
        laueGroups.get(LGm3).add(SG206);

        laueGroups.put(LGm3m, new HashSet<SpaceGroup>());
        laueGroups.get(LGm3m).add(SG207);
        laueGroups.get(LGm3m).add(SG208);
        laueGroups.get(LGm3m).add(SG209);
        laueGroups.get(LGm3m).add(SG210);
        laueGroups.get(LGm3m).add(SG211);
        laueGroups.get(LGm3m).add(SG212);
        laueGroups.get(LGm3m).add(SG213);
        laueGroups.get(LGm3m).add(SG214);
        laueGroups.get(LGm3m).add(SG215);
        laueGroups.get(LGm3m).add(SG216);
        laueGroups.get(LGm3m).add(SG217);
        laueGroups.get(LGm3m).add(SG218);
        laueGroups.get(LGm3m).add(SG219);
        laueGroups.get(LGm3m).add(SG220);
        laueGroups.get(LGm3m).add(SG221);
        laueGroups.get(LGm3m).add(SG222);
        laueGroups.get(LGm3m).add(SG223);
        laueGroups.get(LGm3m).add(SG224);
        laueGroups.get(LGm3m).add(SG225);
        laueGroups.get(LGm3m).add(SG226);
        laueGroups.get(LGm3m).add(SG227);
        laueGroups.get(LGm3m).add(SG228);
        laueGroups.get(LGm3m).add(SG229);
        laueGroups.get(LGm3m).add(SG230);
    }



    @Override
    public void initSymbols() {
        symbols.put("P1", SG1);
        symbols.put("P-1", SG2);
        symbols.put("P2", SG3);
        symbols.put("P21", SG4);
        symbols.put("C2", SG5);
        symbols.put("Pm", SG6);
        symbols.put("Pc", SG7);
        symbols.put("Cm", SG8);
        symbols.put("Cc", SG9);
        symbols.put("P2/m", SG10);
        symbols.put("P21/m", SG11);
        symbols.put("C2/m", SG12);
        symbols.put("P2/c", SG13);
        symbols.put("P21/c", SG14);
        symbols.put("C2/c", SG15);
        symbols.put("P222", SG16);
        symbols.put("P2221", SG17);
        symbols.put("P21212", SG18);
        symbols.put("P212121", SG19);
        symbols.put("C2221", SG20);
        symbols.put("C222", SG21);
        symbols.put("F222", SG22);
        symbols.put("I222", SG23);
        symbols.put("I212121", SG24);
        symbols.put("Pmm2", SG25);
        symbols.put("Pmc21", SG26);
        symbols.put("Pcc2", SG27);
        symbols.put("Pma2", SG28);
        symbols.put("Pca21", SG29);
        symbols.put("Pnc2", SG30);
        symbols.put("Pmn21", SG31);
        symbols.put("Pba2", SG32);
        symbols.put("Pna21", SG33);
        symbols.put("Pnn2", SG34);
        symbols.put("Cmm2", SG35);
        symbols.put("Cmc21", SG36);
        symbols.put("Ccc2", SG37);
        symbols.put("Amm2", SG38);
        symbols.put("Abm2", SG39);
        symbols.put("Ama2", SG40);
        symbols.put("Aba2", SG41);
        symbols.put("Fmm2", SG42);
        symbols.put("Fdd2", SG43);
        symbols.put("Imm2", SG44);
        symbols.put("Iba2", SG45);
        symbols.put("Ima2", SG46);
        symbols.put("Pmmm", SG47);
        symbols.put("Pnnn", SG48);
        symbols.put("Pccm", SG49);
        symbols.put("Pban", SG50);
        symbols.put("Pmma", SG51);
        symbols.put("Pnna", SG52);
        symbols.put("Pmna", SG53);
        symbols.put("Pcca", SG54);
        symbols.put("Pbam", SG55);
        symbols.put("Pccn", SG56);
        symbols.put("Pbcm", SG57);
        symbols.put("Pnnm", SG58);
        symbols.put("Pmmn", SG59);
        symbols.put("Pbcn", SG60);
        symbols.put("Pbca", SG61);
        symbols.put("Pnma", SG62);
        symbols.put("Cmcm", SG63);
        symbols.put("Cmca", SG64);
        symbols.put("Cmmm", SG65);
        symbols.put("Cccm", SG66);
        symbols.put("Cmma", SG67);
        symbols.put("Ccca", SG68);
        symbols.put("Fmmm", SG69);
        symbols.put("Fddd", SG70);
        symbols.put("Immm", SG71);
        symbols.put("Ibam", SG72);
        symbols.put("Ibca", SG73);
        symbols.put("Imma", SG74);
        symbols.put("P4", SG75);
        symbols.put("P41", SG76);
        symbols.put("P42", SG77);
        symbols.put("P43", SG78);
        symbols.put("I4", SG79);
        symbols.put("I41", SG80);
        symbols.put("P-4", SG81);
        symbols.put("I-4", SG82);
        symbols.put("P4/m", SG83);
        symbols.put("P42/m", SG84);
        symbols.put("P4/n", SG85);
        symbols.put("P42/n", SG86);
        symbols.put("I4/m", SG87);
        symbols.put("I41/a", SG88);
        symbols.put("P422", SG89);
        symbols.put("P4212", SG90);
        symbols.put("P4122", SG91);
        symbols.put("P41212", SG92);
        symbols.put("P4222", SG93);
        symbols.put("P42212", SG94);
        symbols.put("P4322", SG95);
        symbols.put("P43212", SG96);
        symbols.put("I422", SG97);
        symbols.put("I4122", SG98);
        symbols.put("P4mm", SG99);
        symbols.put("P4bm", SG100);
        symbols.put("P42cm", SG101);
        symbols.put("P42nm", SG102);
        symbols.put("P4cc", SG103);
        symbols.put("P4nc", SG104);
        symbols.put("P42mc", SG105);
        symbols.put("P42bc", SG106);
        symbols.put("I4mm", SG107);
        symbols.put("I4cm", SG108);
        symbols.put("I41md", SG109);
        symbols.put("I41cd", SG110);
        symbols.put("P-42m", SG111);
        symbols.put("P-42c", SG112);
        symbols.put("P-421m", SG113);
        symbols.put("P-421c", SG114);
        symbols.put("P-4m2", SG115);
        symbols.put("P-4c2", SG116);
        symbols.put("P-4b2", SG117);
        symbols.put("P-4n2", SG118);
        symbols.put("I-4m2", SG119);
        symbols.put("I-4c2", SG120);
        symbols.put("I-42m", SG121);
        symbols.put("I-42d", SG122);
        symbols.put("P4/mmm", SG123);
        symbols.put("P4/mcc", SG124);
        symbols.put("P4/nbm", SG125);
        symbols.put("P4/nnc", SG126);
        symbols.put("P4/mbm", SG127);
        symbols.put("P4/mnc", SG128);
        symbols.put("P4/nmm", SG129);
        symbols.put("P4/ncc", SG130);
        symbols.put("P42/mmc", SG131);
        symbols.put("P42/mcm", SG132);
        symbols.put("P42/nbc", SG133);
        symbols.put("P42/nnm", SG134);
        symbols.put("P42/mbc", SG135);
        symbols.put("P42/mnm", SG136);
        symbols.put("P42/nmc", SG137);
        symbols.put("P42/ncm", SG138);
        symbols.put("I4/mmm", SG139);
        symbols.put("I4/mcm", SG140);
        symbols.put("I41/amd", SG141);
        symbols.put("I41/acd", SG142);
        symbols.put("P3", SG143);
        symbols.put("P31", SG144);
        symbols.put("P32", SG145);
        symbols.put("H3", SG146);
        symbols.put("R3", SG1146);
        symbols.put("P-3", SG147);
        symbols.put("H-3", SG148);
        symbols.put("R-3", SG1148);
        symbols.put("P312", SG149);
        symbols.put("P321", SG150);
        symbols.put("P3112", SG151);
        symbols.put("P3121", SG152);
        symbols.put("P3212", SG153);
        symbols.put("P3221", SG154);
        symbols.put("H32", SG155);
        symbols.put("R32", SG1155);
        symbols.put("P3m1", SG156);
        symbols.put("P31m", SG157);
        symbols.put("P3c1", SG158);
        symbols.put("P31c", SG159);
        symbols.put("H3m", SG160);
        symbols.put("R3m", SG1160);
        symbols.put("H3c", SG161);
        symbols.put("R3c", SG1161);
        symbols.put("P-31m", SG162);
        symbols.put("P-31c", SG163);
        symbols.put("P-3m1", SG164);
        symbols.put("P-3c1", SG165);
        symbols.put("H-3m", SG166);
        symbols.put("R-3m", SG1166);
        symbols.put("H-3c", SG167);
        symbols.put("R-3c", SG1167);
        symbols.put("P6", SG168);
        symbols.put("P61", SG169);
        symbols.put("P65", SG170);
        symbols.put("P62", SG171);
        symbols.put("P64", SG172);
        symbols.put("P63", SG173);
        symbols.put("P-6", SG174);
        symbols.put("P6/m", SG175);
        symbols.put("P63/m", SG176);
        symbols.put("P622", SG177);
        symbols.put("P6122", SG178);
        symbols.put("P6522", SG179);
        symbols.put("P6222", SG180);
        symbols.put("P6422", SG181);
        symbols.put("P6322", SG182);
        symbols.put("P6mm", SG183);
        symbols.put("P6cc", SG184);
        symbols.put("P63cm", SG185);
        symbols.put("P63mc", SG186);
        symbols.put("P-6m2", SG187);
        symbols.put("P-6c2", SG188);
        symbols.put("P-62m", SG189);
        symbols.put("P-62c", SG190);
        symbols.put("P6/mmm", SG191);
        symbols.put("P6/mcc", SG192);
        symbols.put("P63/mcm", SG193);
        symbols.put("P63/mmc", SG194);
        symbols.put("P23", SG195);
        symbols.put("F23", SG196);
        symbols.put("I23", SG197);
        symbols.put("P213", SG198);
        symbols.put("I213", SG199);
        symbols.put("Pm-3", SG200);
        symbols.put("Pn-3", SG201);
        symbols.put("Fm-3", SG202);
        symbols.put("Fd-3", SG203);
        symbols.put("Im-3", SG204);
        symbols.put("Pa-3", SG205);
        symbols.put("Ia-3", SG206);
        symbols.put("P432", SG207);
        symbols.put("P4232", SG208);
        symbols.put("F432", SG209);
        symbols.put("F4132", SG210);
        symbols.put("I432", SG211);
        symbols.put("P4332", SG212);
        symbols.put("P4132", SG213);
        symbols.put("I4132", SG214);
        symbols.put("P-43m", SG215);
        symbols.put("F-43m", SG216);
        symbols.put("I-43m", SG217);
        symbols.put("P-43n", SG218);
        symbols.put("F-43c", SG219);
        symbols.put("I-43d", SG220);
        symbols.put("Pm-3m", SG221);
        symbols.put("Pn-3n", SG222);
        symbols.put("Pm-3n", SG223);
        symbols.put("Pn-3m", SG224);
        symbols.put("Fm-3m", SG225);
        symbols.put("Fm-3c", SG226);
        symbols.put("Fd-3m", SG227);
        symbols.put("Fd-3c", SG228);
        symbols.put("Im-3m", SG229);
        symbols.put("Ia-3d", SG230);
    }
}

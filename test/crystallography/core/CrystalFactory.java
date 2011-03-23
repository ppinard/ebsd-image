package crystallography.core;

public class CrystalFactory {

    public static Crystal ferrite() {
        return new Crystal("Ferrite", UnitCellFactory.cubic(2.87),
                AtomSitesFactory.atomSitesBCC(26), SpaceGroups.fromIndex(229));
    }



    public static Crystal silicon() {
        return new Crystal("Silicon", UnitCellFactory.cubic(5.43),
                AtomSitesFactory.atomSitesFCC(14), SpaceGroups.fromIndex(216));
    }



    public static Crystal zirconium() {
        return new Crystal("Zirconium", UnitCellFactory.hexagonal(3.2, 5.15),
                AtomSitesFactory.atomSitesHCP(40), SpaceGroups.fromIndex(194));
    }
}

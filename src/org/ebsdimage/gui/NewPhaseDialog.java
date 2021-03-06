/*
 * EBSD-Image
 * Copyright (C) 2010-2011 Philippe T. Pinard
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
package org.ebsdimage.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;
import ptpshared.gui.GuiUtil;
import ptpshared.util.ElementProperties;
import rmlimage.gui.BasicDialog;
import rmlshared.gui.*;
import rmlshared.thread.PlugIn;
import crystallography.core.*;
import static crystallography.core.LaueGroup.*;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

/**
 * Dialog to create a new phase.
 * 
 * @author Marin Lagac&eacute;
 * @author Philippe T. Pinard
 */
public class NewPhaseDialog extends BasicDialog {

    /**
     * Action to insert a new atom site.
     * 
     * @author Philippe T. Pinard
     */
    private class Add extends PlugIn {
        @Override
        public void xRun() {
            String symbol = symbolField.getText();
            if (!ElementProperties.isCorrect(symbol)) {
                ErrorDialog.show("Invalid atomic symbol");
                return;
            }

            if (!xField.isCorrect())
                return;
            if (!yField.isCorrect())
                return;
            if (!zField.isCorrect())
                return;
            if (!occupancyField.isCorrect())
                return;

            AtomSite atomSite =
                    new AtomSite(ElementProperties.getAtomicNumber(symbol),
                            xField.getValue(), yField.getValue(),
                            zField.getValue(), occupancyField.getValue());

            ((AtomSiteTableModel) atomSiteTable.getModel()).append(atomSite);
        }
    }

    /**
     * Table model for the atom site.
     * 
     * @author Philippe T. Pinard
     */
    private static class AtomSiteTableModel extends AbstractTableModel {
        /** Column names. */
        String[] columnNames = { "Symbol", "X", "Y", "Z", "Occupancy" };

        /** ArrayList of atom sites. */
        AtomSites atomSites = new AtomSites();



        /**
         * Appends an <code>AtomSite</code> to the bottom of the table.
         * 
         * @param atomSite
         *            <code>AtomSite</code> to append
         */
        public void append(AtomSite atomSite) {
            // Insert the atomSite
            atomSites.add(atomSite);

            int row = atomSites.size() - 1;
            fireTableRowsInserted(row, row);
        }



        /**
         * Clear all defined atom sites.
         */
        public void clear() {
            atomSites.clear();
            fireTableDataChanged();
        }



        /**
         * Returns a copy of the <code>AtomSite</code>s in the table.
         * 
         * @return atom sites
         */
        public AtomSites getAtomSites() {
            AtomSites atomSites = new AtomSites();
            for (AtomSite atomSite : this.atomSites)
                atomSites.add(atomSite);

            return atomSites;
        }



        @Override
        public int getColumnCount() {
            return 5;
        }



        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }



        @Override
        public int getRowCount() {
            return atomSites.size();
        }



        @Override
        public Object getValueAt(int row, int column) {
            AtomSite atomSite = atomSites.get(row);

            switch (column) {
            case 0:
                return ElementProperties.getSymbol(atomSite.atomicNumber);
            case 1:
                return atomSite.position.getX();
            case 2:
                return atomSite.position.getY();
            case 3:
                return atomSite.position.getZ();
            case 4:
                return atomSite.occupancy;
            default:
                throw new AssertionError("Invalid column no: " + column);
            }
        }



        /**
         * Removes an <code>AtomSite</code> from the table.
         * 
         * @param row
         *            index of the row to remove
         */
        public void remove(int row) {
            if (row < 0 || row >= atomSites.size())
                return;

            atomSites.remove(row);

            fireTableRowsDeleted(row, row);
        }

        // /**
        // * Inserts an <code>AtomSite</code> at the specified row.
        // *
        // * @param row
        // * row index (the top row has index 0)
        // * @param atomSite
        // * <code>AtomSite</code> to insert
        // */
        // public void insert(int row, AtomSite atomSite) {
        // if (row < 0)
        // throw new IllegalArgumentException("Invalid row: " + row);
        //
        // // If row > total number or rows, append the atomSite to the end
        // if (row >= atomSites.size()) {
        // append(atomSite);
        // return;
        // }
        //
        // // Insert the atomSite
        // atomSites.add(row, atomSite);
        //
        // fireTableRowsInserted(row, row);
        // }

    }

    /**
     * Action to calculate the equivalent atom sites positions.
     * 
     * @author Philippe T. Pinard
     */
    private class CalculateAtomSites extends PlugIn {

        @Override
        public void xRun() throws Exception {
            AtomSiteTableModel model =
                    (AtomSiteTableModel) atomSiteTable.getModel();
            AtomSites atoms = model.getAtomSites();
            SpaceGroup spaceGroup = spaceGroupCBox.getSelectedItem();

            atoms = Calculations.equivalentPositions(atoms, spaceGroup);

            model.clear();
            for (AtomSite atom : atoms)
                model.append(atom);
        }
    }

    /**
     * Listener of the crystal system combo box.
     */
    private class CrystalSystemCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshLaueGroup();
        }
    }

    /**
     * Listener of the Laue group combo box.
     */
    private class LaueGroupCBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            refreshSpaceGroup();
        }
    }

    /**
     * Action to remove an atom site.
     * 
     * @author Philippe T. Pinard
     */
    private class Remove extends PlugIn {
        @Override
        public void xRun() {
            AtomSiteTableModel model =
                    (AtomSiteTableModel) atomSiteTable.getModel();

            for (int row : atomSiteTable.getSelectedRows())
                model.remove(row);
        }
    }

    /** Icon for the add button. */
    private static final ImageIcon ADD_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-add_(22x22).png");

    /** Icon for the remove button. */
    private static final ImageIcon REMOVE_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/list-remove_(22x22).png");

    /** Icon for the calculation button. */
    private static final ImageIcon CALC_ICON =
            GuiUtil.loadIcon("ptpshared/data/icon/calc_(22x22).png");

    /** Field for the crystal's name. */
    private TextField nameField;

    /** Combo box for the crystal system. */
    private ComboBox<CrystalSystem> crystalSystemCBox;

    /** Combo box for the Laue group. */
    private ComboBox<LaueGroup> laueGroupCBox;

    /** Combo box for the space group. */
    private ComboBox<SpaceGroup> spaceGroupCBox;

    /** Field for the lattice constant a. */
    private DoubleField aField;

    /** Field for the lattice constant b. */
    private DoubleField bField;

    /** Field for the lattice constant c. */
    private DoubleField cField;

    /** Field for the lattice angle alpha. */
    private DoubleField alphaField;

    /** Field for the lattice angle beta. */
    private DoubleField betaField;

    /** Field for the lattice angle gamma. */
    private DoubleField gammaField;

    /** Table containing all the atoms. */
    private JTable atomSiteTable;

    /** Button to remove an atom from the table. */
    private Button removeButton;

    /** Button to calculate the equivalent atom sites positions. */
    private Button calcButton;

    /** Field for the atom's element symbol. */
    private TextField symbolField;

    /** Field for the atom's x position. */
    private DoubleField xField;

    /** Field for the atom's y position. */
    private DoubleField yField;

    /** Field for the atom's z position. */
    private DoubleField zField;

    /** Field for the atom's occupancy. */
    private DoubleField occupancyField;

    /** Button to add an atom to the table. */
    private Button addButton;



    /**
     * Creates a new <code>NewPhaseDialog</code>.
     */
    public NewPhaseDialog() {
        this(new Crystal("Untitled", new UnitCell(1, 1, 1, 1, 1, 1),
                new AtomSites(), SpaceGroups1.SG1));
    }



    /**
     * Creates a new <code>NewPhaseDialog</code> where the initial values are in
     * the specified crystal.
     * 
     * @param crystal
     *            initial crystal
     */
    public NewPhaseDialog(Crystal crystal) {
        super("New Phase");

        // Build the subpanel holding the combo boxes
        Panel crystalSystemPanel = new Panel(new MigLayout());

        crystalSystemPanel.add("Crystal's name");
        nameField = new TextField("Crystal's name", crystal.name);
        crystalSystemPanel.add(nameField, "wrap");

        crystalSystemPanel.add("Crystal system");
        crystalSystemCBox = new ComboBox<CrystalSystem>(CrystalSystem.values());
        crystalSystemCBox.setSelectedItem(crystal.spaceGroup.crystalSystem);
        crystalSystemCBox.addActionListener(new CrystalSystemCBoxListener());
        crystalSystemPanel.add(crystalSystemCBox, "wrap");

        crystalSystemPanel.add("Laue group");
        laueGroupCBox = new ComboBox<LaueGroup>(LaueGroup.values());
        laueGroupCBox.setSelectedItem(crystal.spaceGroup.laueGroup);
        laueGroupCBox.addActionListener(new LaueGroupCBoxListener());
        crystalSystemPanel.add(laueGroupCBox, "wrap");

        crystalSystemPanel.add("Space group");
        spaceGroupCBox = new ComboBox<SpaceGroup>();
        crystalSystemPanel.add(spaceGroupCBox, "wrap");

        // Build the Unit Cell subpanel
        Panel unitCellPanel = new Panel(new MigLayout());
        unitCellPanel.setBorder(new TitledBorder("Unit Cell"));

        unitCellPanel.add("a");
        aField = new DoubleField("Lattice constant a", crystal.unitCell.a);
        aField.setRange(1e-6, Double.MAX_VALUE);
        unitCellPanel.add(aField);
        unitCellPanel.add("\u212b"); // angstrom

        unitCellPanel.add("b");
        bField = new DoubleField("Lattice constant b", crystal.unitCell.b);
        bField.setRange(1e-6, Double.MAX_VALUE);
        unitCellPanel.add(bField);
        unitCellPanel.add("\u212b"); // angstrom

        unitCellPanel.add("c");
        cField = new DoubleField("Lattice constant c", crystal.unitCell.c);
        cField.setRange(1e-6, Double.MAX_VALUE);
        unitCellPanel.add(cField);
        unitCellPanel.add("\u212b", "wrap"); // angstrom

        unitCellPanel.add("\u03b1");
        alphaField =
                new DoubleField("Lattice angle alpha",
                        toDegrees(crystal.unitCell.alpha));
        alphaField.setRange(1e-6, 180);
        unitCellPanel.add(alphaField);
        unitCellPanel.add("\u00b0"); // deg

        unitCellPanel.add("\u03b2");
        betaField =
                new DoubleField("Lattice angle beta",
                        toDegrees(crystal.unitCell.beta));
        betaField.setRange(1e-6, 180);
        unitCellPanel.add(betaField);
        unitCellPanel.add("\u00b0"); // deg

        unitCellPanel.add("\u03b3");
        gammaField =
                new DoubleField("Lattice angle gamma",
                        toDegrees(crystal.unitCell.gamma));
        gammaField.setRange(1e-6, 180);
        unitCellPanel.add(gammaField);
        unitCellPanel.add("\u00b0"); // deg

        Panel atomSitesPanel = new Panel(new MigLayout());
        atomSitesPanel.setBorder(new TitledBorder(
                "Atom Sites (General Positions)"));

        AtomSiteTableModel model = new AtomSiteTableModel();
        for (AtomSite atom : crystal.atoms)
            model.append(atom);

        atomSiteTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(atomSiteTable);
        scrollPane.setPreferredSize(new Dimension(250, 150));
        atomSiteTable.setFillsViewportHeight(true);
        atomSitesPanel.add(scrollPane, "span 1 5");

        atomSitesPanel.add("Symbol:");
        symbolField = new TextField("Symbol", "");
        atomSitesPanel.add(symbolField, "wrap");

        atomSitesPanel.add("X:");
        xField = new DoubleField("X", 0); // no range
        atomSitesPanel.add(xField, "wrap");

        atomSitesPanel.add("Y:");
        yField = new DoubleField("Y", 0); // no range
        atomSitesPanel.add(yField, "wrap");

        atomSitesPanel.add("Z:");
        zField = new DoubleField("Z", 0); // no range
        atomSitesPanel.add(zField, "wrap");

        atomSitesPanel.add("Occupancy:");
        occupancyField = new DoubleField("Occupancy", 1);
        occupancyField.setRange(0, 1);
        atomSitesPanel.add(occupancyField, "wrap");

        calcButton = new Button(CALC_ICON);
        calcButton.setToolTipText("Calculate equivalent atoms positions");
        calcButton.setPlugIn(new CalculateAtomSites());
        atomSitesPanel.add(calcButton, "align right, split 2");

        removeButton = new Button(REMOVE_ICON);
        removeButton.setToolTipText("Remove selected atom");
        removeButton.setPlugIn(new Remove());
        atomSitesPanel.add(removeButton);

        addButton = new Button(ADD_ICON);
        addButton.setToolTipText("Add new atom");
        addButton.setPlugIn(new Add());
        atomSitesPanel.add(addButton, "span, align right");

        Panel cPanel = new Panel(new FlowLayout(FlowLayout.VERTICAL));
        cPanel.add(crystalSystemPanel);
        cPanel.add(unitCellPanel, "align left");
        cPanel.add(atomSitesPanel);

        setMainComponent(cPanel);

        refreshLaueGroup();
        spaceGroupCBox.setSelectedItem(crystal.spaceGroup);
    }



    /**
     * Returns the defined atom sites.
     * 
     * @return atom sites
     */
    private AtomSites getAtomSites() {
        return ((AtomSiteTableModel) atomSiteTable.getModel()).getAtomSites();
    }



    /**
     * Returns the defined crystal with the unit cell, atom sites and symmetry.
     * 
     * @return crystal
     */
    public Crystal getCrystal() {
        return new Crystal(nameField.getText(), getUnitCell(), getAtomSites(),
                getSpaceGroup());
    }



    /**
     * Returns the selected space group.
     * 
     * @return space group
     */
    private SpaceGroup getSpaceGroup() {
        return spaceGroupCBox.getSelectedItemBFR();
    }



    /**
     * Returns the defined unit cell.
     * 
     * @return unit cell
     */
    private UnitCell getUnitCell() {
        switch (crystalSystemCBox.getSelectedItem()) {
        case TRICLINIC:
            return UnitCellFactory.triclinic(aField.getValueBFR(),
                    bField.getValueBFR(), cField.getValueBFR(),
                    toRadians(alphaField.getValueBFR()),
                    toRadians(betaField.getValueBFR()),
                    toRadians(gammaField.getValueBFR()));
        case MONOCLINIC:
            return UnitCellFactory.monoclinic(aField.getValueBFR(),
                    bField.getValueBFR(), cField.getValueBFR(),
                    toRadians(betaField.getValueBFR()));
        case ORTHORHOMBIC:
            return UnitCellFactory.orthorhombic(aField.getValueBFR(),
                    bField.getValueBFR(), cField.getValueBFR());
        case TRIGONAL:
            return UnitCellFactory.trigonal(aField.getValueBFR(),
                    toRadians(alphaField.getValueBFR()));
        case TETRAGONAL:
            return UnitCellFactory.tetragonal(aField.getValueBFR(),
                    cField.getValueBFR());
        case HEXAGONAL:
            return UnitCellFactory.hexagonal(aField.getValueBFR(),
                    cField.getValueBFR());
        case CUBIC:
            return UnitCellFactory.cubic(aField.getValueBFR());
        default:
            throw new RuntimeException("Unknow crystal system.");
        }

    }



    @Override
    public boolean isCorrect() {
        if (!super.isCorrect())
            return false;

        if (!nameField.isCorrect())
            return false;
        if (nameField.getValue().length() == 0) {
            ErrorDialog.show("Please specify a name for the crystal.");
            return false;
        }

        return true;
    }



    /**
     * Refreshes the display of the dialog based on the crystal symmetry combo
     * box selection.
     */
    private void refreshLaueGroup() {
        if (crystalSystemCBox.getSelectedItem() == null)
            return;

        LaueGroup tmpLaueGroup = laueGroupCBox.getSelectedItem();

        switch (crystalSystemCBox.getSelectedItem()) {
        case TRICLINIC:
            aField.setEnabled(true);
            bField.setEnabled(true);
            cField.setEnabled(true);

            alphaField.setEnabled(true);
            betaField.setEnabled(true);
            gammaField.setEnabled(true);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LG1);

            break;
        case MONOCLINIC:
            aField.setEnabled(true);
            bField.setEnabled(true);
            cField.setEnabled(true);

            alphaField.setEnabled(false);
            alphaField.setValue(90);
            gammaField.setEnabled(false);
            gammaField.setValue(90);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LG2m);

            break;
        case ORTHORHOMBIC:
            aField.setEnabled(true);
            bField.setEnabled(true);
            cField.setEnabled(true);

            alphaField.setEnabled(false);
            alphaField.setValue(90);
            betaField.setEnabled(false);
            betaField.setValue(90);
            gammaField.setEnabled(false);
            gammaField.setValue(90);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LGmmm);

            break;
        case TRIGONAL:
            aField.setEnabled(true);
            bField.setEnabled(false);
            bField.setValue(Double.NaN);
            cField.setEnabled(false);
            cField.setValue(Double.NaN);

            alphaField.setEnabled(true);
            betaField.setEnabled(false);
            betaField.setValue(Double.NaN);
            gammaField.setEnabled(false);
            gammaField.setValue(Double.NaN);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LG3);
            laueGroupCBox.addGeneric(LG3m);

            break;
        case TETRAGONAL:
            aField.setEnabled(true);
            bField.setEnabled(false);
            bField.setValue(Double.NaN);
            cField.setEnabled(true);

            alphaField.setEnabled(false);
            alphaField.setValue(90);
            betaField.setEnabled(false);
            betaField.setValue(90);
            gammaField.setEnabled(false);
            gammaField.setValue(90);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LG4m);
            laueGroupCBox.addGeneric(LG4mmm);

            break;
        case HEXAGONAL:
            aField.setEnabled(true);
            bField.setEnabled(false);
            bField.setValue(Double.NaN);
            cField.setEnabled(true);

            alphaField.setEnabled(false);
            alphaField.setValue(90);
            betaField.setEnabled(false);
            betaField.setValue(90);
            gammaField.setEnabled(false);
            gammaField.setValue(120);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LG6m);
            laueGroupCBox.addGeneric(LG6mmm);

            break;
        case CUBIC:
            aField.setEnabled(true);
            bField.setEnabled(false);
            bField.setValue(Double.NaN);
            cField.setEnabled(false);
            cField.setValue(Double.NaN);

            alphaField.setEnabled(false);
            alphaField.setValue(90);
            betaField.setEnabled(false);
            betaField.setValue(90);
            gammaField.setEnabled(false);
            gammaField.setValue(90);

            laueGroupCBox.removeAllItems();
            laueGroupCBox.addGeneric(LGm3);
            laueGroupCBox.addGeneric(LGm3m);

            break;
        default:
            throw new RuntimeException("Unknown crystal system ("
                    + crystalSystemCBox.getSelectedItem() + ")");
        }

        laueGroupCBox.setSelectedItem(tmpLaueGroup);

        refreshSpaceGroup();
    }



    /**
     * Refreshes the space group combo box.
     */
    private void refreshSpaceGroup() {
        if (laueGroupCBox.getSelectedItem() == null)
            return;

        SpaceGroup tmpSpaceGroup = spaceGroupCBox.getSelectedItem();

        spaceGroupCBox.removeAllItems();

        SpaceGroup[] sgs = SpaceGroups.list(laueGroupCBox.getSelectedItem());
        Arrays.sort(sgs);
        for (SpaceGroup sg : sgs)
            spaceGroupCBox.addGeneric(sg);

        spaceGroupCBox.setSelectedItem(tmpSpaceGroup);
    }
}

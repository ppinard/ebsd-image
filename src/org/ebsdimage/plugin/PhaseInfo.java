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
package org.ebsdimage.plugin;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import net.miginfocom.swing.MigLayout;

import org.ebsdimage.core.PhaseMap;

import ptpshared.util.ElementProperties;
import rmlimage.core.Map;
import rmlimage.gui.PlugIn;
import rmlshared.gui.BasicDialog;
import rmlshared.gui.ComboBox;
import rmlshared.gui.Panel;
import crystallography.core.AtomSite;
import crystallography.core.AtomSites;
import crystallography.core.Crystal;
import static java.lang.Math.toDegrees;

/**
 * Plug-in to display the information about the phases in a
 * <code>PhasesMap</code>.
 * 
 * @author Philippe T. Pinard
 */
public class PhaseInfo extends PlugIn {

    /**
     * Dialog to display the information about the phases.
     * 
     * @author Philippe T. Pinard
     */
    private static class Dialog extends BasicDialog {

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
             * Removes all items in the table.
             */
            public void clear() {
                atomSites.clear();
                fireTableDataChanged();
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

        }

        /**
         * Listener of the crystal system combo box.
         */
        private class PhaseCBoxListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        }

        /** HashMap to access the phases information by their name. */
        private HashMap<String, Crystal> phases;

        /** Combo box field to select a phase. */
        private ComboBox<String> phaseCBox;

        /** Label for the crystal system. */
        private JLabel crystalSystemLabel;

        /** Label for the Laue group. */
        private JLabel laueGroupLabel;

        /** Label for the space group. */
        private JLabel spaceGroupLabel;

        /** Label for the lattice constant a. */
        private JLabel aLabel;

        /** Label for the lattice constant b. */
        private JLabel bLabel;

        /** Label for the lattice constant c. */
        private JLabel cLabel;

        /** Label for the lattice constant alpha. */
        private JLabel alphaLabel;

        /** Label for the lattice constant beta. */
        private JLabel betaLabel;

        /** Label for the lattice constant gamma. */
        private JLabel gammaLabel;

        /** Table containing all the atoms. */
        private JTable atomSiteTable;



        /**
         * Creates a new <code>Dialog</code> to display the info about the
         * specified phases.
         * 
         * @param phases
         *            array of phase
         */
        public Dialog(Crystal[] phases) {
            super("Phases Info");

            if (phases.length == 0)
                throw new IllegalArgumentException(
                        "At least one phase must be defined.");

            this.phases = new HashMap<String, Crystal>();
            for (int i = 0; i < phases.length; i++) {
                this.phases.put((i + 1) + " - " + phases[i].name, phases[i]);
            }

            // Panel
            Panel panel = new Panel(new MigLayout());

            String[] phasesName = this.phases.keySet().toArray(new String[0]);
            Arrays.sort(phasesName);
            panel.add("Phase:");
            phaseCBox = new ComboBox<String>(phasesName);
            phaseCBox.setSelectedItem(phasesName[0]);
            phaseCBox.addActionListener(new PhaseCBoxListener());
            panel.add(phaseCBox, "wrap");

            panel.add("Crystal system:");
            crystalSystemLabel = new JLabel();
            panel.add(crystalSystemLabel, "wrap");

            panel.add("Laue group:");
            laueGroupLabel = new JLabel();
            panel.add(laueGroupLabel, "wrap");

            panel.add("Space group:");
            spaceGroupLabel = new JLabel();
            panel.add(spaceGroupLabel, "wrap");

            // Build the Unit Cell subpanel
            Panel unitCellPanel = new Panel(new MigLayout());
            unitCellPanel.setBorder(new TitledBorder("Unit Cell"));

            unitCellPanel.add("a:");
            aLabel = new JLabel();
            unitCellPanel.add(aLabel);
            unitCellPanel.add("\u212b"); // angstrom

            unitCellPanel.add("b:");
            bLabel = new JLabel();
            unitCellPanel.add(bLabel);
            unitCellPanel.add("\u212b"); // angstrom

            unitCellPanel.add("c:");
            cLabel = new JLabel();
            unitCellPanel.add(cLabel);
            unitCellPanel.add("\u212b", "wrap"); // angstrom

            unitCellPanel.add("\u03b1:");
            alphaLabel = new JLabel();
            unitCellPanel.add(alphaLabel);
            unitCellPanel.add("\u00b0"); // deg

            unitCellPanel.add("\u03b2:");
            betaLabel = new JLabel();
            unitCellPanel.add(betaLabel);
            unitCellPanel.add("\u00b0"); // deg

            unitCellPanel.add("\u03b3:");
            gammaLabel = new JLabel();
            unitCellPanel.add(gammaLabel);
            unitCellPanel.add("\u00b0"); // deg

            panel.add(unitCellPanel, "span 2, wrap");

            Panel atomSitesPanel = new Panel(new MigLayout());
            atomSitesPanel.setBorder(new TitledBorder("Atom Sites"));

            atomSiteTable = new JTable(new AtomSiteTableModel());
            JScrollPane scrollPane = new JScrollPane(atomSiteTable);
            scrollPane.setPreferredSize(new Dimension(250, 150));
            atomSiteTable.setFillsViewportHeight(true);
            atomSitesPanel.add(scrollPane);

            panel.add(atomSitesPanel, "span 2");

            setMainComponent(panel);

            refresh();
        }



        /**
         * Refresh the information in this dialog.
         */
        private void refresh() {
            Crystal phase = phases.get(phaseCBox.getSelectedItem());

            crystalSystemLabel.setText(phase.spaceGroup.crystalSystem.getLabel());
            laueGroupLabel.setText(phase.spaceGroup.laueGroup.getLabel());
            spaceGroupLabel.setText(phase.spaceGroup.symbol);

            aLabel.setText(Double.toString(phase.unitCell.a));
            bLabel.setText(Double.toString(phase.unitCell.b));
            cLabel.setText(Double.toString(phase.unitCell.c));
            alphaLabel.setText(Double.toString(toDegrees(phase.unitCell.alpha)));
            betaLabel.setText(Double.toString(toDegrees(phase.unitCell.beta)));
            gammaLabel.setText(Double.toString(toDegrees(phase.unitCell.gamma)));

            AtomSiteTableModel model =
                    (AtomSiteTableModel) atomSiteTable.getModel();
            model.clear();
            for (AtomSite atom : phase.atoms)
                model.append(atom);
        }
    }



    /**
     * Display the phases information of the selected <code>PhasesMap</code>.
     */
    private void doInfo() {
        if (!areMapsLoaded(true))
            return;

        Map map = getSelectedMap();

        if (!isCorrectType(map, PhaseMap.class, true))
            return;

        PhaseMap phasesMap = (PhaseMap) map;

        if (phasesMap.getPhases().length == 0) {
            showErrorDialog("No phase in the map.");
            return;
        }

        Dialog dialog = new Dialog(phasesMap.getPhases());

        dialog.show();
    }



    @Override
    protected void xRun() throws Exception {
        doInfo();
    }

}

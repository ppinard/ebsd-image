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
package crystallography.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Logger;

import ptpshared.util.ElementProperties;
import rmlshared.io.FileUtil;
import rmlshared.io.Loader;
import rmlshared.ui.Monitorable;
import bsh.EvalError;
import bsh.Interpreter;
import crystallography.core.*;

/**
 * A special tokenizer class for dealing with quoted strings in CIF files.
 * regarding the treatment of single quotes vs. primes in cif file, PMR wrote:
 * The CIF format was developed in that late 1980's under the aegis of the
 * International Union of Crystallography (I am a consultant to the COMCIFs
 * committee). It was ratified by the Union and there have been several
 * workshops. mmCIF is an extension of CIF which includes a relational
 * structure. The formal publications are: Hall, S. R. (1991). "The STAR File: A
 * New Format for Electronic Data Transfer and Archiving", J. Chem. Inform.
 * Comp. Sci., 31, 326-333. Hall, S. R., Allen, F. H. and Brown, I. D. (1991).
 * "The Crystallographic Information File (CIF): A New Standard Archive File for
 * Crystallography", Acta Cryst., A47, 655-685. Hall, S.R. & Spadaccini, N.
 * (1994). "The STAR File: Detailed Specifications," J. Chem. Info. Comp. Sci.,
 * 34, 505-508.
 * 
 * @author Jmol: an open-source Java viewer for chemical structures in 3D.
 *         http://www.jmol.org/
 */
public class CifLoader implements Loader, Monitorable {

    /** Complete data from the cif. */
    private Hashtable<String, Vector<Hashtable<String, Object>>> allData;

    /** Buffer to read cif file. */
    private BufferedReader br;

    /** Parsing variable. */
    private int cch;

    /** Data from the cif. */
    private Hashtable<String, Object> data;

    /** Parsing variable. */
    private int fieldCount;

    /** Parsing variable. */
    private int ich;

    /** Parsing variable. */
    private int ichPeeked;

    /** String interpreter to parse mathematical expression. */
    private final Interpreter interpreter = new Interpreter();

    /** Parsing variable. */
    private String line;

    /** Parsing variable. */
    private String[] loopData;

    /** Track progress. */
    private double progress = 0.0;

    /** Track progress's status. */
    private String status = "";

    /** Parsing variable. */
    private String str;

    /** Parsing variable. */
    private String strPeeked;

    /** Parsing variable. */
    private boolean wasUnQuoted;



    @Override
    public boolean canLoad(File file) {
        return FileUtil.isExtension(file, "cif");
    }



    /**
     * Reads all Cif Data from a reader.
     * 
     * @return Hashtable of models Vector of Hashtable data
     */
    private Hashtable<String, Vector<Hashtable<String, Object>>> getAllCifData() {
        line = "";
        String key;
        allData = new Hashtable<String, Vector<Hashtable<String, Object>>>();
        Vector<Hashtable<String, Object>> models =
                new Vector<Hashtable<String, Object>>();
        allData.put("models", models);
        try {
            while ((key = getNextToken()) != null) {
                if (key.startsWith("global_") || key.startsWith("data_")) {
                    data = new Hashtable<String, Object>();
                    models.add(data);
                    data.put("name", key);
                    continue;
                }
                if (key.startsWith("loop_")) {
                    getCifLoopData();
                    continue;
                }
                if (key.indexOf("_") != 0) {
                    Logger.getLogger("ebsd").info(
                            "CIF ERROR ? should be an underscore: " + key);
                } else {
                    String value = getNextToken();
                    if (value == null) {
                        Logger.getLogger("ebsd").info(
                                "CIF ERROR ? end of file; data missing: " + key);
                    } else {
                        data.put(key, value);
                    }
                }
            }
        } catch (Exception e) {
            // ?
        }
        try {
            if (br != null)
                br.close();
        } catch (Exception e) {
            // ?
        }

        return allData;
    }



    /**
     * Returns the atomic number from the label string in the cif.
     * 
     * @param label
     *            label/symbol of an element
     * @return atomic number
     */
    private int getAtomicNumber(String label) {
        String newSymbol = String.valueOf(label.charAt(0));

        int charVal = label.charAt(1);
        if (charVal >= 97 && charVal <= 122)
            newSymbol += (char) charVal;

        return ElementProperties.getAtomicNumber(newSymbol);
    }



    /**
     * Get the data from a loop in a cif.
     * 
     * @throws Exception
     *             if an error occurs.
     */
    @SuppressWarnings("unchecked")
    private void getCifLoopData() throws Exception {
        String str;
        Vector<String> keyWords = new Vector<String>();
        while ((str = peekToken()) != null && str.charAt(0) == '_') {
            str = getTokenPeeked();
            keyWords.add(str);
            data.put(str, new Vector<String>());
        }
        fieldCount = keyWords.size();
        if (fieldCount == 0)
            return;
        loopData = new String[fieldCount];
        while (getData()) {
            for (int i = 0; i < fieldCount; i++) {
                ((Vector<String>) data.get(keyWords.get(i))).add(loopData[i]);
            }
        }
    }



    /**
     * Returns the coordinate of a position from the specified symmetrically
     * equivalent position expression. The expression can contain symbols such
     * as +, -, *, / and the variables x,y and z. It is evaluated with the
     * values of x, y and z of the given atom positions.
     * 
     * @param posText
     *            string of the coordinate
     * @param atomPosX
     *            x value for an atom position
     * @param atomPosY
     *            y value for an atom position
     * @param atomPosZ
     *            z value for an atom position
     * @param index
     *            index of the position
     * @return coordinate of a position
     * @throws IOException
     *             if an error occurs while evaluating the expression of the
     *             position
     */
    private double getCoord(String posText, String[] atomPosX,
            String[] atomPosY, String[] atomPosZ, int index) throws IOException {
        posText =
                posText.replace("x", atomPosX[index]).replace("y",
                        atomPosY[index]).replace("z", atomPosZ[index]);

        try {
            return (Double) interpreter.eval(posText);
        } catch (EvalError e) {
            throw new IOException(e);
        }
    }



    /**
     * Returns a <code>Crystal</code> from the data in the cif.
     * 
     * @param allData
     *            parsed data of the cif
     * @return a <code>Crystal</code>
     * @throws IOException
     *             if an error occurs while parsing the data
     */
    private Crystal getCrystal(
            Hashtable<String, Vector<Hashtable<String, Object>>> allData)
            throws IOException {
        Hashtable<String, Object> data = allData.get("models").firstElement();

        // Check key in cif
        if (!data.containsKey("_chemical_name_mineral"))
            throw new IOException("Lattice parameter a is missing.");
        if (!data.containsKey("_cell_length_a"))
            throw new IOException("Lattice parameter a is missing.");
        if (!data.containsKey("_cell_length_b"))
            throw new IOException("Lattice parameter b is missing.");
        if (!data.containsKey("_cell_length_c"))
            throw new IOException("Lattice parameter c is missing.");
        if (!data.containsKey("_cell_angle_alpha"))
            throw new IOException("Lattice angle alpha is missing.");
        if (!data.containsKey("_cell_angle_beta"))
            throw new IOException("Lattice angle beta is missing.");
        if (!data.containsKey("_cell_angle_gamma"))
            throw new IOException("Lattice angle gamma is missing.");
        if (!data.containsKey("_atom_site_label"))
            throw new IOException("Atom site symbol (label) is missing.");
        if (!data.containsKey("_atom_site_fract_x"))
            throw new IOException("X coodinates of atom sites is missing.");
        if (!data.containsKey("_atom_site_fract_y"))
            throw new IOException("Y coodinates of atom sites is missing.");
        if (!data.containsKey("_atom_site_fract_z"))
            throw new IOException("Z coodinates of atom sites is missing.");
        if (!(data.containsKey("_space_group_IT_number") || data.containsKey("_symmetry_Int_Tables_number")))
            throw new IOException("Space group number is missing.");

        // Name
        String name = (String) data.get("_chemical_name_mineral");

        // Unit cell
        double a = Double.parseDouble((String) data.get("_cell_length_a"));
        double b = Double.parseDouble((String) data.get("_cell_length_b"));
        double c = Double.parseDouble((String) data.get("_cell_length_c"));
        double alpha =
                Math.toRadians(Double.parseDouble((String) data.get("_cell_angle_alpha")));
        double beta =
                Math.toRadians(Double.parseDouble((String) data.get("_cell_angle_beta")));
        double gamma =
                Math.toRadians(Double.parseDouble((String) data.get("_cell_angle_gamma")));
        UnitCell unitCell = new UnitCell(a, b, c, alpha, beta, gamma);

        // Atom sites
        AtomSites atomSites = new AtomSites();

        ArrayList<String[]> symmetryEquivPositions = new ArrayList<String[]>();

        String[] positions;
        if (data.containsKey("_symmetry_equiv_pos_as_xyz"))
            for (Object pos : ((Vector<?>) data.get("_symmetry_equiv_pos_as_xyz"))) {
                positions = ((String) pos).split(",");

                if (positions.length != 3)
                    throw new IOException(
                            "Error while parsing symmetry equivalent position ("
                                    + pos.toString() + ").");

                symmetryEquivPositions.add(positions);
            }
        else
            symmetryEquivPositions.add(new String[] { "x", "y", "z" });

        String[] atomPosX =
                ((Vector<?>) data.get("_atom_site_fract_x")).toArray(new String[0]);
        String[] atomPosY =
                ((Vector<?>) data.get("_atom_site_fract_y")).toArray(new String[0]);
        String[] atomPosZ =
                ((Vector<?>) data.get("_atom_site_fract_z")).toArray(new String[0]);
        String[] atomLabels =
                ((Vector<?>) data.get("_atom_site_label")).toArray(new String[0]);

        String[] atomOccupancies;
        if (data.containsKey("_atom_site_occupancy"))
            atomOccupancies =
                    ((Vector<?>) data.get("_atom_site_occupancy")).toArray(new String[0]);
        else {
            atomOccupancies = new String[atomPosX.length];
            Arrays.fill(atomOccupancies, "1.0");
        }

        if (atomPosX.length != atomPosY.length
                || atomPosX.length != atomPosZ.length
                || atomPosX.length != atomLabels.length
                || atomPosX.length != atomOccupancies.length)
            throw new IOException(
                    "The number of coordinates in X, Y and Z doesn't match the number of symbols.");

        int atomicNumber;
        double x, y, z, occupancy;
        for (int i = 0; i < atomPosX.length; i++) {
            atomicNumber = getAtomicNumber(atomLabels[i]);
            occupancy = Double.parseDouble(atomOccupancies[i]);

            for (String[] pos : symmetryEquivPositions) {
                x = getCoord(pos[0], atomPosX, atomPosY, atomPosZ, i);
                y = getCoord(pos[1], atomPosX, atomPosY, atomPosZ, i);
                z = getCoord(pos[2], atomPosX, atomPosY, atomPosZ, i);

                try {
                    atomSites.add(new AtomSite(atomicNumber, x, y, z, occupancy));
                } catch (AtomSitePositionException ex) {
                    // Pass
                }
            }
        }

        // Point group
        String indexStr = (String) data.get("_space_group_IT_number");
        if (indexStr == null)
            indexStr = (String) data.get("_symmetry_Int_Tables_number");

        int index = Integer.parseInt(indexStr);
        SpaceGroup sg = SpaceGroups.fromIndex(index);

        return new Crystal(name, unitCell, atomSites, sg);
    }



    /**
     * General reader for loop data fills loopData with fieldCount fields.
     * 
     * @return false if EOF
     * @throws Exception
     *             if an error occurs
     */
    private boolean getData() throws Exception {
        // line is already present, and we leave with the next line to parse
        for (int i = 0; i < fieldCount; ++i) {
            loopData[i] = getNextDataToken();
            if (loopData[i] == null)
                return false;
        }
        return true;
    }



    /**
     * First checks to see if the next token is an unquoted control code, and if
     * so, returns null.
     * 
     * @return next data token or null
     * @throws Exception
     *             if an error occurs
     */
    private String getNextDataToken() throws Exception {
        String str = peekToken();
        if (str == null)
            return null;
        if (wasUnQuoted)
            if (str.charAt(0) == '_' || str.startsWith("loop_")
                    || str.startsWith("data_") || str.startsWith("stop_")
                    || str.startsWith("global_"))
                return null;
        return getTokenPeeked();
    }



    /**
     * Returns the next token of any kind.
     * 
     * @return the next token of any kind, or null
     * @throws Exception
     *             if an error occurs
     */
    private String getNextToken() throws Exception {
        while (!hasMoreTokens())
            if (setStringNextLine() == null)
                return null;
        return nextToken();
    }



    @Override
    public double getTaskProgress() {
        return progress;
    }



    @Override
    public String getTaskStatus() {
        return status;
    }



    /**
     * Returns the token last acquired.
     * 
     * @return the token last acquired; may be null
     */
    private String getTokenPeeked() {
        ich = ichPeeked;
        return strPeeked;
    }



    /**
     * Return if there is more available tokens.
     * 
     * @return <code>true</code> if there are more tokens in the line buffer
     */
    private boolean hasMoreTokens() {
        if (str == null)
            return false;
        char ch = '#';
        while (ich < cch && ((ch = str.charAt(ich)) == ' ' || ch == '\t'))
            ++ich;
        return (ich < cch && ch != '#');
    }



    @Override
    public Crystal load(File file) throws IOException {
        if (!canLoad(file))
            throw new IOException(
                    "Invalid type of file. The extension must be cif.");

        br = new BufferedReader(new FileReader(file));

        progress = 0.0;
        status = "Parsing cif file...";

        Hashtable<String, Vector<Hashtable<String, Object>>> allData =
                getAllCifData();

        progress = 0.5;
        status = "Creating crystal...";

        Crystal crystal = getCrystal(allData);

        progress = 1.0;
        status = "Completed";
        br = null;

        return crystal;
    }



    @Override
    public Crystal load(File file, Object obj) throws IOException {
        return load(file);
    }



    /**
     * assume that hasMoreTokens() has been called and that ich is pointing at a
     * non-white character. Also sets boolean wasUnQuoted, because we need to
     * know if we should be checking for a control keyword. 'loop_' is different
     * from just loop_ without the quotes.
     * 
     * @return null if no more tokens, "\0" if '.' or '?', or next token
     */
    private String nextToken() {
        if (ich == cch)
            return null;
        int ichStart = ich;
        char ch = str.charAt(ichStart);
        if (ch != '\'' && ch != '"' && ch != '\1') {
            wasUnQuoted = true;
            while (ich < cch && (ch = str.charAt(ich)) != ' ' && ch != '\t')
                ++ich;
            if (ich == ichStart + 1)
                if (str.charAt(ichStart) == '.' || str.charAt(ichStart) == '?')
                    return "\0";
            return str.substring(ichStart, ich);
        }
        wasUnQuoted = false;
        char chOpeningQuote = ch;
        boolean previousCharacterWasQuote = false;
        while (++ich < cch) {
            ch = str.charAt(ich);
            if (previousCharacterWasQuote && (ch == ' ' || ch == '\t'))
                break;
            previousCharacterWasQuote = (ch == chOpeningQuote);
        }
        if (ich == cch) {
            if (previousCharacterWasQuote) // close quote was last char of
                // string
                return str.substring(ichStart + 1, ich - 1);
            // reached the end of the string without finding closing '
            return str.substring(ichStart, ich);
        }
        ++ich; // throw away the last white character
        return str.substring(ichStart + 1, ich - 2);
    }



    /**
     * Just look at the next token. Saves it for retrieval using
     * getTokenPeeked()
     * 
     * @return next token or null if EOF
     * @throws Exception
     *             if an error occurs
     */
    private String peekToken() throws Exception {
        while (!hasMoreTokens())
            if (setStringNextLine() == null)
                return null;
        int ich = this.ich;
        strPeeked = nextToken();
        ichPeeked = this.ich;
        this.ich = ich;
        return strPeeked;
    }



    /**
     * Read the next line from the bufferer reader.
     * 
     * @return next line
     */
    private String readLine() {
        try {
            line = br.readLine();
            return line;
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * Sets a string to be parsed from the beginning.
     * 
     * @param str
     *            string to set;
     */
    private void setString(String str) {
        this.str = str;
        line = str;
        cch = (str == null ? 0 : str.length());
        ich = 0;
    }



    /**
     * Sets the string for parsing to be from the next line when the token
     * buffer is empty, and if ';' is at the beginning of that line, extends the
     * string to include that full multiline string. Uses \1 to indicate that
     * this is a special quotation.
     * 
     * @return the next line or null if EOF
     * @throws Exception
     *             if an error occurs
     */
    private String setStringNextLine() throws Exception {
        setString(readLine());
        if (line == null || line.length() == 0 || line.charAt(0) != ';')
            return line;
        ich = 1;
        String str = '\1' + line.substring(1) + '\n';
        while (readLine() != null) {
            if (line.startsWith(";")) {
                // remove trailing <eol> only, and attach rest of next line
                str =
                        str.substring(0, str.length() - 1) + '\1'
                                + line.substring(1);
                break;
            }
            str += line + '\n';
        }
        setString(str);
        return str;
    }
}
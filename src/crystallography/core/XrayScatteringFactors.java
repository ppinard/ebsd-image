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
package crystallography.core;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import ptpshared.util.ElementProperties;
import rmlshared.io.CsvReader;
import static rmlshared.io.FileUtil.getURL;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

/**
 * Scattering factors for x-rays diffraction. The scattering factors are
 * calculated from an exponential fit of the scattering factors curves. The
 * values of the fitting coefficients were taken from the International
 * Crystallography Tables. See the methods {@link #getFromPlaneSpacing} and
 * {@link #getFromS} to obtain the scattering factor of a given atomic number
 * and plane spacing.
 * <p/>
 * <b>References:</b>
 * <ul>
 * <li>International Crystallography Tables, volume C, Tables 6.1.1.4 and
 * 6.1.1.5</li>
 * </ul>
 * 
 * @author Philippe T. Pinard
 */
public class XrayScatteringFactors extends ScatteringFactors {

    /**
     * Creates a new <code>XrayScatteringFactors</code> and read the fitting
     * coefficients from the csv files.
     * 
     * @throws RuntimeException
     *             if one csv file cannot be read
     */
    public XrayScatteringFactors() {
        read02();
        read26();
    }



    /**
     * Calculates the scattering factor for values of s between 0 and 2.
     * 
     * @param atomicNumber
     *            atomic number of the element
     * @param s
     *            fitting variable
     * @return scattering factor
     */
    private double calculateScatteringFactor02(int atomicNumber, double s) {
        HashMap<String, Double> coeffs = coefficients02.get(atomicNumber);

        double[] a =
                { coeffs.get("a1"), coeffs.get("a2"), coeffs.get("a3"),
                        coeffs.get("a4") };
        double[] b =
                { coeffs.get("b1"), coeffs.get("b2"), coeffs.get("b3"),
                        coeffs.get("b4") };
        double c = coeffs.get("c");

        // Calculate factor
        double f = 0.0;

        for (int i = 0; i < 4; i++)
            f += a[i] * exp(-b[i] * pow(s, 2));

        f += c;

        return f;
    }



    /**
     * Calculates the scattering factor for values of s between 2 and 6.
     * 
     * @param atomicNumber
     *            atomic number of the element
     * @param s
     *            fitting variable
     * @return scattering factor
     */
    private double calculateScatteringFactor26(int atomicNumber, double s) {
        HashMap<String, Double> coeffs = coefficients26.get(atomicNumber);

        double[] a =
                { coeffs.get("a0"), coeffs.get("a1"), coeffs.get("a2") / 10.0,
                        coeffs.get("a3") / 100.0 };

        // Calculate factor
        double f = 0.0;

        for (int i = 0; i < 4; i++)
            f += a[i] * pow(s, i);

        f = exp(f);

        return f;
    }



    /**
     * Returns the scattering factor for the given atomic number and plane
     * spacing.
     * <p/>
     * The variable s used in the calculations is defined as
     * <code>s = sin(theta) / lambda</code>.
     * <p/>
     * The values are limited for 0 < s < 6 A^{-1}. A warning is returned is s
     * exceeds these limits.
     * <p/>
     * More easily it can be calculated from the plane spacing (d)
     * <code>s = 1 / (2d)</code>.
     * <p/>
     * For the plane spacing the values are limited for d > 1.047 angstroms.
     * 
     * @param atomicNumber
     *            atomic number of the atom
     * @param planeSpacing
     *            spacing of a given plane in angstroms
     * @return scattering factor
     */
    @Override
    public double getFromPlaneSpacing(int atomicNumber, double planeSpacing) {
        double s = 1.0 / (2.0 * planeSpacing);
        return getFromS(atomicNumber, s);
    }



    /**
     * Return the scattering factor for the given atomic number and the variable
     * s.
     * <p/>
     * The variable s used in the calculations is defined as
     * <code>s = sin(theta) / lambda</code>.
     * <p/>
     * The values are limited for 0 < s < 6 A^{-1}. A warning is returned is s
     * exceeds these limits.
     * 
     * @param atomicNumber
     *            atomic number of the atom
     * @param s
     *            fitting variable
     * @return scattering factors
     */
    @Override
    public double getFromS(int atomicNumber, double s) {
        // For s between 0 and 2.
        if (s >= 0 && s < 2)
            return calculateScatteringFactor02(atomicNumber, s);
        else if (s >= 2 && s < 6)
            return calculateScatteringFactor26(atomicNumber, s);
        else {
            Logger logger = Logger.getLogger("crystallography");

            logger.warning("Outside table range of s (" + s + ") < 6 angstroms");

            return calculateScatteringFactor26(atomicNumber, s);
        }
    }



    /**
     * Reads the csv file with values between <code>s=0</code> to
     * <code>s=2</code> angstroms.
     * 
     * @throws RuntimeException
     *             if the file cannot be read
     */
    private void read02() {
        URL url =
                getURL("crystallography/data/xray_scattering_factors_0_2.csv");

        try {
            CsvReader csvFile = new CsvReader(url);

            // Skip header
            csvFile.skipLine();

            while (true) {
                String[] line = csvFile.readLine();
                if (line == null)
                    break; // end of lines

                // Get the value for the atomic number and charge
                int atomicNumber = readAtomicNumber(line);
                int charge = readCharge(line);

                // Only consider atom with a zero charge
                if (charge != 0)
                    continue;

                // Get coefficients
                HashMap<String, Double> coefficients =
                        new HashMap<String, Double>();
                coefficients.put("a1", Double.parseDouble(line[3]));
                coefficients.put("b1", Double.parseDouble(line[4]));
                coefficients.put("a2", Double.parseDouble(line[5]));
                coefficients.put("b2", Double.parseDouble(line[6]));
                coefficients.put("a3", Double.parseDouble(line[7]));
                coefficients.put("b3", Double.parseDouble(line[8]));
                coefficients.put("a4", Double.parseDouble(line[9]));
                coefficients.put("b4", Double.parseDouble(line[10]));
                coefficients.put("c", Double.parseDouble(line[11]));

                coefficients02.put(atomicNumber, coefficients);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }



    /**
     * Reads the csv file with values between <code>s=2</code> to
     * <code>s=6</code> angstroms.
     * 
     * @throws RuntimeException
     *             if the file cannot be read
     */
    private void read26() {
        URL url =
                getURL("crystallography/data/xray_scattering_factors_2_6.csv");

        try {
            CsvReader csvFile = new CsvReader(url);

            // Skip header
            csvFile.skipLine();

            while (true) {
                String[] line = csvFile.readLine();
                if (line == null)
                    break; // end of lines

                // Get the value for the atomic number and charge
                int atomicNumber = readAtomicNumber(line);
                int charge = readCharge(line);

                // Only consider atom with a zero charge
                if (charge != 0)
                    continue;

                // Get coefficients
                HashMap<String, Double> coefficients =
                        new HashMap<String, Double>();
                coefficients.put("a0", Double.parseDouble(line[3]));
                coefficients.put("a1", Double.parseDouble(line[4]));
                coefficients.put("a2", Double.parseDouble(line[5]));
                coefficients.put("a3", Double.parseDouble(line[6]));

                coefficients26.put(atomicNumber, coefficients);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }



    /**
     * Returns the atomic number of the line.
     * 
     * @param line
     *            line in the csv file
     * @return atomic number
     */
    private int readAtomicNumber(String[] line) {
        String symbol = line[0].trim();
        int atomicNumber = ElementProperties.getAtomicNumber(symbol);
        return atomicNumber;
    }



    /**
     * Returns the charge of the line.
     * 
     * @param line
     *            line in the csv file
     * @return charge
     */
    private int readCharge(String[] line) {
        String charge = line[1].trim();
        if ("val".equals(charge))
            return -1;
        else
            return (int) Double.parseDouble(charge);
    }
}

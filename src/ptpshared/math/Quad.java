package ptpshared.math;

import static java.lang.Math.*;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.integration.UnivariateRealIntegratorImpl;

/**
 * Implementation in Java of the QUADPACK (only for bounded integrals). Code
 * taken from <i>riso numerical</i> project (http://riso.cvs.sourceforge.net)
 * and reformat to use Apache common match interface. Documentation is taken
 * from the original Fortran QUADPACK code. Default values for the accuracy and
 * limit are taken from the QUADPACK implementation in the Python module scipy.
 * 
 * @author Robert Piessens
 * @author Elise de Doncker-Kapenger
 * @author Christian Ueberhuber
 * @author David Kahaner
 * @author Robert Dodier
 */
public class Quad extends UnivariateRealIntegratorImpl {

    /** The abscissae and weights are given for the interval (-1,1). */
    private static final double[] D1MACH = { Double.MIN_VALUE,
            Double.MAX_VALUE, pow(2, -52), pow(2, -51), log(2) / log(10) };



    /**
     * QAGE estimates a definite integral.
     * 
     * @param f
     *            integrand function
     * @param a
     *            lower limit of integration
     * @param b
     *            upper limit of integration
     * @param epsabs
     *            the absolute accuracy requested
     * @param epsrel
     *            the relative accuracy requested
     * @param limit
     *            the maximum number of subintervals that can be used
     * @param result
     *            the estimated value of the integral
     * @param abserr
     *            an estimate of || I - RESULT ||
     * @param neval
     *            the number of times the integral was evaluated
     * @param ier
     *            return code
     * @param alist
     *            list of left end points of all subintervals considered up to
     *            now
     * @param blist
     *            list of right end points of all subintervals considered up to
     *            now
     * @param rlist
     *            contains in entries 1 through LAST the integral approximations
     *            on the subintervals
     * @param elist
     *            error estimate applying to rlist(i)
     * @param iord
     *            the first K elements of which are pointers to the error
     *            estimates over the subintervals, such that elist(iord(1)),
     *            ..., elist(iord(k)) form a decreasing sequence, with k = last
     *            if last <= (limit/2+2), and k = limit+1-last otherwise.
     * @param last
     *            index for subdivision
     * @throws FunctionEvaluationException
     *             if an error occurs while evaluating the integrand function
     */
    private static void doQAGE(UnivariateRealFunction f, double a, double b,
            double epsabs, double epsrel, int limit, double[] result,
            double[] abserr, int[] neval, int[] ier, double[] alist,
            double[] blist, double[] rlist, double[] elist, int[] iord,
            int[] last) throws FunctionEvaluationException {
        double area, area12, a1, a2, b1, b2, correc = -999, dres, epmach, erlarg =
                -999, erlast, errbnd, erro12, errsum, ertest = -999, oflow, small =
                -999, uflow;
        double[] res3la = new double[3], rlist2 = new double[52];
        double[] defabs = new double[1], resabs = new double[1];
        double[] area1 = new double[1], area2 = new double[1];
        double[] error1 = new double[1], error2 = new double[1];
        double[] defab1 = new double[1], defab2 = new double[1];
        double[] reseps = new double[1], abseps = new double[1];
        double[] errmax = new double[1];
        int id, ierro, iroff1, iroff2, iroff3, jupbnd, k, ksgn, ktmin;
        int[] maxerr = new int[1], nrmax = new int[1], numrl2 = new int[1], nres =
                new int[1];
        boolean extrap, noext;
        epmach = D1MACH[4 - 1];
        ier[0] = 0;
        neval[0] = 0;
        last[0] = 0;
        result[0] = 0;
        abserr[0] = 0;
        alist[1 - 1] = a;
        blist[1 - 1] = b;
        rlist[1 - 1] = 0;
        elist[1 - 1] = 0;
        if (epsabs <= 0 && epsrel < max(50 * epmach, 0.5e-28))
            ier[0] = 6;
        if (ier[0] == 6)
            return;
        uflow = D1MACH[1 - 1];
        oflow = D1MACH[2 - 1];
        ierro = 0;
        doQK21(f, a, b, result, abserr, defabs, resabs);
        dres = abs(result[0]);
        errbnd = max(epsabs, epsrel * dres);
        last[0] = 1;
        rlist[1 - 1] = result[0];
        elist[1 - 1] = abserr[0];
        iord[1 - 1] = 1;
        if (abserr[0] <= 100 * epmach * defabs[0] && abserr[0] > errbnd)
            ier[0] = 2;
        if (limit == 1)
            ier[0] = 1;
        if (ier[0] != 0 || (abserr[0] <= errbnd && abserr[0] != resabs[0])
                || abserr[0] == 0) {
            neval[0] = 42 * last[0] - 21;
            // System.err.println(
            // "do_qagse: return after 1st qk21; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
            // );
            return;
        }
        rlist2[1 - 1] = result[0];
        errmax[0] = abserr[0];
        maxerr[0] = 1;
        area = result[0];
        errsum = abserr[0];
        abserr[0] = oflow;
        nrmax[0] = 1;
        nres[0] = 0;
        numrl2[0] = 2;
        ktmin = 0;
        extrap = false;
        noext = false;
        iroff1 = 0;
        iroff2 = 0;
        iroff3 = 0;
        ksgn = -1;
        if (dres >= (1 - 50 * epmach) * defabs[0])
            ksgn = 1;
        for (last[0] = 2; last[0] <= limit; last[0] += 1) {
            a1 = alist[maxerr[0] - 1];
            b1 = 0.5 * (alist[maxerr[0] - 1] + blist[maxerr[0] - 1]);
            a2 = b1;
            b2 = blist[maxerr[0] - 1];
            erlast = errmax[0];
            doQK21(f, a1, b1, area1, error1, resabs, defab1);
            doQK21(f, a2, b2, area2, error2, resabs, defab2);
            area12 = area1[0] + area2[0];
            erro12 = error1[0] + error2[0];
            errsum = errsum + erro12 - errmax[0];
            area = area + area12 - rlist[maxerr[0] - 1];
            if (!(defab1[0] == error1[0] || defab2[0] == error2[0])) {
                if (!(abs(rlist[maxerr[0] - 1] - area12) > 1e-5 * abs(area12) || erro12 < 0.99 * errmax[0])) {
                    if (extrap)
                        iroff2 = iroff2 + 1;
                    if (!extrap)
                        iroff1 = iroff1 + 1;
                }
                if (last[0] > 10 && erro12 > errmax[0])
                    iroff3 = iroff3 + 1;
            }
            rlist[maxerr[0] - 1] = area1[0];
            rlist[last[0] - 1] = area2[0];
            errbnd = max(epsabs, epsrel * abs(area));
            if (iroff1 + iroff2 >= 10 || iroff3 >= 20)
                ier[0] = 2;
            if (iroff2 >= 5)
                ierro = 3;
            if (last[0] == limit)
                ier[0] = 1;
            if (max(abs(a1), abs(b2)) <= (1 + 100 * epmach)
                    * (abs(a2) + 1000 * uflow))
                ier[0] = 4;
            if (!(error2[0] > error1[0])) {
                alist[last[0] - 1] = a2;
                blist[maxerr[0] - 1] = b1;
                blist[last[0] - 1] = b2;
                elist[maxerr[0] - 1] = error1[0];
                elist[last[0] - 1] = error2[0];
            } else {
                alist[maxerr[0] - 1] = a2;
                alist[last[0] - 1] = a1;
                blist[last[0] - 1] = b1;
                rlist[maxerr[0] - 1] = area2[0];
                rlist[last[0] - 1] = area1[0];
                elist[maxerr[0] - 1] = error2[0];
                elist[last[0] - 1] = error1[0];
            }
            doQSORT(limit, last[0], maxerr, errmax, elist, iord, nrmax);
            if (errsum <= errbnd) {
                result[0] = 0;
                for (k = 1; k <= last[0]; k += 1) {
                    result[0] = result[0] + rlist[k - 1];
                }
                abserr[0] = errsum;
                if (ier[0] > 2)
                    ier[0] = ier[0] - 1;
                neval[0] = 42 * last[0] - 21;
                // System.err.println(
                // "do_qagse: return thru errsum <= errbnd; errsum: "+errsum+" errbnd: "+errbnd+" result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                // );
                return;
            }
            // System.err.println( "reached ier != 0 test; ier: "+ier[0] );
            if (ier[0] != 0)
                break;
            // System.err.println( "reached last != 2 test; last: "+last[0] );
            if (!(last[0] == 2)) {
                // System.err.println( "reach noext test; noext: "+noext );
                if (noext)
                    continue;
                if (erlarg == -999)
                    throw new FunctionEvaluationException(erlarg,
                            "erlarg NOT DEFINED!!!");
                erlarg = erlarg - erlast;
                if (small == -999)
                    throw new FunctionEvaluationException(small,
                            "small NOT DEFINED!!!");
                if (abs(b1 - a1) > small)
                    erlarg = erlarg + erro12;
                if (!(extrap)) {
                    // System.err.println(
                    // "reach small test 1; blist[maxerr]: "+blist [ maxerr[0]
                    // -1 ]+" alist[maxerr]: "+alist [ maxerr[0] -1
                    // ]+" small: "+small );
                    if (abs(blist[maxerr[0] - 1] - alist[maxerr[0] - 1]) > small)
                        continue;
                    // System.err.println( "passed small test 1" );
                    extrap = true;
                    nrmax[0] = 2;
                }
                if (ertest == -999)
                    throw new FunctionEvaluationException(ertest,
                            "ertest NOT DEFINED!!!");
                // System.err.println(
                // "reached ierro test; ierro: "+ierro+" erlarg: "+erlarg+" ertest: "+ertest
                // );
                if (!(ierro == 3 || erlarg <= ertest)) {
                    id = nrmax[0];
                    jupbnd = last[0];
                    if (last[0] > (2 + limit / 2))
                        jupbnd = limit + 3 - last[0];
                    boolean goto90 = false;
                    for (k = id; k <= jupbnd; k += 1) {
                        maxerr[0] = iord[nrmax[0] - 1];
                        errmax[0] = elist[maxerr[0] - 1];
                        // System.err.println(
                        // "reach small test 2; small: "+small );
                        if (abs(blist[maxerr[0] - 1] - alist[maxerr[0] - 1]) > small) {
                            // System.err.println( "set goto90 = true" );
                            goto90 = true;
                            break;
                        }
                        nrmax[0] = nrmax[0] + 1;
                    }
                    if (goto90)
                        continue;
                }
                numrl2[0] = numrl2[0] + 1;
                // System.err.println(
                // "do_qagse: assign area ==  "+area+" to rlist2["+(numrl2[0]-1)+"]"
                // );
                rlist2[numrl2[0] - 1] = area;
                doQEXTR(numrl2, rlist2, reseps, abseps, res3la, nres);
                ktmin = ktmin + 1;
                if (ktmin > 5 && abserr[0] < 1e-3 * errsum)
                    ier[0] = 5;
                // System.err.println(
                // "reached abseps < abserr test; abseps: "+abseps[0]+", abserr: "+abserr[0]
                // );
                if (!(abseps[0] >= abserr[0])) {
                    ktmin = 0;
                    abserr[0] = abseps[0];
                    // System.err.println(
                    // "assign reseps ("+reseps[0]+") to result" );
                    result[0] = reseps[0];
                    correc = erlarg;
                    ertest = max(epsabs, epsrel * abs(reseps[0]));
                    if (abserr[0] <= ertest)
                        break;
                }
                if (numrl2[0] == 1)
                    noext = true;
                if (ier[0] == 5)
                    break;
                maxerr[0] = iord[1 - 1];
                errmax[0] = elist[maxerr[0] - 1];
                nrmax[0] = 1;
                extrap = false;
                small = small * 0.5;
                erlarg = errsum;
                continue;
            }
            small = abs(b - a) * 0.375;
            erlarg = errsum;
            ertest = errbnd;
            // System.err.println(
            // "do_qagse: assign (#2) area ==  "+area+" to rlist2[1]" );
            rlist2[2 - 1] = area;
        }
        if (abserr[0] == oflow) {
            // System.err.println(
            // "do_qagse: abserr: "+abserr[0]+"  oflow: "+oflow );
            result[0] = 0;
            for (k = 1; k <= last[0]; k += 1) {
                result[0] = result[0] + rlist[k - 1];
            }
            abserr[0] = errsum;
            if (ier[0] > 2)
                ier[0] = ier[0] - 1;
            neval[0] = 42 * last[0] - 21;
            // System.err.println(
            // "do_qagse: return thru abserr == oflow; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
            // );
            return;
        }
        if (ier[0] + ierro == 0) {
            if (correc == -999)
                throw new FunctionEvaluationException(correc,
                        "correc NOT DEFINED!!!");
            if (ierro == 3)
                abserr[0] = abserr[0] + correc;
            if (ier[0] == 0)
                ier[0] = 3;
            if (result[0] != 0 && area != 0) {
                if (abserr[0] / abs(result[0]) > errsum / abs(area)) {
                    result[0] = 0;
                    for (k = 1; k <= last[0]; k += 1) {
                        result[0] = result[0] + rlist[k - 1];
                    }
                    abserr[0] = errsum;
                    if (ier[0] > 2)
                        ier[0] = ier[0] - 1;
                    neval[0] = 42 * last[0] - 21;
                    // System.err.println(
                    // "do_qagse: return thru rel. err. ineq.; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                    // );
                    return;
                }
                if (ksgn == (-1)
                        && max(abs(result[0]), abs(area)) <= defabs[0] * 0.01) {
                    if (ier[0] > 2)
                        ier[0] = ier[0] - 1;
                    neval[0] = 42 * last[0] - 21;
                    // System.err.println(
                    // "do_qagse: return thru 2nd rel. err. ineq.; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                    // );
                    return;
                }
                if (0.01 > (result[0] / area) || (result[0] / area) > 100
                        || errsum > abs(area))
                    ier[0] = 6;
                if (ier[0] > 2)
                    ier[0] = ier[0] - 1;
                neval[0] = 42 * last[0] - 21;
                // System.err.println(
                // "do_qagse: didn't return thru other ways; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                // );
                return;
            }
            if (abserr[0] > errsum) {
                result[0] = 0;
                for (k = 1; k <= last[0]; k += 1) {
                    result[0] = result[0] + rlist[k - 1];
                }
                abserr[0] = errsum;
                if (ier[0] > 2)
                    ier[0] = ier[0] - 1;
                neval[0] = 42 * last[0] - 21;
                // System.err.println(
                // "do_qagse: return thru abserr > errsum; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                // );
                return;
            }
            if (area == 0) {
                if (ier[0] > 2)
                    ier[0] = ier[0] - 1;
                neval[0] = 42 * last[0] - 21;
                // System.err.println(
                // "do_qagse: return thru area == 0; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
                // );
                return;
            }
        }
        if (ksgn == (-1) && max(abs(result[0]), abs(area)) <= defabs[0] * 0.01) {
            if (ier[0] > 2)
                ier[0] = ier[0] - 1;
            neval[0] = 42 * last[0] - 21;
            // System.err.println(
            // "do_qagse: return thru ineq 3; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
            // );
            return;
        }
        if (0.01 > (result[0] / area) || (result[0] / area) > 100
                || errsum > abs(area))
            ier[0] = 6;
        if (ier[0] > 2)
            ier[0] = ier[0] - 1;
        neval[0] = 42 * last[0] - 21;
        // System.err.println(
        // "do_qagse: didn't return other ways #2; result: "+result[0]+" abserr: "+abserr[0]+" ier: "+ier[0]
        // );
        return;
    }



    /**
     * QEXTR carries out the Epsilon extrapolation algorithm.
     * <p/>
     * The routine determines the limit of a given sequence of approximations,
     * by means of the epsilon algorithm of P. Wynn. An estimate of the absolute
     * error is also given. The condensed epsilon table is computed. Only those
     * elements needed for the computation of the next diagonal are preserved.
     * 
     * @param n
     *            indicates the entry of EPSTAB which contains the new element
     *            in the first column of the epsilon table.
     * @param epstab
     *            the two lower diagonals of the triangular epsilon table. The
     *            elements are numbered starting at the right-hand corner of the
     *            triangle.
     * @param result
     *            the estimated value of the integral.
     * @param abserr
     *            stimate of the absolute error computed from RESULT and the 3
     *            previous results.
     * @param res3la
     *            the last 3 results
     * @param nres
     *            the number of calls to the routine. This should be zero on the
     *            first call, and is automatically updated before return.
     */
    private static void doQEXTR(int[] n, double[] epstab, double[] result,
            double[] abserr, double[] res3la, int[] nres) {
        double delta1, delta2, delta3, epmach, epsinf = -1, error, err1, err2, err3, e0, e1, e1abs, e2, e3, oflow, res, ss =
                -1, tol1, tol2, tol3;
        int i, ib, ib2, ie, indx, k1, k2, k3, limexp, newelm, num;
        epmach = D1MACH[4 - 1];
        oflow = D1MACH[2 - 1];
        nres[0] = nres[0] + 1;
        abserr[0] = oflow;
        // System.err.println(
        // "do_qelg: assign epstab["+(n[0]-1)+"] == "+epstab[n[0]-1]+" tp result"
        // );
        result[0] = epstab[n[0] - 1];
        if (n[0] < 3) {
            abserr[0] = max(abserr[0], 5 * epmach * abs(result[0]));
            return;
        }
        limexp = 50;
        epstab[n[0] + 2 - 1] = epstab[n[0] - 1];
        newelm = (n[0] - 1) / 2;
        epstab[n[0] - 1] = oflow;
        num = n[0];
        k1 = n[0];
        for (i = 1; i <= newelm; i += 1) {
            k2 = k1 - 1;
            k3 = k1 - 2;
            res = epstab[k1 + 2 - 1];
            e0 = epstab[k3 - 1];
            e1 = epstab[k2 - 1];
            e2 = res;
            e1abs = abs(e1);
            delta2 = e2 - e1;
            err2 = abs(delta2);
            tol2 = max(abs(e2), e1abs) * epmach;
            delta3 = e1 - e0;
            err3 = abs(delta3);
            tol3 = max(e1abs, abs(e0)) * epmach;
            if (!(err2 > tol2 || err3 > tol3)) {
                // System.err.println(
                // "do_qelg: assign res == "+res+" tp result" );
                result[0] = res;
                abserr[0] = err2 + err3;
                abserr[0] = max(abserr[0], 5 * epmach * abs(result[0]));
                return;
            }
            e3 = epstab[k1 - 1];
            epstab[k1 - 1] = e1;
            delta1 = e1 - e3;
            err1 = abs(delta1);
            tol1 = max(e1abs, abs(e3)) * epmach;
            boolean goto20 = false;
            if (err1 <= tol1 || err2 <= tol2 || err3 <= tol3) {
                goto20 = true;
            } else {
                ss = 1 / delta1 + 1 / delta2 - 1 / delta3;
                epsinf = abs(ss * e1);
            }
            if (goto20 || !(epsinf > 1e-4)) {
                n[0] = i + i - 1;
            } else {
                res = e1 + 1 / ss;
                epstab[k1 - 1] = res;
                k1 = k1 - 2;
                error = err2 + abs(res - e2) + err3;
                if (error > abserr[0])
                    continue;
                abserr[0] = error;
                // System.err.println(
                // "do_qelg: assign (#2) res == "+res+" tp result" );
                result[0] = res;
            }
        }
        if (n[0] == limexp)
            n[0] = 2 * (limexp / 2) - 1;
        ib = 1;
        if ((num / 2) * 2 == num)
            ib = 2;
        ie = newelm + 1;
        for (i = 1; i <= ie; i += 1) {
            ib2 = ib + 2;
            epstab[ib - 1] = epstab[ib2 - 1];
            ib = ib2;
        }
        if (!(num == n[0])) {
            indx = num - n[0] + 1;
            for (i = 1; i <= n[0]; i += 1) {
                epstab[i - 1] = epstab[indx - 1];
                indx = indx + 1;
            }
        }
        if (!(nres[0] >= 4)) {
            res3la[nres[0] - 1] = result[0];
            abserr[0] = oflow;
            abserr[0] = max(abserr[0], 5 * epmach * abs(result[0]));
            return;
        }
        abserr[0] =
                abs(result[0] - res3la[3 - 1]) + abs(result[0] - res3la[2 - 1])
                        + abs(result[0] - res3la[1 - 1]);
        res3la[1 - 1] = res3la[2 - 1];
        res3la[2 - 1] = res3la[3 - 1];
        res3la[3 - 1] = result[0];
        abserr[0] = max(abserr[0], 5 * epmach * abs(result[0]));
        return;
    }



    /**
     * QK21 carries out a 21 point Gauss-Kronrod quadrature rule.
     * <p/>
     * <b>Discussion:</b>
     * <p/>
     * This routine approximates I = integral ( A <= X <= B ) F(X) dx with an
     * error estimate, and J = integral ( A <= X <= B ) | F(X) | dx
     * 
     * @param integrand
     *            the integrand function
     * @param a
     *            lower limit of the integration
     * @param b
     *            upper limit of the integration
     * @param result
     *            the estimated value of the integral. RESULT is computed by
     *            applying the 21-point Kronrod rule (resk) obtained by optimal
     *            addition of abscissae to the 10-point Gauss rule (resg).
     * @param abserr
     *            an estimate of | I - RESULT |
     * @param resabs
     *            approximation to the integral of the absolute value of F
     * @param resasc
     *            approximation to the integral | F-I/(B-A) | over [A,B]
     * @throws FunctionEvaluationException
     *             if an error occurs while evaluating the integrand function
     */
    private static void doQK21(UnivariateRealFunction integrand, double a,
            double b, double[] result, double[] abserr, double[] resabs,
            double[] resasc) throws FunctionEvaluationException {
        // System.err.println( "do_qk21: a: "+a+"  b: "+b );
        double absc, centr, dhlgth;
        double epmach, fc, fsum, fval1, fval2, hlgth, resg, resk, reskh, uflow;
        int j, jtw, jtwm1;
        double[] fv1 = new double[10];
        double[] fv2 = new double[10];
        double[] wg =
                { 0.066671344308688137593568809893332,
                        0.149451349150580593145776339657697,
                        0.219086362515982043995534934228163,
                        0.269266719309996355091226921569469,
                        0.295524224714752870173892994651338 };
        double[] xgk =
                { 0.995657163025808080735527280689003,
                        0.973906528517171720077964012084452,
                        0.930157491355708226001207180059508,
                        0.865063366688984510732096688423493,
                        0.780817726586416897063717578345042,
                        0.679409568299024406234327365114874,
                        0.562757134668604683339000099272694,
                        0.433395394129247190799265943165784,
                        0.294392862701460198131126603103866,
                        0.148874338981631210884826001129720,
                        0.000000000000000000000000000000000 };
        double[] wgk =
                { 0.011694638867371874278064396062192,
                        0.032558162307964727478818972459390,
                        0.054755896574351996031381300244580,
                        0.075039674810919952767043140916190,
                        0.093125454583697605535065465083366,
                        0.109387158802297641899210590325805,
                        0.123491976262065851077958109831074,
                        0.134709217311473325928054001771707,
                        0.142775938577060080797094273138717,
                        0.147739104901338491374841515972068,
                        0.149445554002916905664936468389821 };
        epmach = D1MACH[4 - 1];
        uflow = D1MACH[1 - 1];
        centr = 0.5 * (a + b);
        hlgth = 0.5 * (b - a);
        dhlgth = abs(hlgth);
        resg = 0;
        fc = integrand.value(centr);
        // double max_fx = fc;
        resk = wgk[11 - 1] * fc;
        resabs[0] = abs(resk);
        for (j = 1; j <= 5; j++) {
            jtw = 2 * j;
            absc = hlgth * xgk[jtw - 1];
            fval1 = integrand.value(centr - absc);
            // if ( fval1 > max_fx ) max_fx = fval1;
            fval2 = integrand.value(centr + absc);
            // if ( fval2 > max_fx ) max_fx = fval2;
            fv1[jtw - 1] = fval1;
            fv2[jtw - 1] = fval2;
            fsum = fval1 + fval2;
            resg = resg + wg[j - 1] * fsum;
            resk = resk + wgk[jtw - 1] * fsum;
            resabs[0] = resabs[0] + wgk[jtw - 1] * (abs(fval1) + abs(fval2));
        }
        for (j = 1; j <= 5; j++) {
            jtwm1 = 2 * j - 1;
            absc = hlgth * xgk[jtwm1 - 1];
            fval1 = integrand.value(centr - absc);
            // if ( fval1 > max_fx ) max_fx = fval1;
            fval2 = integrand.value(centr + absc);
            // if ( fval2 > max_fx ) max_fx = fval2;
            fv1[jtwm1 - 1] = fval1;
            fv2[jtwm1 - 1] = fval2;
            fsum = fval1 + fval2;
            resk = resk + wgk[jtwm1 - 1] * fsum;
            resabs[0] = resabs[0] + wgk[jtwm1 - 1] * (abs(fval1) + abs(fval2));
        }
        reskh = resk * 0.5;
        resasc[0] = wgk[11 - 1] * abs(fc - reskh);
        for (j = 1; j <= 10; j++)
            resasc[0] =
                    resasc[0]
                            + wgk[j - 1]
                            * (abs(fv1[j - 1] - reskh) + abs(fv2[j - 1] - reskh));
        result[0] = resk * hlgth;
        resabs[0] = resabs[0] * dhlgth;
        resasc[0] = resasc[0] * dhlgth;
        abserr[0] = abs((resk - resg) * hlgth);
        if (resasc[0] != 0 && abserr[0] != 0)
            abserr[0] =
                    resasc[0] * min(1, pow((200 * abserr[0] / resasc[0]), 1.5));
        if (resabs[0] > uflow / (50 * epmach))
            abserr[0] = max((epmach * 50) * resabs[0], abserr[0]);
        // System.err.println(
        // "do_qk21: max_fx: "+max_fx+", result: "+result[0]+"  abserr: "+abserr[0]
        // );
    }



    /**
     * QSORT maintains the order of a list of local error estimates.
     * <p/>
     * This routine maintains the descending ordering in the list of the local
     * error estimates resulting from the interval subdivision process. At each
     * call two error estimates are inserted using the sequential search
     * top-down for the largest error estimate and bottom-up for the smallest
     * error estimate.
     * 
     * @param limit
     *            the maximum number of error estimates the list can contain
     * @param last
     *            the current number of error estimates
     * @param maxerr
     *            the index in the list of the NRMAX-th largest error
     * @param ermax
     *            the NRMAX-th largest error = ELIST(MAXERR)
     * @param elist
     *            contains the error estimates
     * @param iord
     *            The first K elements contain pointers to the error estimates
     *            such that ELIST(IORD(1)) through ELIST(IORD(K)) form a
     *            decreasing sequence, with K = LAST if LAST <= (LIMIT/2+2), and
     *            otherwise K = LIMIT+1-LAST.
     * @param nrmax
     *            largest error = ELIST(MAXERR)
     */
    private static void doQSORT(int limit, int last, int[] maxerr,
            double[] ermax, double[] elist, int[] iord, int[] nrmax) {
        double errmax, errmin;
        int i = 0, ibeg, ido, isucc, j, jbnd, jupbn, k;
        if (!(last > 2)) {
            iord[1 - 1] = 1;
            iord[2 - 1] = 2;
            maxerr[0] = iord[nrmax[0] - 1];
            ermax[0] = elist[maxerr[0] - 1];
            return;
        }
        errmax = elist[maxerr[0] - 1];
        if (!(nrmax[0] == 1)) {
            ido = nrmax[0] - 1;
            for (i = 1; i <= ido; i += 1) {
                isucc = iord[nrmax[0] - 1 - 1];
                if (errmax <= elist[isucc - 1])
                    break;
                iord[nrmax[0] - 1] = isucc;
                nrmax[0] = nrmax[0] - 1;
            }
        }
        jupbn = last;
        if (last > (limit / 2 + 2))
            jupbn = limit + 3 - last;
        errmin = elist[last - 1];
        jbnd = jupbn - 1;
        ibeg = nrmax[0] + 1;
        boolean goto60 = false;
        if (!(ibeg > jbnd)) {
            for (i = ibeg; i <= jbnd; i += 1) {
                isucc = iord[i - 1];
                if (errmax >= elist[isucc - 1]) {
                    goto60 = true;
                    break;
                }
                iord[i - 1 - 1] = isucc;
            }
        }
        if (!goto60) {
            iord[jbnd - 1] = maxerr[0];
            iord[jupbn - 1] = last;
            maxerr[0] = iord[nrmax[0] - 1];
            ermax[0] = elist[maxerr[0] - 1];
            return;
        }
        iord[i - 1 - 1] = maxerr[0];
        k = jbnd;
        boolean goto80 = false;
        for (j = i; j <= jbnd; j += 1) {
            isucc = iord[k - 1];
            if (errmin < elist[isucc - 1]) {
                goto80 = true;
                break;
            }
            iord[k + 1 - 1] = isucc;
            k = k - 1;
        }
        if (!goto80) {
            iord[i - 1] = last;
            maxerr[0] = iord[nrmax[0] - 1];
            ermax[0] = elist[maxerr[0] - 1];
            return;
        }
        iord[k + 1 - 1] = last;
        maxerr[0] = iord[nrmax[0] - 1];
        ermax[0] = elist[maxerr[0] - 1];
        return;
    }



    /**
     * Implementation of the quadpack.
     * <p/>
     * quadpack is a fortran subroutine package for the numerical computation of
     * definite one-dimensional integrals. it originated from a joint project of
     * r. piessens and e. de doncker (appl. and progr. div.- k.u.leuven,
     * belgium), c. ueberhuber (inst. fuer - techn.u.wien, austria), and d.
     * kahaner (nation. bur. of standards- washington d.c., u.s.a.).
     */
    public Quad() {
        super(50);
        defaultAbsoluteAccuracy = 1.49e-8;
        defaultRelativeAccuracy = 1.49e-8;
        defaultMinimalIterationCount = 50;
    }



    /**
     * Implementation of the quadpack.
     * <p/>
     * quadpack is a fortran subroutine package for the numerical computation of
     * definite one-dimensional integrals. it originated from a joint project of
     * r. piessens and e. de doncker (appl. and progr. div.- k.u.leuven,
     * belgium), c. ueberhuber (inst. fuer - techn.u.wien, austria), and d.
     * kahaner (nation. bur. of standards- washington d.c., u.s.a.).
     * 
     * @param f
     *            integrand function
     */
    @Deprecated
    public Quad(UnivariateRealFunction f) {
        super(f, 50);
        defaultAbsoluteAccuracy = 1.49e-8;
        defaultRelativeAccuracy = 1.49e-8;
        defaultMinimalIterationCount = 50;
    }



    @Override
    @Deprecated
    public double integrate(double a, double b) throws ConvergenceException,
            FunctionEvaluationException {
        return integrate(f, a, b);
    }



    /**
     * QAGE estimates a definite integral.
     * <p/>
     * The routine calculates an approximation RESULT to a definite integral I =
     * integral of F over (A,B), hopefully satisfying || I - RESULT || <= max (
     * EPSABS, EPSREL * ||I|| ).
     * 
     * @param f
     *            integrand function
     * @param a
     *            lower limit of integration
     * @param b
     *            upper limit of integration
     * @return the estimated value of the integral
     * @throws FunctionEvaluationException
     *             if an error occurs while evaluating the integrand function
     */
    @Override
    public double integrate(UnivariateRealFunction f, double a, double b)
            throws FunctionEvaluationException {
        clearResult();
        verifyInterval(a, b);

        int limit = getMaximalIterationCount();
        double epsabs = getAbsoluteAccuracy();
        double epsrel = getRelativeAccuracy();

        double[] result = new double[1];
        double[] abserr = new double[1];
        int[] ier = new int[1];

        int[] iwork = null;
        double[] work = null;
        int[] neval = new int[1], last = new int[1];
        boolean verboseErrors = false;

        int lenw = 4 * limit;
        if (iwork == null || iwork.length != limit)
            iwork = new int[limit];
        if (work == null || work.length != lenw)
            work = new double[lenw];

        ier[0] = 6;
        neval[0] = 0;
        last[0] = 0;
        result[0] = 0;
        abserr[0] = 0;
        if (!(limit < 1 || lenw < limit * 4)) {
            double[] alist = new double[limit];
            double[] blist = new double[limit];
            double[] rlist = new double[limit];
            double[] elist = new double[limit];

            doQAGE(f, a, b, epsabs, epsrel, limit, result, abserr, neval, ier,
                    alist, blist, rlist, elist, iwork, last);

            System.arraycopy(alist, 0, work, 0, limit);
            System.arraycopy(blist, 0, work, limit, limit);
            System.arraycopy(rlist, 0, work, 2 * limit, limit);
            System.arraycopy(elist, 0, work, 3 * limit, limit);
        }

        if (ier[0] != 0 && verboseErrors)
            throw new FunctionEvaluationException(ier[0], "abnormal return");

        setResult(result[0], neval[0]);
        setAbsoluteAccuracy(abserr[0]);
        setRelativeAccuracy(abserr[0] / result[0]);

        return this.result;
    }

}

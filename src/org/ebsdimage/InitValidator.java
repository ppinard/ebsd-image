package org.ebsdimage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import org.ebsdimage.core.run.Operation;
import org.ebsdimage.gui.run.ops.OperationCreator;
import org.ebsdimage.io.FileUtil;
import org.simpleframework.xml.Root;

import rmlshared.thread.Reflection;
import static org.ebsdimage.core.exp.ExpConstants.*;
import static ptpshared.io.FileUtil.joinPackageNames;

/**
 * Validates that the convention used in the experiment operations' definition.
 * 
 * @author Philippe T. Pinard
 */
public class InitValidator {

    /**
     * Quick check of the operations.
     * 
     * @param args
     *            no arguments is required
     * @throws Exception
     *             if an exception occurs
     */
    public static void main(String[] args) throws Exception {
        new InitValidator().validate();
    }

    /** Logger for errors and warnings. */
    private final Logger logger = Logger.getLogger("ebsd");



    /**
     * Verifies if the class has a <code>DEFAULT</code> public static final
     * field.
     * 
     * @param clasz
     *            a class
     * @return <code>true</code> if the class has a public static final field
     *         named <code>DEFAULT</code>
     */
    private boolean hasDefaultField(Class<?> clasz) {
        // Check if DEFAULT field exists
        Field field;
        try {
            field = clasz.getField("DEFAULT");
        } catch (NoSuchFieldException e) {
            return false;
        }

        // Check if DEFAULT field return the right type of class
        if (!field.getDeclaringClass().equals(clasz))
            return false;

        // Check if the DEFAULT field is public static final
        if (!Modifier.isPublic(field.getModifiers())
                || !Modifier.isStatic(field.getModifiers())
                || !Modifier.isFinal(field.getModifiers()))
            return false;

        return true;
    }



    /**
     * Verifies if the class has related a GUI class. The GUI class must have
     * the same name as the class with the word <code>Creator</code> or
     * <code>Dialog</code> as a suffix.
     * 
     * @param clasz
     *            a class
     * @return <code>true</code> if the class has a related GUI class
     */
    private boolean hasGUIClass(Class<?> clasz) {
        String packageName = clasz.getPackage().getName();
        if (!packageName.startsWith("org.ebsdimage.core.exp.ops."))
            throw new IllegalArgumentException("Class (" + clasz
                    + ") is not an experiment operation.");

        packageName = packageName.replaceFirst(".core.", ".gui.");

        // Find GUI class
        Class<?> guiClasz;
        try {
            guiClasz =
                    Reflection.forName(packageName + "."
                            + clasz.getSimpleName() + "Creator");
        } catch (IllegalArgumentException e1) {
            try {
                guiClasz =
                        Reflection.forName(packageName + "."
                                + clasz.getSimpleName() + "Dialog");
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }

        // Check that the GUI class extends OperationCreator
        if (!OperationCreator.class.isAssignableFrom(guiClasz))
            return false;

        return true;
    }



    /**
     * Checks whether a class is an operation class. A class must:
     * <ul>
     * <li>be derived from <code>Operation</code> class</li>
     * <li>not be an abstract class</li>
     * <li>not end with the suffix "Mock" or "Test"</li>
     * </ul>
     * 
     * @param clasz
     *            a class
     * @return <code>true</code> if the class is an operation class
     */
    private boolean isOperation(Class<?> clasz) {
        return Operation.class.isAssignableFrom(clasz)
                && !Modifier.isAbstract(clasz.getModifiers())
                && !clasz.getSimpleName().endsWith("Mock")
                && !clasz.getSimpleName().endsWith("Test");
    }



    /**
     * Verifies if the class can be serialized as an XML. The method checks that
     * the class or one of its parent(s) has the <code>Root</code> annotation.
     * 
     * @param clasz
     *            a class
     * @return <code>true</code> if the class can be serialized as an XML
     */
    private boolean isXMLSerializable(Class<?> clasz) {
        while (!clasz.equals(Object.class)) {
            if (clasz.getAnnotation(Root.class) != null)
                return true;

            clasz = clasz.getSuperclass();
        }

        return false;
    }



    /**
     * Validates that all experiment operations have:
     * <ul>
     * <li>a <code>DEFAULT</code> public static final variable (error)</li>
     * <li>can be saved as an XML (error)</li>
     * <li>has a GUI class (warning)</li>.
     * </ul>
     * 
     * @throws IOException
     *             if an error occurs while finding the operations
     */
    public void validate() throws IOException {
        validateOperationInPackage(joinPackageNames(PATTERN_CORE_PACKAGE, "op"));
        validateOperationInPackage(joinPackageNames(PATTERN_CORE_PACKAGE,
                "post"));
        validateOperationInPackage(joinPackageNames(PATTERN_CORE_PACKAGE,
                "results"));

        validateOperationInPackage(joinPackageNames(HOUGH_CORE_PACKAGE, "pre"));
        validateOperationInPackage(joinPackageNames(HOUGH_CORE_PACKAGE, "op"));
        validateOperationInPackage(joinPackageNames(HOUGH_CORE_PACKAGE, "post"));
        validateOperationInPackage(joinPackageNames(HOUGH_CORE_PACKAGE,
                "results"));

        validateOperationInPackage(joinPackageNames(DETECTION_CORE_PACKAGE,
                "pre"));
        validateOperationInPackage(joinPackageNames(DETECTION_CORE_PACKAGE,
                "op"));
        validateOperationInPackage(joinPackageNames(DETECTION_CORE_PACKAGE,
                "post"));
        validateOperationInPackage(joinPackageNames(DETECTION_CORE_PACKAGE,
                "results"));

        validateOperationInPackage(joinPackageNames(
                IDENTIFICATION_CORE_PACKAGE, "pre"));
        validateOperationInPackage(joinPackageNames(
                IDENTIFICATION_CORE_PACKAGE, "op"));
        validateOperationInPackage(joinPackageNames(
                IDENTIFICATION_CORE_PACKAGE, "post"));
        validateOperationInPackage(joinPackageNames(
                IDENTIFICATION_CORE_PACKAGE, "results"));

        validateOperationInPackage(joinPackageNames(INDEXING_CORE_PACKAGE,
                "pre"));
        validateOperationInPackage(joinPackageNames(INDEXING_CORE_PACKAGE, "op"));
        validateOperationInPackage(joinPackageNames(INDEXING_CORE_PACKAGE,
                "post"));
        validateOperationInPackage(joinPackageNames(INDEXING_CORE_PACKAGE,
                "results"));
    }



    /**
     * Checks that all operations in the specified package are valid. The result
     * of the validation is output in the logger.
     * 
     * @param packageName
     *            package name (dot form)
     * @throws IOException
     *             if an error occurs while finding the operations
     */
    private void validateOperationInPackage(String packageName)
            throws IOException {

        for (Class<?> clasz : FileUtil.getClasses(packageName)) {
            // Skip non-operation class
            if (!isOperation(clasz))
                continue;

            // Check XML serializable
            if (!isXMLSerializable(clasz))
                logger.severe("Class (" + clasz
                        + ") cannot be serialized to an XML.");

            // Skip pattern operations
            if (packageName.equals(joinPackageNames(PATTERN_CORE_PACKAGE, "op")))
                continue;

            // Check DEFAULT field
            if (!hasDefaultField(clasz))
                logger.severe("Class (" + clasz
                        + ") does not have a DEFAULT field.");

            // Check GUI class
            if (!hasGUIClass(clasz))
                logger.warning("Class (" + clasz
                        + ") does not have a GUI class.");
        }
    }
}

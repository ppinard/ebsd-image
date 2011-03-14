package org.ebsdimage.core.sim;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;

public class Shape03 {
    public static void main(String[] args) {
        GUI guiObj = new GUI();
    }// end main
}// end controlling class Shape03

class GUI extends Frame {
    int res;// store screen resolution here

    static final int ds = 72;// default scale, 72 units/inch

    static final int hSize = 4;// horizonal size = 4 inches

    static final int vSize = 4;// vertical size = 4 inches



    GUI() {// constructor
           // Get screen resolution
        res = Toolkit.getDefaultToolkit().getScreenResolution();

        // Set Frame size
        this.setSize(hSize * res, vSize * res);
        this.setVisible(true);
        this.setTitle("Copyright 1999, R.G.Baldwin");

        // Window listener to terminate program.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }// end constructor



    // Override the paint() method
    @Override
    public void paint(Graphics g) {
        // Downcast the Graphics object to a Graphics2D object
        Graphics2D g2 = (Graphics2D) g;

        // Scale device space to produce inches on the screen
        // based on actual screen resolution.
        g2.scale((double) res / 72, (double) res / 72);

        // Translate origin to center of Frame
        g2.translate((hSize / 2) * ds, (vSize / 2) * ds);

        // Draw x-axis
        g2.draw(new Line2D.Double(-1.5 * ds, 0.0, 1.5 * ds, 0.0));
        // Draw y-axis
        g2.draw(new Line2D.Double(0.0, -1.5 * ds, 0.0, 1.5 * ds));

        // Define a one-inch diameter circle centered about
        // its origin. Note that Ellipse2D implements Shape
        Ellipse2D.Double theCircle =
                new Ellipse2D.Double(-0.5 * ds, -0.5 * ds, 1.0 * ds, 1.0 * ds);

        // Draw theCircle in the Frame in the default
        // drawing color, black
        g2.draw(theCircle);

        // Get bounding box of theCircle
        Rectangle2D theBoundingBox = theCircle.getBounds2D();
        g2.setColor(Color.red);// change the drawing color
        // Draw the bounding box in the new color
        g2.draw(theBoundingBox);

        // Create boxes to test for contains and intersects
        Rectangle2D.Double theInsideBox =
                new Rectangle2D.Double(-0.25 * ds, -0.25 * ds, 0.5 * ds,
                        0.5 * ds);
        Rectangle2D.Double theIntersectingBox =
                new Rectangle2D.Double(0.3 * ds, 0.3 * ds, 0.5 * ds, 0.5 * ds);
        Rectangle2D.Double theOutsideBox =
                new Rectangle2D.Double(-1.25 * ds, -1.25 * ds, 0.5 * ds,
                        0.5 * ds);

        // Draw the test boxes in new colors
        g2.setColor(Color.green);
        g2.draw(theInsideBox);// theInsideBox is green
        g2.setColor(Color.blue);
        g2.draw(theIntersectingBox);// theIntersectingBox blue
        g2.setColor(Color.magenta);
        g2.draw(theOutsideBox);// theOutsideBox is magenta

        // Now perform the tests and display the results
        // on the command-line screen.
        System.out.println("theCircle contains theBoundingBox: "
                + theCircle.contains(theBoundingBox));
        System.out.println("theCircle contains theInsideBox: "
                + theCircle.contains(theInsideBox));
        System.out.println("theCircle contains theIntersectingBox: "
                + theCircle.contains(theIntersectingBox));
        System.out.println("theCircle contains theOutsideBox: "
                + theCircle.contains(theOutsideBox));
        System.out.println();// blank line
        System.out.println("theCircle intersects theBoundingBox: "
                + theCircle.intersects(theBoundingBox));
        System.out.println("theCircle intersects theInsideBox: "
                + theCircle.intersects(theInsideBox));
        System.out.println("theCircle intersects theIntersectingBox: "
                + theCircle.intersects(theIntersectingBox));
        System.out.println("theCircle intersects theOutsideBox: "
                + theCircle.intersects(theOutsideBox));

        Path2D path = new Path2D.Double();

        QuadCurve2D curve = new QuadCurve2D.Double(0, 0, 20, 20, 40, 30);
        path.append(curve, true);

        curve = new QuadCurve2D.Double(40, 30, 50, 50, 0, 90);
        path.append(curve, true);

        g2.draw(path);
        
        g2.draw

    }// end overridden paint()
}// end class GUI
// =================================//

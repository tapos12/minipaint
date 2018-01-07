/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Color;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 */
public class Coordinate implements Serializable {

        public int x1, y1, x2, y2;
        public Color foreColor;
        public Vector xPoly, yPoly;

        public Coordinate(int inx1, int iny1, int inx2, int iny2, Color color) {
            x1 = inx1;
            y1 = iny1;
            x2 = inx2;
            y2 = iny2;
            foreColor = color;
        }

        public Coordinate(Vector inXPolygon, Vector inYPolygon, Color color) {
            xPoly = (Vector) inXPolygon.clone();
            yPoly = (Vector) inYPolygon.clone();
            foreColor = color;
        }

        public Color colour() {
            return foreColor;
        }

        public int getX1() {
            return x1;
        }

        public int getX2() {
            return x2;
        }

        public int getY1() {
            return y1;
        }

        public int getY2() {
            return y2;
        }

        public Vector getXPolygon() {
            return xPoly;
        }

        public Vector getYPolygon() {
            return yPoly;
        }
    }

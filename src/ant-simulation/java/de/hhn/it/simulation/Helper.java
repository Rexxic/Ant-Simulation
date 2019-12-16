package de.hhn.it.simulation;

import javafx.scene.paint.Color;

import java.util.Random;

/**
 * Klasse mit Hilfsmethoden zum erzeugen von Zufallszahlen und zum Umrechnen von
 * Grad in Bogenmass und umgekehrt. Für alle weiteren mathematischen Funktionen
 * siehe {@link Math}.
 */
public class Helper {
    private static final Random random = new Random();
    private static final int[] colorIndex = {randomInt(24)};

    /**
     * @param upperBound obere Grenze der Zufallszahlen
     * @return Eine gleich verteilte Pseudozufallszahl zwischen 0 (inklusive)
     * und der gegebenen oberen Grenze (exclusive)
     * @throws IllegalArgumentException wenn bound nicht positiv ist
     */
    public static int randomInt(int upperBound) {
        return random.nextInt(upperBound);
    }

    /**
     * @param upperBound obere Grenze der Zufallszahlen
     * @return Eine gleich verteilte Pseudozufallszahl zwischen 0 (inklusive)
     * und der gegebenen oberen Grenze (exklusive)
     */
    public static double randomDouble(double upperBound) {
        return random.nextDouble() * upperBound;
    }

    /**
     * @param bounds obere un negiert untere Grenze der Zufallszahlen
     * @return Eine gleich verteilte Pseudozufallszahl zwischen den gegebenen
     * Grenzen (inklusive)
     */
    public static double randomDoubleUpperLowerBound(double bounds) {
        return random.nextDouble() * 2 * bounds - bounds;
    }

    /**
     * @param degree der zu transformierende Wert in Grad
     * @return den berechneten Wert in Bogenmass
     */
    public static double degreeToRadian(double degree) {
        return degree / 360 * 2 * Math.PI;
    }

    /**
     * @param radian der zu transformierende Wert im Bogenmass
     * @return den berechneten Wert in Grad
     */
    public static double radianToDegree(double radian) {
        return radian / (2 * Math.PI) * 360;
    }

    /**
     * @param diffX,diffY die relative Position des Zieles zu unserem Ausgangsobjekt
     * @return gibt die tatsächliche Entfernung des Objekts zu der Position des Zieles zurück
     */
    public static double distance(double diffX, double diffY) {
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * @param diffX,diffY die relative Position des Zieles zu unserem Ausgangsobjekt
     * @return errechnet den Winkel relativ zu 0° in dem der Zielpunkt liegt
     */
    public static double offset(double diffX, double diffY) {
        return Math.toDegrees(Math.atan2(diffY, diffX));
    }

    /**
     * Diese Methode bei jedem Aufruf eine andere der im Array color abgespeicherten Farben aus, um sicherzustellen das sich
     * die Farben nicht allzu ähnlich sind. Momentan sind 14 verschiedene Farben eingetragen.
     */
    public static Color nxtColor() {
        Color[] color = {Color.BLUE, Color.OLIVEDRAB, Color.WHITESMOKE, Color.BLACK, Color.YELLOW, Color.BROWN,
                Color.ORANGE, Color.DARKVIOLET, Color.GREEN, Color.INDIGO, Color.GOLDENROD, Color.DARKCYAN, Color.RED,
                Color.DARKRED, Color.DARKCYAN, Color.DARKVIOLET, Color.DARKBLUE, Color.DARKGOLDENROD, Color.DARKGRAY,
                Color.DARKGREEN, Color.DARKKHAKI, Color.DARKMAGENTA, Color.DARKORCHID, Color.DARKSEAGREEN, Color.DARKTURQUOISE};
        if (colorIndex[0] >= 24) {
            colorIndex[0] = -1;
        }
        colorIndex[0]++;
        return color[colorIndex[0]];
    }
}

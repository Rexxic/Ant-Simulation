package de.hhn.it.simulation;

import de.hhn.it.ui.AntModelGraphic;
import javafx.scene.paint.Color;

/**
 * Mit der Klasse {@link Ant} werden Ameisen in der Simulation funktional
 * abgebildet. Ameisen definieren ihre Position und Rotation (in Grad).
 *
 * @see AntModelGraphic
 */
public class Ant extends Animal {
    private final AntModelGraphic graphic;

    private String antText;
    private boolean isLocked;
    private boolean carriesFood;

    public Ant(double x, double y, double rotation, double boost, Color color) {
        super(x, y, rotation, boost,null);

        javafx.scene.paint.Color headColor = javafx.scene.paint.Color.color(color.getRed() / 5, color.getGreen() / 5, color.getBlue() / 5, 1.0);
        javafx.scene.paint.Color bodyColor1 = javafx.scene.paint.Color.color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2, 1.0);
        javafx.scene.paint.Color bodyColor2 = javafx.scene.paint.Color.color(color.getRed() / 3, color.getGreen() / 3, color.getBlue() / 3, 1.0);
        this.graphic = new AntModelGraphic(color, color, headColor, bodyColor1, bodyColor2, javafx.scene.paint.Color.rgb(64, 255, 0, 1.0));

        this.antText = null;
        this.carriesFood = false;
        this.isLocked = false;
    }

    /**
     * Führt einen Simulationsschritt durch und lässt die Ameise jeweils einen
     * Schritt nach rechts machen.
     */
    @Override
    public void doSimulationStep() {
        super.hasTarget = isLocked || carriesFood;
        super.rotationManipulator();
        super.stepForward();
    }

    /**
     * @return {@code true} wenn diese Ameise Nahrung transportiert.
     */
    public boolean isCarryingFood() {
        return carriesFood;
    }

    public void isCarryingFood(boolean bool) {
        carriesFood = bool;
    }

    public void lock(boolean bool) {
        isLocked = bool;
    }

    /**
     * Liefert die grafische Repräsentation der Ameise. Es wird immer die
     * gleiche {@link AntModelGraphic} Instanz zurückgegeben.
     *
     * @return die grafische Repräsentation dieser Ameise, nie {@code null}
     */
    public AntModelGraphic getSimulationGraphic() {
        return graphic;
    }

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        // Frei nach Joachim Rigelnatz: "Die Ameisen"
        return this.antText;
    }
}
package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Genome;
import de.hhn.it.ui.AntModelGraphic;
import javafx.scene.paint.Color;

/**
 * @author Cedric Seiz
 * Die Queen erbt ihr Genom von einem Ameisenhaufen und mutiert es. Sie bekommt von der Simulation ein Ziel gesetzt und
 * "Verwandelt" sich in einen Ameisenhaufen den sie ihr Genom weitergibt wenn sie ihr Ziel erreicht hat (sie wird gelöscht
 * und der Ameisenhaufen gespawnt).
 */
public class Queen extends Animal {
    private final AntModelGraphic graphic;
    private final Color color;
    private String antText;
    private int stash;

    public Queen(double x, double y, double rotation, Color color, int workers, Genome genome) {
        super(x, y, rotation, null, new Genome(genome));

        Color headColor = javafx.scene.paint.Color.color(Color.GOLD.getRed() / 2, Color.GOLD.getGreen() / 3, Color.GOLD.getBlue() / 3, 1.0);
        Color bodyColor1 = javafx.scene.paint.Color.color(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2, 1.0);
        Color bodyColor2 = javafx.scene.paint.Color.color(color.getRed() / 3, color.getGreen() / 3, color.getBlue() / 3, 1.0);
        double scale = 1 + (1 / 16f * (genome.getHealthPoints()));
        this.graphic = new AntModelGraphic(color, headColor, headColor, bodyColor1, bodyColor2, Color.rgb(64, 255, 0, 1.0), scale);

        this.color = color;
        this.antText = null;
        this.stash = Math.round(genome.getFoodConsumption() * workers);

        super.hasTarget = true;
    }

    /**
     * Führt einen Simulationsschritt durch und lässt die Ameise jeweils einen
     * Schritt nach rechts machen.
     */
    @Override
    public void doSimulationStep() {
        super.rotationManipulator();
        super.stepForward();
    }

    public Color getColor() {
        return color;
    }

    public int getStash() {
        return stash;
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
        return this.antText;
    }
}

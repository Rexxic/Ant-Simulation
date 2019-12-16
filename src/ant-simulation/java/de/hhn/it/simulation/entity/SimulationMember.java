package de.hhn.it.simulation.entity;

import de.hhn.it.ui.SimulationGraphic;

/**
 * Mit der Klasse {@link SimulationMember} werden Teilnehmer der Simulation
 * funktional abgebildet. Teilnehmer der Simulation definieren ihre Position und
 * Rotation (in Grad).
 */
public abstract class SimulationMember {
    private final SimulationGraphic graphic;

    private String memberText = null;

    protected double x, y, rotation;

    public SimulationMember(double x, double y, double rotation, SimulationGraphic graphic) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.graphic = graphic;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRotation() {
        return rotation;
    }

    /**
     * Führt einen Simulationsschritt durch.
     */
    public void doSimulationStep() {

    }

    /**
     * Liefert die grafische Repräsentation dieses Simulationsteilnehmers. Es
     * wird immer die gleiche {@link SimulationGraphic} Instanz zurückgegeben.
     *
     * @return die grafische Repräsentation dieses Simulationsteilnehmers, nie
     * {@code null}
     */
    public SimulationGraphic getSimulationGraphic() {
        return graphic;
    }

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        return memberText;
    }
}

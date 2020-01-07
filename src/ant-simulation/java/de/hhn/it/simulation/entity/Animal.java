package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Genome;
import de.hhn.it.simulation.Helper;
import de.hhn.it.simulation.Simulation;
import de.hhn.it.ui.ImageFileGraphic;

/**
 * @author Cedric Seiz
 * Diese Klasse stellt einem Tier sämtliche methoden zur verfügung die es in der simulation benötigt. Dazu gehört das sich
 * innerhalb der Simulationsgrenzen bewegen, sich auf einen bestimmten Punkt zuzubewegen, sowie schaden nehmen und schaden
 * an anderen Tieren verursachen.
 */

public abstract class Animal extends SimulationMember {
    private static final double SURFACE_HEIGHT = Simulation.getSimulationSurfaceHeight();
    private static final double SURFACE_WIDTH = Simulation.getSimulationSurfaceWidth();
    private final Genome genome;

    private boolean reachedTarget;
    protected boolean hasTarget;
    private double targetX;
    private double targetY;
    private double step;
    private float healthPoints;
    private int cooldown;
    private int stun;

    private int nearTargetDistance;

    public Animal(double x, double y, double rotation, ImageFileGraphic graphic, Genome genome) {
        super(x, y, rotation, graphic);

        this.genome = genome;

        step = genome.getMovementSpeed();
        healthPoints = genome.getHealthPoints();

        this.reachedTarget = false;
        this.hasTarget = false;
        this.targetX = -50;
        this.targetY = -50;

        nearTargetDistance = 5;
    }

    /**
     * Methode um die Rotation der Ameise zu verändern. Sie verhindert das die Ameise sich aus den Simulationsgrenzen bewegt
     * und lässt sie wenn sie ein Ziel hat sich auf dieses zubewegen.
     */
    protected void rotationManipulator() {
        super.rotation = super.rotation % 360;
        if (super.rotation > 180) {
            super.rotation -= 360;
        } else if (super.rotation < -180) {
            super.rotation += 360;
        }

        double rnd = Helper.randomDouble(5);
        int rndOffset = 2;
        double rnd2 = Helper.randomDoubleUpperLowerBound(7);
        double navCos = Math.cos(Helper.degreeToRadian(super.rotation));
        double navSin = Math.sin(Helper.degreeToRadian(super.rotation));

        if ((50 > super.y) && (navSin > -0.25)) {
            if (navCos < 0) {
                super.rotation += 2 * rnd;
            } else if (navCos >= 0) {
                super.rotation -= rnd;
            }

        } else if ((SURFACE_HEIGHT - 50 < super.y) && (navSin < 0.25)) {
            if (navCos < 0) {
                super.rotation -= 2 * rnd;
            } else if (navCos >= 0) {
                super.rotation += rnd;
            }

        } else if ((50 > super.x) && (navCos < 0.25)) {
            if (navSin > 0) {
                super.rotation -= 2 * rnd;
            } else if (navSin <= 0) {
                super.rotation += 2 * rnd;
            }

        } else if ((SURFACE_WIDTH - 50 < super.x) && (navCos > -0.25)) {
            if (navSin > 0) {
                super.rotation += 2 * rnd;
            } else if (navSin <= 0) {
                super.rotation -= 2 * rnd;
            }

        } else if (hasTarget) {
            turnTo(targetX - super.x, targetY - super.y, rnd2, rndOffset);

        } else {
            /*
              Die normale zufällige Bewegung des Tieres wenn es weder in der Nähe der Simulationsgrenzen
              noch auf ein Ziel fixiert ist.
             */
            super.rotation += Helper.randomDoubleUpperLowerBound(5);
        }

        if (Helper.distance(targetX - super.x, targetY - super.y) < nearTargetDistance) {
            reachedTarget = true;
        }
    }

    /**
     * Methode setzt ein Ziel für die Ameise.
     */
    public void setNewTarget(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        reachedTarget = false;
    }

    /**
     * Bewegt das Tier einen STEP in die Richtung in die es schaut. Verringert zudem die beiden Zähler Cooldown und Stun um 1 da diese
     * Methode bei allem was sich bewegt ausgeführt wird.
     */
    protected void stepForward() {
        if (stun == 0) {
            super.x += (Math.cos(Helper.degreeToRadian(super.rotation)) * step);
            super.y += (-Math.sin(Helper.degreeToRadian(super.rotation)) * step);
        }
        if (cooldown > 0) {
            cooldown--;
        }
        if (stun > 0) {
            stun--;
        }
    }

    /**
     * @param diffX,diffY zu dem Tier relative Koordinaten des Ziels
     * @param rnd         Wert um den das Tier pro Schritt gedreht wird
     * @param rndOffset   zur Verschiebung des Wertebereichs wenn es sich bei dem rnd Wert um einen zufällig generierten
     *                    Wert handelt
     *                    <p>
     *                    Diese Methode dreht die Ameise schrittweise in Richtung der relativen Koordinaten.
     */
    protected void turnTo(double diffX, double diffY, double rnd, double rndOffset) {
        if (Math.sin(Math.toRadians(super.rotation + Helper.offset(diffX, diffY))) >= 0) {
            super.rotation -= rnd + rndOffset;
        } else {
            super.rotation += rnd + rndOffset;
        }
    }

    public boolean isNearTarget() {
        return reachedTarget;
    }

    public void setlockTarget(boolean lock) {
        hasTarget = lock;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public void setNearTargetDistance(int nearTargetDistance) {
        this.nearTargetDistance = nearTargetDistance;
    }

    /**
     * @param damageValue Verursachter Schaden
     * @return True, wenn der Schaden die Healthpoints des Tieres unter 0 bringt.
     */
    public boolean takeLethalDamage(float damageValue) {
        healthPoints -= damageValue;
        return healthPoints <= 0;
    }

    /**
     * @param target Das anzugreifende Animal
     * @return True wenn der Schaden für das angegriffene Animal tödlich ist.
     * Nach jedem Angriff ist eine Tier für eine drittel Sekunde etwa unfähig sich zu bewegen.
     */
    public boolean attack(Animal target) {
        if (cooldown == 0) {
            cooldown = Math.round(60 * genome.getAttackSpeed());
            stun = 20;
            return target.takeLethalDamage(genome.getAttackDamage());
        }
        return false;
    }

    public Genome getGenome() {
        return genome;
    }
}

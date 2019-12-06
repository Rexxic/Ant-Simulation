package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;

public abstract class Animal extends SimulationMember {
    private static final int STEP = 1;
    private static final double SURFACE_HEIGHT = Simulation.getSimulationSurfaceHeight();
    private static final double SURFACE_WIDTH = Simulation.getSimulationSurfaceWidth();

    private boolean reachedTarget;
    protected boolean hasTarget;
    private double targetX;
    private double targetY;

    public Animal(double x, double y, double rotation, ImageFileGraphic graphic) {
        super(x, y, rotation, graphic);

        this.reachedTarget = false;
        this.hasTarget = false;
        this.targetX = -50;
        this.targetY = -50;
    }

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

        if (((SURFACE_HEIGHT * 0.075) > super.y) && (navSin > -0.25)) {
            if (navCos < 0) {
                super.rotation += 2 * rnd;
            } else if (navCos >= 0) {
                super.rotation -= rnd;
            }

        } else if (((SURFACE_HEIGHT * 0.925) < super.y) && (navSin < 0.25)) {
            if (navCos < 0) {
                super.rotation -= 2 * rnd;
            } else if (navCos >= 0) {
                super.rotation += rnd;
            }

        } else if (((SURFACE_WIDTH * 0.05) > super.x) && (navCos < 0.25)) {
            if (navSin > 0) {
                super.rotation -= 2 * rnd;
            } else if (navSin <= 0) {
                super.rotation += 2 * rnd;
            }

        } else if (((SURFACE_WIDTH * 0.95) < super.x) && (navCos > -0.25)) {
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

        if (Helper.distance(targetX - super.x, targetY - super.y) < 5) {
            reachedTarget = true;
        }
    }

    public void setNewTarget(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
        reachedTarget = false;
    }

    /**
     * Bewegt das Tier einen STEP in die Richtung in die es schaut.
     */
    protected void stepForward() {
        super.x += (Math.cos(Helper.degreeToRadian(super.rotation)) * STEP);
        super.y += (-Math.sin(Helper.degreeToRadian(super.rotation)) * STEP);
    }

    /**
     * @param diffX,diffY zu dem Tier relative Koordinaten des Ziels
     * @param rnd         Wert um den das Tier pro Schritt gedreht wird
     * @param rndOffset   zur Verschiebung des Wertebereichs wenn es sich bei dem rnd Wert um einen zufällig generierten
     *                    Wert handelt
     *                    <p>
     *                    Diese Methode dreht die Ameise schrittweise in Richtung der relativen Koordinaten.
     */
    protected void turnTo(double diffX, double diffY, double rnd, int rndOffset) {
        if (Math.sin(Math.toRadians(super.rotation + Helper.offset(diffX, diffY))) >= 0) {
            super.rotation -= rnd + rndOffset;
        } else {
            super.rotation += rnd + rndOffset;
        }
    }

    public boolean isNearTarget() {
        return reachedTarget;
    }
}

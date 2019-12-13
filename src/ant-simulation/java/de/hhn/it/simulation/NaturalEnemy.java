package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;

import java.util.ArrayList;
import java.util.List;

public class NaturalEnemy extends Animal implements Reproduce<Food> {
    private int stash;
    private int sleep;

    public NaturalEnemy(double x, double y, double rotation) {
        super(x, y, rotation, new ImageFileGraphic("bug.png"));
        this.stash = 0;
        this.sleep = 0;
        this.setStep(1.5);
    }

    public void doSimulationStep() {
        super.rotationManipulator();
        if (sleep <= 0) {
            super.stepForward();
        } else {
            sleep--;
        }
    }

    public boolean isSleeping() {
        return sleep > 0;
    }

    public boolean isAwakening() {
        return sleep < 20;
    }

    @Override
    public List<Food> createChildren() {
        if (stash > 0) {
            List<Food> newFood = new ArrayList<>();
            newFood.add(new Food(super.x, super.y, stash));
            stash = 0;
            return newFood;
        }
        return null;
    }
}

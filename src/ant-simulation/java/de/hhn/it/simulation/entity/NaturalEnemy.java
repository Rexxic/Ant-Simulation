package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Genome;
import de.hhn.it.simulation.Helper;
import de.hhn.it.simulation.Reproduce;
import de.hhn.it.ui.ImageFileGraphic;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Cedric Seiz
 * Ein natürlicher Fressfeind der Ameisen, er schläft wenn er vollgefressen ist und produziert einen Futterhaufen
 * wenn er ausgeschlafen ist.
 */
public class NaturalEnemy extends Animal implements Reproduce<Food> {
    private int stash;
    private int sleep;
    private int maxStash;

    public NaturalEnemy(double x, double y, double rotation) {
        super(x, y, rotation, new ImageFileGraphic("ameisenbär.png"), new Genome());
        this.stash = 0;
        this.sleep = 0;
        maxStash = 50 + Helper.randomInt(50);
        this.setStep(1.4);
        setNearTargetDistance(20);
    }

    public void doSimulationStep() {
        super.rotationManipulator();
        if (sleep <= 0) {
            super.stepForward();
            if (stash > maxStash) {
                sleep = 10*stash;
            }
        } else {
            sleep--;
        }
    }

    public boolean isSleeping() {
        return sleep > 0;
    }

    public boolean isAwakening() {
        return sleep < 20 && sleep > 0;
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

    public void addFood() {
        stash += 6 + Helper.randomInt(4);
    }

    @Override
    public String getText() {
        if (isSleeping()) {
            return "Sleeping...";
        } else {
            return "Stash: " + stash;
        }
    }
}

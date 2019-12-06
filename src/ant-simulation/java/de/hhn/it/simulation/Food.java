package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;

public class Food extends SimulationMember {
    private String foodText;
    private int stash;
    private boolean hasFood;

    public Food(double x, double y, int stash) {
        super(x, y, Helper.randomInt(360), new ImageFileGraphic("food.png"));
        this.foodText = "Stash: ";
        this.stash = stash;
        this.hasFood = true;
    }

    boolean hasFood() {
        return hasFood;
    }

    boolean takeFood() {
        if (stash > 1) {
            stash--;
            return true;
        } else if (stash == 1) {
            stash--;
            hasFood = false;
            return true;
        } else {
            hasFood = false;
            return false;
        }
    }


    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        return this.foodText + this.stash;
    }
}
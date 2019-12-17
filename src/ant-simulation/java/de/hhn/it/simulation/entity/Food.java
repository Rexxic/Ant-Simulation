package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Helper;
import de.hhn.it.ui.ImageFileGraphic;

public class Food extends SimulationMember {
    private String foodText;
    private int stash;
    private boolean hasFood;

    /**
     * @Author Cedric Seiz
     * Diese Klasse stellt Futter in der Simulation dar.
     */
    public Food(double x, double y, int stash) {
        super(x, y, Helper.randomInt(360), new ImageFileGraphic("food.png"));
        this.foodText = "Stash: ";
        this.stash = stash;
        this.hasFood = true;
    }

    public boolean hasFood() {
        return hasFood;
    }

    /**
     * @param amount Menge des Futters, das genommen wird.
     * @return true wenn der Futterhaufen weiterexistiert, false wenn er leer ist und zerstört werden kann.
     */
    public boolean takeFood(int amount) {
        if (stash > amount) {
            stash-=amount;
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
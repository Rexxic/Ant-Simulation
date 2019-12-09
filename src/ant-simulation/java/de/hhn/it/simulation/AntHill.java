package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;

public class AntHill extends SimulationMember implements Reproduce<Ant> {
    private final Color workerColor;

    private ArrayList<Ant> antArrayList;
    private String antHillText;
    private double boost;
    private long lastChildrenCreation;
    private int stash;

    public AntHill(double x, double y, double rotation, Color color, int stash) {
        super(x, y, rotation, new ImageFileGraphic("ant-hill.png"));
        this.workerColor = color;

        this.antArrayList = new ArrayList<>();
        this.antHillText = "Stash: ";
        this.lastChildrenCreation = System.currentTimeMillis();
        this.stash = stash;
    }

    @Override
    public void doSimulationStep() {
        antArrayList.forEach(Ant::doSimulationStep);
    }

    @Override
    public List<Ant> createChildren() {
        List<Ant> newAntList = new ArrayList<>();
        if (System.currentTimeMillis() - lastChildrenCreation >= 9000 + Helper.randomInt(2000)) {
            for (int count = 0; count < stash / 10; count++) {
                newAntList.add(new Ant(super.x, super.y, Helper.randomDouble(360), boost, workerColor));
            }
            lastChildrenCreation = System.currentTimeMillis();
        }
        return newAntList;
    }

    public boolean removeChildren(Ant ant) {
        return antArrayList.remove(ant);
    }

    public void giveAntList(List<Ant> antList) {
        antArrayList.addAll(antList);
        stash -= antList.size() * 10;
    }

    public int getAntCount() {
        return antArrayList.size();
    }

    public List<Ant> getAntList() {
        return antArrayList;
    }

    public void giveFood() {
        stash++;
    }

    public void boost(double boost) {
        this.boost = boost;
    }

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        return this.antHillText + stash + "  Ants: " + antArrayList.size();
    }
}
package de.hhn.it.simulation;

import de.hhn.it.ui.ImageFileGraphic;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.List;

public class AntHill extends SimulationMember implements Reproduce<Ant> {
    private final Color workerColor;

    private ArrayList<Ant> antArrayList;
    private String antHillText;
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
            int fighter = 0;
            int worker = 0;
            boolean[] bool = {false,false};
            for(Ant ant:antArrayList) {
                if(ant.isFigther()) {fighter++;}
                else{worker++;}
            }
            fighter = fighter == 0 ? 1:fighter;
            if(worker/fighter>=5) {bool[0] = true;}
            for (int count = 0; count < stash / 10; count++) {
                Ant ant = new Ant(super.x, super.y, Helper.randomDouble(360), workerColor,bool[count%2]);
                ant.setStep(1.1);
                newAntList.add(ant);
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

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        return this.antHillText + stash + "  Ants: " + antArrayList.size();
    }
}
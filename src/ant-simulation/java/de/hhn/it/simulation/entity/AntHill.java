package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Genome;
import de.hhn.it.simulation.Helper;
import de.hhn.it.simulation.Reproduce;
import de.hhn.it.ui.ImageFileGraphic;
import javafx.scene.paint.Color;

import java.util.*;

public class AntHill extends SimulationMember implements Reproduce<Ant> {
    private final Color workerColor;
    private final Genome genome;

    private ArrayList<Ant> antArrayList;
    private long lastChildrenCreation;
    private int stash;
    private boolean terminateHill;


    public AntHill(double x, double y, double rotation, Color color, int stash, Genome genome) {
        super(x, y, rotation, new ImageFileGraphic("ant-hill.png"));
        this.workerColor = color;
        this.genome = genome;

        this.antArrayList = new ArrayList<>();
        this.lastChildrenCreation = System.currentTimeMillis();
        this.stash = stash;
        terminateHill = false;
    }

    @Override
    public void doSimulationStep() {
        if (antArrayList.size() >= genome.getAntLimit()) {
            terminateHill = true;
        } else if (antArrayList.size()==0 && stash < genome.getFoodConsumption()) {
            terminateHill = true;
        } else {
            antArrayList.forEach(Ant::doSimulationStep);
        }
    }

    @Override
    public List<Ant> createChildren() {
        List<Ant> newAntList = new ArrayList<>();
        if (System.currentTimeMillis() - lastChildrenCreation >= 9000 + Helper.randomInt(2000)) {
            int fighter = 0;
            int worker = 0;
            boolean[] bool = {false, false};
            for (Ant ant : antArrayList) {
                if (ant.isFigther()) {
                    fighter++;
                } else {
                    worker++;
                }
            }
            fighter = fighter == 0 ? 1 : fighter;
            if (worker / (float) fighter >= genome.getFighterRatio()) {
                bool[0] = true;
            }
            for (int count = Math.round(genome.getFoodConsumption()); count < stash / genome.getFoodConsumption(); count++) {
                Ant ant = new Ant(super.x, super.y, Helper.randomDouble(360), workerColor, bool[count % 2], genome);
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
        stash -= antList.size() * genome.getFoodConsumption();
    }

    public int getAntCount() {
        return antArrayList.size();
    }

    public List<Ant> getAntList() {
        return antArrayList;
    }

    public void giveFood() {
        stash += genome.getCapacity();
    }

    public boolean isTerminateHill() {
        return terminateHill;
    }

    /**
     * @return Beschreibungstext, kann {@code null} sein.
     */
    public String getText() {
        return "Stash: " + stash + "  Ants: " + antArrayList.size() + "\n" + genome.getText();
    }

    public Genome getGenome() {
        return genome;
    }
}
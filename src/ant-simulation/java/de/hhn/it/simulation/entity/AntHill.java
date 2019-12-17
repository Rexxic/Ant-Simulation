package de.hhn.it.simulation.entity;

import de.hhn.it.simulation.Genome;
import de.hhn.it.simulation.Helper;
import de.hhn.it.simulation.Reproduce;
import de.hhn.it.ui.ImageFileGraphic;
import javafx.scene.paint.Color;
import java.util.*;

/**
 * @Author: Cedric Seiz
 * Diese Klasse beschreibt Ameisenhügel. Sie kann Einheiten des Types Ant reproduzieren und zerstört sich selbst wenn sie entweder
 * ihr limit erreicht hat, oder wenn sie kein Futter mehr hat. Von Simulation werden dann Queens erzeugt je nachdem wie viel Futter
 * und wie viele Ameisen der Ameisenhfufen bei seiner Zersörung beherbergt.
 */
public class AntHill extends SimulationMember implements Reproduce<Ant> {
    private final Color workerColor;
    private final Genome genome;

    private ArrayList<Ant> antArrayList;
    private long lastChildrenCreation;
    private int stash;
    private int feedCount;
    private boolean terminateHill;


    public AntHill(double x, double y, double rotation, Color color, int stash, Genome genome) {
        super(x, y, rotation, new ImageFileGraphic("ant-hill.png"));
        this.workerColor = color;
        this.genome = genome;

        this.antArrayList = new ArrayList<>();
        this.lastChildrenCreation = System.currentTimeMillis();
        this.stash = stash;
        this.stash += genome.getThreshold();
        this.terminateHill = false;
        this.feedCount = 600;
    }

    /**
     * Führt einen Simulationstep aus was für den Ameisenhaufen bedeutet, dass er überprüft ob seine Ameisenliste
     * leer ist und er kein Futter hat oder er seine maximale Größe erreicht. In diesen Fällen setzt er terminateHill
     * auf true worauf die Simulation diesen Ameisenhaufen auflößt und wenn er Ameisen beinhaltet Queens spawnt die,
     * die sein Genom erben. Ausserdem führt diese Methode einen Simulationsschritt für jede Ameise aus und zieht ihm
     * etwa alle 10 Sekunden ein wenig Futter relativ zu seiner Größe ab.
     */
    @Override
    public void doSimulationStep() {
        if (antArrayList.size() >= genome.getAntLimit() || (stash <= 0 && antArrayList.isEmpty())) {
            terminateHill = true;
        } else if (stash < 0) {
            removeChildren(antArrayList.get(Helper.randomInt(antArrayList.size())));
            giveFood();
        } else {
            antArrayList.forEach(Ant::doSimulationStep);
        }
        feedCount--;
        if(feedCount<=0) {
            stash -= genome.getFoodConsumption() * 0.01 * getAntCount();
            feedCount = 550 + Helper.randomInt(100);
        }
    }

    /**
     * @return Eine Liste an Ameisen die erzeugt werden sollen.
     */
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
            for (int count = 1; count < (stash - genome.getThreshold()) / genome.getFoodConsumption(); count++) {
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
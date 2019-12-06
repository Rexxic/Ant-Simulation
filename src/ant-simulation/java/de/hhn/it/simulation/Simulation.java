package de.hhn.it.simulation;

import de.hhn.it.ui.AntApplication;
import de.hhn.it.ui.UiManager;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Dies Klasse bildet das zentrale Element der Simulation. Sie koordiniert die
 * Bewegungen, das Erscheinen und Verschwinden der Simulationsteilnehmer. Die
 * Methode {@link Simulation#doSimulationStep()} wird vom Taktgeber der
 * grafischen Oberfläche in regelmäßigen Abständen aufgerufen.
 *
 * @see AntApplication#SIMULATION_FRAME_LENGTH
 */
public class Simulation {
    private static final int ANTHILL_COUNT = 4;
    private static final int FOOD_COUNT = 6;
    private static final int MIN_ANT_COUNT = 80;
    private static final int MAX_ANT_COUNT = 400;
    private static final int FIELD_OF_VIEW = 50;

    private static double simulationSurfaceHeight;
    private static double simulationSurfaceWidth;

    private UiManager uiManager;
    private ArrayList<Food> foodArrayList;
    private ArrayList<AntHill> antHillArrayList;
    private HashMap<Ant, Ant[]> antAntArrayHashMap;
    private HashMap<Ant, Food> antFoodHashMap;

    public Simulation(UiManager uiManager) {
        this.uiManager = uiManager;
        this.foodArrayList = new ArrayList<>();
        this.antHillArrayList = new ArrayList<>();
        this.antAntArrayHashMap = new HashMap<>();
        this.antFoodHashMap = new HashMap<>();

        Simulation.simulationSurfaceHeight = uiManager.getSimulationSurfaceHeight();
        Simulation.simulationSurfaceWidth = uiManager.getSimulationSurfaceWidth();

        for (int a = 0; a < ANTHILL_COUNT; a++) {
            double x = simulationSurfaceWidth / 2 + Helper.randomDoubleUpperLowerBound(simulationSurfaceWidth / 3);
            double y = simulationSurfaceHeight / 2 + Helper.randomDoubleUpperLowerBound(simulationSurfaceHeight / 3);
            int startSize = MIN_ANT_COUNT / ANTHILL_COUNT + Helper.randomInt(10);
            Color color = Helper.nxtColor();
            AntHill antHill = new AntHill(x, y, Helper.randomInt(180) - 90, color, startSize * 10);
            antHillArrayList.add(antHill);
            uiManager.add(antHill);

            List<Ant> newAntList = new ArrayList<>();
            for (int count = 0; count < startSize; count++) {
                Ant ant = new Ant(x, y, Helper.randomDouble(360), color);
                newAntList.add(ant);
                uiManager.add(ant);
            }
            antHill.giveAntList(newAntList);
        }
    }

    /**
     * Führt einen Simulationsschritt durch.
     */
    public void doSimulationStep() {
        final int[] antCount = {0};

        antAntArrayHashMap.clear();
        antHillArrayList.forEach(AntHill -> antCount[0] += AntHill.getAntCount());

        if (antCount[0] != 0) {
            while (foodArrayList.size() < FOOD_COUNT) {
                double x = 0.05 * simulationSurfaceWidth + Helper.randomDouble(simulationSurfaceWidth * 0.9);
                double y = 0.075 * simulationSurfaceHeight + Helper.randomDouble(simulationSurfaceHeight * 0.85);

                Food food = new Food(x, y, 10 + Helper.randomInt(antCount[0] / FOOD_COUNT));
                uiManager.add(food);
                foodArrayList.add(food);
            }
        }

        for (AntHill antHill : antHillArrayList) {
            antCount[0] = createChildren(antHill, antCount[0]);

            for (Ant ant : antHill.getAntList()) {
                updateAntAntArrayHashMap(ant, antHill.getAntList());
                updateAntFoodHashMap(ant);
                antMovementHandler(ant, antHill);
            }
            antHill.doSimulationStep();
        }
    }

    public int createChildren(AntHill antHill, int antCount) {
        List<Ant> antList = antHill.createChildren();
        int antCountDiff = MAX_ANT_COUNT - antCount;
        if (!antList.isEmpty() && antCountDiff > 0) {
            if (antList.size() <= antCountDiff) {
                antList.forEach(Ant -> uiManager.add(Ant));
                antHill.giveAntList(antList);
                antCount += antCountDiff;
            } else {
                antList.subList(0, antCountDiff).forEach(Ant -> uiManager.add(Ant));
                antHill.giveAntList(antList.subList(0, antCountDiff));
                antCount += antList.size();
            }
        }
        return antCount;
    }

    public void updateAntAntArrayHashMap(Ant ant, List<Ant> antList) {
        int count = 0;
        Ant[] antArray = new Ant[5];

        for (Ant antTarget : antList) {
            if (!ant.equals(antTarget) && count < 5 && computeDistance(ant, antTarget) <= FIELD_OF_VIEW) {
                antArray[count] = antTarget;
                count++;
            } else if (count >= 5) {
                break;
            }
        }
        antAntArrayHashMap.put(ant, antArray);
    }

    public void updateAntFoodHashMap(Ant ant) {
        if (!antFoodHashMap.containsKey(ant)) {
            for (Food food : foodArrayList) {
                if (computeDistance(ant, food) <= FIELD_OF_VIEW * 2) {
                    antFoodHashMap.put(ant, food);
                    break;
                }
            }
        }
    }

    public void antMovementHandler(Ant ant, AntHill antHill) {
        if (!antFoodHashMap.containsKey(ant)) {
            for (Ant nearAnt : antAntArrayHashMap.get(ant)) {
                if (antFoodHashMap.containsKey(nearAnt)) {
                    Food nearAntFood = antFoodHashMap.get(nearAnt);
                    if (nearAntFood != null && foodArrayList.contains(nearAntFood)) {
                        antFoodHashMap.put(ant, nearAntFood);
                        break;
                    }
                }
            }
        }

        Food food = antFoodHashMap.get(ant);

        if (ant.isNearTarget() && ant.isCarryingFood()) {
            ant.isCarryingFood(false);
            antHill.giveFood();
        } else if (ant.isNearTarget()) {
            if (food != null && foodArrayList.contains(food) && food.takeFood()) {
                ant.isCarryingFood(true);
                if (!food.hasFood()) {
                    antFoodHashMap.remove(ant);
                    uiManager.remove(food);
                    foodArrayList.remove(food);
                    food = null;
                    ant.setNewTarget(-50, -50);
                }
            } else {
                antFoodHashMap.remove(ant);
                food = null;
                ant.setNewTarget(-50, -50);
            }
        }

        if (food != null) {
            ant.lock(true);
            if (!ant.isCarryingFood()) {
                ant.setNewTarget(food.getX(), food.getY());
            } else ant.setNewTarget(antHill.getX(), antHill.getY());
        } else if (!ant.isCarryingFood()) {
            ant.lock(false);
        } else ant.setNewTarget(antHill.getX(), antHill.getY());
    }

    public double computeDistance(SimulationMember member1, SimulationMember member2) {
        return Helper.distance(member1.getX() - member2.getX(), member1.getY() - member2.getY());
    }

    public static double getSimulationSurfaceHeight() {
        return simulationSurfaceHeight;
    }

    public static double getSimulationSurfaceWidth() {
        return simulationSurfaceWidth;
    }
}

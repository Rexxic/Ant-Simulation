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
    private static final int ANTHILL_COUNT = 5;
    private static final int FOOD_COUNT = 20;
    private static final int MIN_ANT_COUNT = 200;
    private static final int MAX_ANT_COUNT = 800;
    private static final int FIELD_OF_VIEW = 200;

    private static double simulationSurfaceHeight;
    private static double simulationSurfaceWidth;

    private UiManager uiManager;
    private ArrayList<Food> foodArrayList;
    private ArrayList<AntHill> antHillArrayList;
    private HashMap<Ant, Ant[]> antAntArrayHashMap;
    private HashMap<Ant, Food> antFoodHashMap;
    private HashMap<Animal, Animal> predatorHashMap;

    public Simulation(UiManager uiManager) {
        this.uiManager = uiManager;
        this.foodArrayList = new ArrayList<>();
        this.antHillArrayList = new ArrayList<>();
        this.antAntArrayHashMap = new HashMap<>();
        this.antFoodHashMap = new HashMap<>();
        this.predatorHashMap = new HashMap<>();

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
                Ant ant = new Ant(x, y, Helper.randomDouble(360), color, false);
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
        final List<Animal> allAntArrayList = new ArrayList<>();

        antAntArrayHashMap.clear();
        antHillArrayList.forEach(AntHill -> {
            antCount[0] += AntHill.getAntCount();
            allAntArrayList.addAll(AntHill.getAntList());
        });

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
            List<Ant> antHillAntList = antHill.getAntList();
            allAntArrayList.removeAll(antHillAntList);
            for (Ant ant : antHillAntList) {
                if (ant.isFigther()) {
                    updateAntAntArrayHashMap(ant, antHillAntList);
                    updatePredatorHashMap(ant, allAntArrayList);
                    fighterAntMovementHandler(ant, antHill);
                } else {
                    updateAntAntArrayHashMap(ant, antHillAntList);
                    updateAntFoodHashMap(ant);
                    commonAntMovementHandler(ant, antHill);
                }
            }
            allAntArrayList.addAll(antHillAntList);
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
            if (!ant.equals(antTarget) && count < 5 && calculateDistance(ant, antTarget) <= FIELD_OF_VIEW) {
                antArray[count] = antTarget;
                count++;
            } else if (count >= 5) {
                break;
            }
        }
        antAntArrayHashMap.put(ant, antArray);
    }

    public void updatePredatorHashMap(Animal animal, List<Animal> targetList) {
        double minDistance = FIELD_OF_VIEW;
        boolean somethingNear = false;
        for (Animal target : targetList) {
            double distance = calculateDistance(animal, target);
            if (distance < minDistance) {
                predatorHashMap.put(animal, target);
                minDistance = distance;
                somethingNear = true;
            }
        }
        if (!somethingNear) {
            predatorHashMap.remove(animal);
        }
    }

    public void updateAntFoodHashMap(Ant ant) {
        if (!antFoodHashMap.containsKey(ant)) {
            for (Food food : foodArrayList) {
                if (calculateDistance(ant, food) <= FIELD_OF_VIEW) {
                    antFoodHashMap.put(ant, food);
                    break;
                }
            }
        }
    }

    public void commonAntMovementHandler(Ant ant, AntHill antHill) {
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

        antSetTarget(ant, food, antHill);
    }

    SimulationMember finalTarget;

    public void fighterAntMovementHandler(Ant ant, AntHill antHill) {
        Ant target;
        if (predatorHashMap.containsKey(ant) && predatorHashMap.get(ant).getClass() == Ant.class) {
            target = (Ant) predatorHashMap.get(ant);
        } else {
            target = null;
        }

        if (ant.isNearTarget() && ant.isCarryingFood()) {
            ant.isCarryingFood(false);
            antHill.giveFood();
        } else if (target != null & !ant.isCarryingFood()) {
            if (calculateDistance(ant, target) <= 5) {
                for (AntHill targetAntHill : antHillArrayList) {
                    if (targetAntHill.removeChildren(target)) {
                        ant.isCarryingFood(true);
                        predatorHashMap.remove(ant);
                        uiManager.remove(target);
                        target = null;
                        break;
                    }
                }
            }
            finalTarget = target;
        } else {
            for (Ant nearAnt : antAntArrayHashMap.get(ant)) {
                if (antFoodHashMap.containsKey(nearAnt)) {
                    finalTarget = antFoodHashMap.get(nearAnt);
                    if (finalTarget != null && foodArrayList.contains(finalTarget)) {
                        break;
                    }
                }
            }
        }
        antSetTarget(ant, finalTarget, antHill);
    }

    private void antSetTarget(Ant ant, SimulationMember target, SimulationMember antHill) {
        if (target != null) {
            ant.lock(true);
            if (!ant.isCarryingFood()) {
                ant.setNewTarget(target.getX(), target.getY());
            } else ant.setNewTarget(antHill.getX(), antHill.getY());
        } else if (!ant.isCarryingFood()) {
            ant.lock(false);
        } else ant.setNewTarget(antHill.getX(), antHill.getY());
    }

    public double calculateDistance(SimulationMember member1, SimulationMember member2) {
        return Helper.distance(member1.getX() - member2.getX(), member1.getY() - member2.getY());
    }

    public static double getSimulationSurfaceHeight() {
        return simulationSurfaceHeight;
    }

    public static double getSimulationSurfaceWidth() {
        return simulationSurfaceWidth;
    }
}

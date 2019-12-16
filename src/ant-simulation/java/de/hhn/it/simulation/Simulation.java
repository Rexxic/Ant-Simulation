package de.hhn.it.simulation;

import de.hhn.it.simulation.entity.*;
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
    private static final int FOOD_COUNT = 25;
    private static final int MIN_ANT_COUNT = 200;
    private static final int MAX_ANT_COUNT = 1000;
    private static final int NATURAL_ENEMY_COUNT = 2;
    private static final int FIELD_OF_VIEW = 150;
    private static final int FOOD_STASH_MULTIPLICAND = 5;

    private static double simulationSurfaceHeight;
    private static double simulationSurfaceWidth;

    private UiManager uiManager;
    private ArrayList<Food> foodArrayList;
    private ArrayList<AntHill> antHillList;
    private ArrayList<NaturalEnemy> naturalEnemyList;
    private ArrayList<Queen> queenList;
    private HashMap<Ant, Ant[]> antAntArrayHashMap;
    private HashMap<Ant, Food> antFoodHashMap;
    private HashMap<Animal, Animal> predatorHashMap;

    public Simulation(UiManager uiManager) {
        this.uiManager = uiManager;

        this.foodArrayList = new ArrayList<>();
        this.antHillList = new ArrayList<>();
        this.naturalEnemyList = new ArrayList<>();
        this.queenList = new ArrayList<>();

        this.antAntArrayHashMap = new HashMap<>();
        this.antFoodHashMap = new HashMap<>();
        this.predatorHashMap = new HashMap<>();

        Simulation.simulationSurfaceHeight = uiManager.getSimulationSurfaceHeight();
        Simulation.simulationSurfaceWidth = uiManager.getSimulationSurfaceWidth();

        for (int a = 0; a < ANTHILL_COUNT; a++) {
            double x = rndWidth(1 / 16f);
            double y = rndHeight(1 / 9f);
            int startSize = MIN_ANT_COUNT / ANTHILL_COUNT + Helper.randomInt(10);

            Color color = Helper.nxtColor();
            Genome genome = new Genome();
            AntHill antHill = new AntHill(x, y, Helper.randomInt(180) - 90, color, Math.round(startSize * genome.getFoodConsumption()), genome);
            antHillList.add(antHill);
            uiManager.add(antHill);

            List<Ant> newAntList = new ArrayList<>();
            for (int count = 0; count < startSize; count++) {
                Ant ant = new Ant(x, y, Helper.randomDouble(360), color, false, antHill.getGenome());
                newAntList.add(ant);
                uiManager.add(ant);
            }
            antHill.giveAntList(newAntList);
        }

        for (int b = 0; b < NATURAL_ENEMY_COUNT; b++) {
            double x = rndWidth(50 / simulationSurfaceWidth);
            double y = rndHeight(50 / simulationSurfaceHeight);
            NaturalEnemy naturalEnemy = new NaturalEnemy(x, y, Helper.randomDouble(360));
            naturalEnemyList.add(naturalEnemy);
            uiManager.add(naturalEnemy);
        }
    }

    /**
     * Führt einen Simulationsschritt durch.
     */
    public void doSimulationStep() {
        final int[] antCount = {0};
        final List<Animal> allAntArrayList = new ArrayList<>();

        antAntArrayHashMap.clear();
        antHillList.forEach(AntHill -> {
            antCount[0] += AntHill.getAntCount();
            allAntArrayList.addAll(AntHill.getAntList());
        });

        if (antCount[0] != 0) {
            while (foodArrayList.size() < FOOD_COUNT) {
                double x = rndWidth(50 / simulationSurfaceWidth);
                double y = rndHeight(50 / simulationSurfaceHeight);

                Food food = new Food(x, y, 10 + Helper.randomInt(antCount[0] / FOOD_COUNT * FOOD_STASH_MULTIPLICAND));
                uiManager.add(food);
                foodArrayList.add(food);
            }
        }
        ArrayList<AntHill> deleteAntHillList = new ArrayList<>();
        for (AntHill antHill : antHillList) {
            List<Ant> antHillAntList = antHill.getAntList();
            allAntArrayList.removeAll(antHillAntList);
            if (antHill.isTerminateHill()) {
                terminateAntHill(antHill);
                deleteAntHillList.add(antHill);
            } else {
                antCount[0] = createChildren(antHill, antCount[0]);
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
        for (AntHill antHill : deleteAntHillList) {
            antHillList.remove(antHill);
            uiManager.remove(antHill);
        }

        for (NaturalEnemy naturalEnemy : naturalEnemyList) {
            updatePredatorHashMap(naturalEnemy, allAntArrayList);
            naturalEnemyMovementHandler(naturalEnemy);
            naturalEnemyCreateChildren(naturalEnemy);
            naturalEnemy.doSimulationStep();
        }

        ArrayList<Queen> deleteQueenList = new ArrayList<>();
        for (Queen queen : queenList) {
            if (queen.isNearTarget()) {
                deleteQueenList.add(queen);
            } else {
                queen.doSimulationStep();
            }
        }
        for (Queen queen : deleteQueenList) {
            AntHill antHill = new AntHill(queen.getX(), queen.getY(), queen.getRotation(), queen.getColor(), queen.getStash(), queen.getGenome());
            antHillList.add(antHill);
            uiManager.add(antHill);
            queenList.remove(queen);
            uiManager.remove(queen);
        }
    }

    public int createChildren(AntHill antHill, int antCount) {
        List<Ant> antList = antHill.createChildren();
        int antCountDiff = MAX_ANT_COUNT - antCount;
        if (!antList.isEmpty() && antCountDiff > 0) {
            if (antList.size() <= antCountDiff) {
                antList.forEach(Ant -> uiManager.add(Ant));
                antHill.giveAntList(antList);
                antCount += antList.size();
            } else {
                antList.subList(0, antCountDiff).forEach(Ant -> uiManager.add(Ant));
                antHill.giveAntList(antList.subList(0, antCountDiff));
                antCount += antCountDiff;
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
            if (food != null && foodArrayList.contains(food) && food.takeFood(ant.getGenome().getCapacity())) {
                ant.isCarryingFood(true);
            } else if (food != null && foodArrayList.contains(food) && !food.hasFood()) {
                antFoodHashMap.remove(ant);
                uiManager.remove(food);
                foodArrayList.remove(food);
                food = null;
                ant.setNewTarget(-50, -50);
            } else {
                antFoodHashMap.remove(ant);
                food = null;
                ant.setNewTarget(-50, -50);
            }
        }

        antSetTarget(ant, food, antHill);
    }

    public void fighterAntMovementHandler(Ant ant, AntHill antHill) {
        Ant target = null;
        SimulationMember finalTarget = null;

        if (predatorHashMap.containsKey(ant) && predatorHashMap.get(ant) instanceof Ant) {
            target = (Ant) predatorHashMap.get(ant);
        }

        if (ant.isNearTarget() && ant.isCarryingFood()) {
            ant.isCarryingFood(false);
            antHill.giveFood();
        } else if (target != null & !ant.isCarryingFood()) {
            if (calculateDistance(ant, target) <= 5) {
                if (ant.attack(target)) {
                    for (AntHill targetAntHill : antHillList) {
                        if (targetAntHill.removeChildren(target)) {
                            ant.isCarryingFood(true);
                            predatorHashMap.remove(ant);
                            uiManager.remove(target);
                            target = null;
                            break;
                        }
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

    public void naturalEnemyMovementHandler(NaturalEnemy naturalEnemy) {
        Ant target = null;
        if (predatorHashMap.containsKey(naturalEnemy) && predatorHashMap.get(naturalEnemy) instanceof Ant) {
            target = (Ant) predatorHashMap.get(naturalEnemy);
        }
        if (!naturalEnemy.isSleeping()) {
            if (target != null && naturalEnemy.isNearTarget()) {
                if (calculateDistance(naturalEnemy, target) <= 10) {
                    if (naturalEnemy.attack(target)) {
                        for (AntHill targetAntHill : antHillList) {
                            if (targetAntHill.removeChildren(target)) {
                                naturalEnemy.addFood();
                                predatorHashMap.remove(naturalEnemy);
                                uiManager.remove(target);
                                target = null;
                                break;
                            }
                        }
                    }
                }
            }
            if (target != null) {
                naturalEnemy.setNewTarget(target.getX(), target.getY());
                naturalEnemy.setlockTarget(true);
            } else {
                naturalEnemy.setlockTarget(false);
            }
        }
    }

    private void naturalEnemyCreateChildren(NaturalEnemy naturalEnemy) {
        if (naturalEnemy.isSleeping()) {
            if (naturalEnemy.isAwakening()) {
                List<Food> newFood = naturalEnemy.createChildren();
                if (newFood != null) {
                    Food food = newFood.get(0);
                    foodArrayList.add(food);
                    uiManager.add(food);
                }
            }
        }
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

    private void terminateAntHill(AntHill antHill) {
        int antHillWorkers = 0;
        for (Ant ant : antHill.getAntList()) {
            antHillWorkers++;
            uiManager.remove(ant);
        }
        int newQueen = Math.round(antHillWorkers / (antHill.getGenome().getFoodConsumption() * 4));
        for (int i = 0; i < newQueen; i++) {
            Queen queen = new Queen(antHill.getX(), antHill.getY(), Helper.randomDouble(360), Helper.nxtColor(), Math.round(antHillWorkers / (float) newQueen), antHill.getGenome());
            queen.setNewTarget(rndWidth(1 / 16f), rndHeight(1 / 9f));
            queenList.add(queen);
            uiManager.add(queen);
        }
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

    /**
     * @param excludedPercent if<0=0; if>=1=1;>
     */
    private static double rndHeight(double excludedPercent) {
        if (excludedPercent <= 0) {
            excludedPercent = 0;
        } else if (excludedPercent >= 1) {
            excludedPercent = 1;
        }

        return excludedPercent / 2 * simulationSurfaceHeight + Helper.randomDouble(simulationSurfaceHeight * (1 - excludedPercent));
    }

    /**
     * @param excludedPercent if<0=0; if>=1=1;>
     */
    private static double rndWidth(double excludedPercent) {
        if (excludedPercent <= 0) {
            excludedPercent = 0;
        } else if (excludedPercent >= 1) {
            excludedPercent = 1;
        }

        return excludedPercent / 2 * simulationSurfaceWidth + Helper.randomDouble(simulationSurfaceWidth * (1 - excludedPercent));
    }
}

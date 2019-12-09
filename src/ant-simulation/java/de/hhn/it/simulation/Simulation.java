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
    private enum AntHillType {CommonAntHill, FireAntHill}

    private static final int COMMON_ANTHILL_COUNT = 4;
    private static final int FIRE_ANTHILL_COUNT = 1;
    private static final int FOOD_COUNT = 20;
    private static final int MIN_ANT_COUNT = 200;
    private static final int MAX_ANT_COUNT = 800;
    private static final int FIELD_OF_VIEW = 100;

    private static double simulationSurfaceHeight;
    private static double simulationSurfaceWidth;

    private UiManager uiManager;
    private ArrayList<Food> foodArrayList;
    private ArrayList<AntHill> commonAntHillArrayList;
    private ArrayList<AntHill> fireAntHillArrayList;
    private HashMap<Ant, Ant[]> antAntArrayHashMap;
    private HashMap<Ant, Food> antFoodHashMap;
    private HashMap<Animal, Animal> predatorHashMap;

    public Simulation(UiManager uiManager) {
        this.uiManager = uiManager;
        this.foodArrayList = new ArrayList<>();
        this.commonAntHillArrayList = new ArrayList<>();
        this.fireAntHillArrayList = new ArrayList<>();
        this.antAntArrayHashMap = new HashMap<>();
        this.antFoodHashMap = new HashMap<>();
        this.predatorHashMap = new HashMap<>();

        Simulation.simulationSurfaceHeight = uiManager.getSimulationSurfaceHeight();
        Simulation.simulationSurfaceWidth = uiManager.getSimulationSurfaceWidth();

        creatAntHills(AntHillType.FireAntHill);
        creatAntHills(AntHillType.CommonAntHill);
    }

    public void creatAntHills(AntHillType anthillType) {
        int antHillCount = anthillType == AntHillType.FireAntHill ? FIRE_ANTHILL_COUNT : COMMON_ANTHILL_COUNT;

        for (int a = 0; a < antHillCount; a++) {
            double x = simulationSurfaceWidth / 2 + Helper.randomDoubleUpperLowerBound(simulationSurfaceWidth / 3);
            double y = simulationSurfaceHeight / 2 + Helper.randomDoubleUpperLowerBound(simulationSurfaceHeight / 3);
            double boost = 0;
            int startSize = MIN_ANT_COUNT / COMMON_ANTHILL_COUNT + Helper.randomInt(10);

            AntHill antHill;
            Color color;
            if (anthillType == AntHillType.FireAntHill) {
                color = Helper.redColor();
                startSize /= 8;
                antHill = new AntHill(x, y, Helper.randomInt(180) - 90, color, startSize * 10);
                fireAntHillArrayList.add(antHill);
                boost = 0.1;
                antHill.boost(boost);
            } else {
                color = Helper.nxtColor();
                antHill = new AntHill(x, y, Helper.randomInt(180) - 90, color, startSize * 10);
                commonAntHillArrayList.add(antHill);
            }
            uiManager.add(antHill);

            List<Ant> newAntList = new ArrayList<>();
            for (int count = 0; count < startSize; count++) {
                Ant ant = new Ant(x, y, Helper.randomDouble(360), boost, color);
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
        final List<Animal> commonAntArrayList = new ArrayList<>();
        final List<Animal> fireAntArrayList = new ArrayList<>();

        antAntArrayHashMap.clear();
        commonAntHillArrayList.forEach(AntHill -> {
            antCount[0] += AntHill.getAntCount();
            commonAntArrayList.addAll(AntHill.getAntList());
        });

        fireAntHillArrayList.forEach(AntHill -> {
            antCount[0] += AntHill.getAntCount();
            fireAntArrayList.addAll(AntHill.getAntList());
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

        for (AntHill antHill : commonAntHillArrayList) {
            antCount[0] = createChildren(antHill, antCount[0]);
            List<Ant> antHillAntList = antHill.getAntList();
            for (Ant ant : antHill.getAntList()) {
                updateAntAntArrayHashMap(ant, antHillAntList);
                updateAntFoodHashMap(ant);
                commonAntMovementHandler(ant, antHill);
            }
            antHill.doSimulationStep();
        }

        for (AntHill antHill : fireAntHillArrayList) {
            antCount[0] = createChildren(antHill, antCount[0]);

            for (Ant ant : antHill.getAntList()) {
                updatePredatorHashMap(ant, commonAntArrayList);
                fireAntMovementHandler(ant, antHill);
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

    public void fireAntMovementHandler(Ant ant, AntHill antHill) {
        Ant target;

        if (predatorHashMap.containsKey(ant) && predatorHashMap.get(ant).getClass() == Ant.class) {
            target = (Ant) predatorHashMap.get(ant);
        } else {
            target = null;
        }

        if (ant.isNearTarget() && ant.isCarryingFood()) {
            ant.isCarryingFood(false);
            antHill.giveFood();
        } else if (target != null &! ant.isCarryingFood()){
            if (calculateDistance(ant,target) <= 5) {
                for (AntHill targetAntHill : commonAntHillArrayList) {
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
        antSetTarget(ant, target, antHill);
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

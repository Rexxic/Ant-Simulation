package de.hhn.it.simulation;

import de.hhn.it.simulation.entity.SimulationMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntityMap {
    private ArrayList<SimulationMember>[][] allEntityMap;
    private HashMap<SimulationMember,int[]> register;
    private int arraySizeX;
    private int arraySizeY;

    public EntityMap(int gridWidth, int gridLength) {
        this.allEntityMap = new ArrayList[gridLength][gridWidth];
        this.register = new HashMap<>();
        this.arraySizeX = gridWidth;
        this.arraySizeY = gridLength;
    }

    public void put(SimulationMember simulationMember) {
        int x = calcFieldX(simulationMember);
        int y = calcFieldY(simulationMember);
        if(register.containsKey(simulationMember)) {
            int[] coords = register.get(simulationMember);
            register.remove(simulationMember);
            allEntityMap[coords[0]][coords[1]].remove(simulationMember);
        }
        allEntityMap[x][y].add(simulationMember);
        register.put(simulationMember, new int[]{x, y});
    }

    public List<SimulationMember> getNearAntList(SimulationMember simulationMember, Class filterClass) {
        int x = calcFieldX(simulationMember);
        int y = calcFieldY(simulationMember);

        int xCount;
        int xEnd;
        int yCount;
        int yEnd;

        if (x <= 0) {
            xCount = 0;
            xEnd = xCount + 2;
        } else if (x >= arraySizeX - 1) {
            xCount = arraySizeX - 2;
            xEnd = arraySizeX;
        } else {
            xCount = x - 1;
            xEnd = x + 2;
        }

        if (y <= 0) {
            yCount = 0;
            yEnd = yCount + 2;
        } else if (y >= arraySizeY - 1) {
            yCount = arraySizeY - 2;
            yEnd = arraySizeY;
        } else {
            yCount = y - 1;
            yEnd = y + 2;
        }

        List<SimulationMember> outputList = new ArrayList<>();

        while (xCount < xEnd) {
            while (yCount < yEnd) {
                allEntityMap[x][y].forEach(C -> {
                    if(C.getClass().equals(filterClass)) {
                        outputList.add(C);
                    }});
                yCount++;
            }
            xCount++;
        }
        return outputList;
    }

    private int calcFieldX(SimulationMember simulationMember) {
        return Math.floorDiv((int) simulationMember.getX(), arraySizeX);
    }

    private int calcFieldY(SimulationMember simulationMember) {
        return Math.floorDiv((int) simulationMember.getY(), arraySizeY);
    }
}

package de.hhn.it.simulation;

import de.hhn.it.simulation.entity.SimulationMember;

import java.util.ArrayList;

public class EntityMap {
    private ArrayList<SimulationMember>[][] allEntityMap;
    private int arraySizeX;
    private int arraySizeY;

    public EntityMap(int gridWidth, int gridLength) {
        this.arraySizeX = gridWidth;
        this.arraySizeY = gridLength;

        this.allEntityMap = new ArrayList[gridWidth][gridLength];
    }

    public void put(SimulationMember simulationMember) {
        int x = calcFieldX(simulationMember);
        int y = calcFieldY(simulationMember);
        allEntityMap[x][y].add(simulationMember);
    }
/*
    public List<SimulationMember> getNearAntList(SimulationMember simulationMember,SimulationMember filterClass) {
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

        List<filterClass.getClas> outputList = new ArrayList<>();

        while (xCount < xEnd) {
            while (yCount < yEnd) {
                allEntityMap[x][y].forEach(Ant -> {if(Ant.getClass() == filterClass.getClass());});
                yCount++;
            }
            xCount++;
        }
        return outputList;
    }

    private int normalicedEnd(int xy) {

    }

 */

    private int calcFieldX(SimulationMember simulationMember) {
        return Math.floorDiv((int) simulationMember.getX(), arraySizeX);
    }

    private int calcFieldY(SimulationMember simulationMember) {
        return Math.floorDiv((int) simulationMember.getY(), arraySizeY);
    }
}

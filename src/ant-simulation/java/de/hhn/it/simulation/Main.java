package de.hhn.it.simulation;

import de.hhn.it.ui.AntApplication;
import javafx.application.Application;

/**
 * Starter Klasse der Simulation.
 */
public class Main {
    /**
     * Startmethode der Simulation. Kommandozeilenparameter werden ignoriert.
     *
     * @param args Parameter wird ignoriert.
     */
    public static void main(String[] args) {
        Application.launch(AntApplication.class, args);
    }
}

package de.hhn.it.simulation;

import java.util.List;

/**
 * Dieses Interface beschreibt Methode von Objekten, die Nachkommen erzeugen koennen.
 * Dieses Interface muss mit der Klasse der Nachkommen typisiert werden.
 *
 * @param <E> Typ (Klasse) der Nachkommen.
 */
public interface Reproduce<E> {

    /**
     * Menge erzeugter Nachkommen
     *
     * @return eine Menge erzeugter Nachkommen
     */
    List<E> createChildren();
}

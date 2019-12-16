package de.hhn.it.ui;

import de.hhn.it.simulation.entity.SimulationMember;
import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 *
 */
public abstract class SimulationGraphic {
    protected final Text textNode = new Text();

    protected abstract void updateUi(SimulationMember simulationMember);

    protected abstract Node getUiNode();

    protected final Text getTextNode() {
        return textNode;
    }
}

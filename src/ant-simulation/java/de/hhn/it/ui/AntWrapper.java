package de.hhn.it.ui;

import de.hhn.it.simulation.Ant;
import de.hhn.it.simulation.SimulationMember;

/**
 *
 */
final class AntWrapper extends SimulationMember implements Clickable {
    private final Ant ant;

    AntWrapper(Ant ant) {
        super(0, 0, 0, null);
        this.ant = ant;
    }

    @Override
    public double getRotation() {
        return ant.getRotation();
    }

    @Override
    public double getX() {
        return ant.getX();
    }

    @Override
    public double getY() {
        return ant.getY();
    }

    @Override
    public SimulationGraphic getSimulationGraphic() {
        return ant.getSimulationGraphic();
    }

    @Override
    public String getText() {
        return ant.getText();
    }

    public boolean isCarryingFood() {
        return ant.isCarryingFood();
    }

    @Override
    public void onClick() {
        if (ant instanceof Clickable)
            ((Clickable) ant).onClick();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ant == null) ? 0 : ant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AntWrapper other = (AntWrapper) obj;
        if (ant == null) {
            if (other.ant != null)
                return false;
        } else if (!ant.equals(other.ant))
            return false;
        return true;
    }
}
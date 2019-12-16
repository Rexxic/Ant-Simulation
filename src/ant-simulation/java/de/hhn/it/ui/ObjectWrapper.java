package de.hhn.it.ui;

import de.hhn.it.simulation.entity.SimulationMember;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
final class ObjectWrapper extends SimulationMember implements Clickable {
    private final Object object;

    ObjectWrapper(Object object) {
        super(0, 0, 0, null);
        this.object = object;
    }

    @Override
    public double getRotation() {
        try {
            Method getRotation = object.getClass().getMethod("getRotation");
            return (double) getRotation.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getX() {
        try {
            Method getX = object.getClass().getMethod("getX");
            return (double) getX.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double getY() {
        try {
            Method getY = object.getClass().getMethod("getY");
            return (double) getY.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SimulationGraphic getSimulationGraphic() {
        try {
            Method getSimulationGraphic = object.getClass().getMethod("getSimulationGraphic");
            return (SimulationGraphic) getSimulationGraphic.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getText() {
        try {
            Method getText = object.getClass().getMethod("getText");
            return (String) getText.invoke(object);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick() {
        if (object instanceof Clickable)
            ((Clickable) object).onClick();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((object == null) ? 0 : object.hashCode());
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
        ObjectWrapper other = (ObjectWrapper) obj;
        if (object == null) {
            if (other.object != null)
                return false;
        } else if (!object.equals(other.object))
            return false;
        return true;
    }
}
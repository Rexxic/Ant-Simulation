package de.hhn.it.ui;

import de.hhn.it.simulation.*;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Mit dem UiManager können Simulationsteilnehmer auf die Simulationsoberfläche
 * gesetzt werden oder von dieser entfernt werden. Teilnehmer können immer nur
 * einmal auf die Oberfläche gesetzt werden. Die gleiche Ameise (gleiche Instanz
 * der Klasse {@link Ant}) kann somit zum Beispiel nur einmal auf die Oberfläche
 * gesetzt werden.
 */
public class UiManager {
    private final Pane root;
    private final Group bottomGroup = new Group();
    private final Group middleGroup = new Group();
    private final Group topGroup = new Group();
    private final Map<SimulationMember, Group> simulationMembersOnGroup = new HashMap<>();
    private Simulation simulation;
    private Timeline mainAnimation;
    private LegAnimation legAnimation;

    /**
     * @param scene nicht {@code null}
     * @param root  nicht {@code null}
     */
    UiManager(Scene scene, Pane root) {
        this.root = Objects.requireNonNull(root);

        root.getChildren().add(bottomGroup);
        root.getChildren().add(middleGroup);
        root.getChildren().add(topGroup);
    }

    /**
     * Fügt eine Ameise der Simulationsoberfläche hinzu. Die Ameise wird
     * unterhalb der bisher hinzugefügten Simulationsteilnehmern angezeigt.
     * Ameisen die mit dieser Methode hinzugefügt wurden, werden immer unter
     * allen anderen angezeigt.
     *
     * @param ant not {@code null}
     * @throws IllegalStateException Wenn die Ameise bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>ant</code> {@code null} ist.
     * @see Layer#BOTTOM
     */
    public void addBottom(Ant ant) {
        Objects.requireNonNull(ant, "Ant is null");
        checkSimulationObjectMethods(ant);

        add((Object) ant, Layer.BOTTOM);
    }

    /**
     * Fügt eine Ameise der Simulationsoberfläche hinzu.
     *
     * @param ant not {@code null}
     * @throws IllegalStateException Wenn die Ameise bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>ant</code> {@code null} ist.
     * @see Layer#MIDDLE
     */
    public void add(Ant ant) {
        Objects.requireNonNull(ant, "Ant is null");
        checkSimulationObjectMethods(ant);

        add((Object) ant, Layer.MIDDLE);
    }

    /**
     * Fügt eine Ameise der Simulationsoberfläche hinzu. Die Ameise wird
     * oberhalb der bisher hinzugefügten Simulationsteilnehmer angezeigt.
     * Ameisen die mit dieser Methode hinzugefügt wurden, werden immer über
     * allen anderen angezeigt.
     *
     * @param ant not {@code null}
     * @throws IllegalStateException Wenn die Ameise bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>ant</code> {@code null} ist.
     * @see Layer#TOP
     */
    public void addTop(Ant ant) {
        Objects.requireNonNull(ant, "Ant is null");
        checkSimulationObjectMethods(ant);

        add((Object) ant, Layer.TOP);
    }

    /**
     * Fügt einen Ameisenhaufen der Simulationsoberfläche hinzu. Der
     * Ameisenhaufen wird unterhalb der bisher hinzugefügten
     * Simulationsteilnehmern angezeigt. Ameisenhaufen die mit dieser Methode
     * hinzugefügt wurden, werden immer unter allen anderen angezeigt.
     *
     * @param antHill nicht {@code null}
     * @throws IllegalStateException Wenn der Ameisenhaufen bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>antHill</code> {@code null}
     *                               ist.
     * @see Layer#BOTTOM
     */
    public void addBottom(AntHill antHill) {
        Objects.requireNonNull(antHill, "Ant is null");
        checkSimulationObjectMethods(antHill);

        add((Object) antHill, Layer.BOTTOM);
    }

    /**
     * Fügt einen Ameisenhaufen der Simulationsoberfläche hinzu.
     *
     * @param antHill nicht {@code null}
     * @throws IllegalStateException Wenn der Ameisenhaufen bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>antHill</code> {@code null}
     *                               ist.
     * @see UiManager#add(AntHill, boolean)
     */
    public void add(AntHill antHill) {
        Objects.requireNonNull(antHill, "AntHill is null");
        checkSimulationObjectMethods(antHill);

        add((Object) antHill, Layer.MIDDLE);
    }

    /**
     * Fügt einen Ameisenhaufen der Simulationsoberfläche hinzu. Der
     * Ameisenhaufen wird oberhalb der bisher hinzugefügten
     * Simulationsteilnehmer angezeigt. Ameisenhaufen die mit dieser Methode
     * hinzugefügt wurden, werden immer über allen anderen angezeigt.
     *
     * @param ant not {@code null}
     * @throws IllegalStateException Wenn die Ameise bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>ant</code> {@code null} ist.
     * @see Layer#TOP
     */
    public void addTop(AntHill antHill) {
        Objects.requireNonNull(antHill, "AntHill is null");
        checkSimulationObjectMethods(antHill);

        add((Object) antHill, Layer.TOP);
    }

    /**
     * Fügt einen Futterhaufen der Simulationsoberfläche hinzu. Der Futterhaufen
     * wird unterhalb der bisher hinzugefügten Simulationsteilnehmern angezeigt.
     * Futterhaufen die mit dieser Methode hinzugefügt wurden, werden immer
     * unter allen anderen angezeigt.
     *
     * @param food nicht {@code null}
     * @throws IllegalStateException Wenn der Futterhaufen bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>food</code> {@code null} ist.
     */
    public void addBottom(Food food) {
        Objects.requireNonNull(food, "Food is null");
        checkSimulationObjectMethods(food);

        add((Object) food, Layer.BOTTOM);
    }

    /**
     * Fügt einen Futterhaufen der Simulationsoberfläche hinzu.
     *
     * @param food nicht {@code null}
     * @throws IllegalStateException Wenn der Futterhaufen bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>food</code> {@code null} ist.
     * @see Layer#MIDDLE
     */
    public void add(Food food) {
        Objects.requireNonNull(food, "Food is null");
        checkSimulationObjectMethods(food);

        add((Object) food, Layer.MIDDLE);
    }

    /**
     * Fügt einen Futterhaufen der Simulationsoberfläche hinzu. Der Futterhaufen
     * wird oberhalb der bisher hinzugefügten Simulationsteilnehmer angezeigt.
     * Futterhaufen die mit dieser Methode hinzugefügt wurden, werden immer über
     * allen anderen angezeigt.
     *
     * @param food nicht {@code null}
     * @throws IllegalStateException Wenn der Futterhaufen bereits auf der Simulationsoberfläche
     *                               vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>food</code> {@code null} ist.
     * @see Layer#TOP
     */
    public void addTop(Food food) {
        Objects.requireNonNull(food, "Food is null");
        checkSimulationObjectMethods(food);

        add((Object) food, Layer.TOP);
    }

    private void checkSimulationObjectMethods(Object o) {
        Method[] methods = o.getClass().getMethods();

        boolean getRotationOk = false, getXOk = false, getYOk = false, getSimulationGraphicOk = false, getTextOk = false;
        for (Method m : methods) {
            if ("getRotation".equals(m.getName()) && double.class.equals(m.getReturnType()) && m.getParameterTypes().length == 0
                    && Modifier.isPublic(m.getModifiers()))
                getRotationOk = true;
            else if ("getX".equals(m.getName()) && double.class.equals(m.getReturnType()) && m.getParameterTypes().length == 0
                    && Modifier.isPublic(m.getModifiers()))
                getXOk = true;
            else if ("getY".equals(m.getName()) && double.class.equals(m.getReturnType()) && m.getParameterTypes().length == 0
                    && Modifier.isPublic(m.getModifiers()))
                getYOk = true;
            else if ("getSimulationGraphic".equals(m.getName())
                    && (SimulationGraphic.class.equals(m.getReturnType()) || SimulationGraphic.class.equals(m.getReturnType()
                    .getSuperclass())) && m.getParameterTypes().length == 0 && Modifier.isPublic(m.getModifiers()))
                getSimulationGraphicOk = true;
            else if ("getText".equals(m.getName()) && String.class.equals(m.getReturnType()) && m.getParameterTypes().length == 0
                    && Modifier.isPublic(m.getModifiers()))
                getTextOk = true;
        }

        List<String> missingMethods = new ArrayList<>();
        if (!getRotationOk)
            missingMethods.add("public double getRotation()");
        if (!getXOk)
            missingMethods.add("public double getX()");
        if (!getYOk)
            missingMethods.add("public double getY()");
        if (!getSimulationGraphicOk)
            missingMethods.add("public SimulationGraphic getSimulationGraphic()");
        if (!getTextOk)
            missingMethods.add("public String getText()");

        StringBuilder missingMethodsText = new StringBuilder();
        boolean first = true;
        for (String mm : missingMethods) {
            if (first)
                first = false;
            else
                missingMethodsText.append(", ");
            missingMethodsText.append(mm);
        }

        if (!missingMethods.isEmpty())
            throw new IllegalArgumentException(o.getClass().getSimpleName() + " is missing "
                    + (missingMethods.size() == 1 ? "a method" : "some methods") + ": " + missingMethodsText.toString());
    }

    /**
     * Fügt einen Simulationsteilnehmer der Simulationsoberfläche hinzu. Der
     * Simulationsteilnehmer wird unterhalb der bisher hinzugefügten
     * Simulationsteilnehmern angezeigt. Simulationsteilnehmer die mit dieser
     * Methode hinzugefügt wurden, werden immer unter allen anderen angezeigt.
     *
     * @param simulationMember nicht {@code null}
     * @throws IllegalStateException Wenn das Simulationsmitglied bereits auf der
     *                               Simulationsoberfläche vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>simulationMember</code>
     *                               {@code null} ist.
     * @see Layer#BOTTOM
     */
    public void addBottom(SimulationMember simulationMember) {
        Objects.requireNonNull(simulationMember, "SimulationMember is null");

        add((Object) simulationMember, Layer.BOTTOM);
    }

    /**
     * Fügt einen Simulationsteilnehmer der Simulationsoberfläche hinzu.
     *
     * @param simulationMember nicht {@code null}
     * @throws IllegalStateException Wenn das Simulationsmitglied bereits auf der
     *                               Simulationsoberfläche vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>simulationMember</code>
     *                               {@code null} ist.
     * @see Layer#MIDDLE
     */
    public void add(SimulationMember simulationMember) {
        Objects.requireNonNull(simulationMember, "SimulationMember is null");

        add((Object) simulationMember, Layer.MIDDLE);
    }

    /**
     * Fügt einen Simulationsteilnehmer der Simulationsoberfläche hinzu. Der
     * Simulationsteilnehmer wird oberhalb der bisher hinzugefügten
     * Simulationsteilnehmer angezeigt. Simulationsteilnehmer die mit dieser
     * Methode hinzugefügt wurden, werden immer über allen anderen angezeigt.
     *
     * @param simulationMember nicht {@code null}
     * @throws IllegalStateException Wenn das Simulationsmitglied bereits auf der
     *                               Simulationsoberfläche vorhanden ist.
     * @throws NullPointerException  Wenn der Parameter <code>simulationMember</code>
     *                               {@code null} ist.
     * @see Layer#Top
     */
    public void addTop(SimulationMember simulationMember) {
        Objects.requireNonNull(simulationMember, "SimulationMember is null");

        add((Object) simulationMember, Layer.TOP);
    }

    /**
     * Entfernt eine Ameise von der Simulationsoberfläche. Die Ameise muss zuvor
     * auf die Oberfläche gesetzt worden sein.
     *
     * @param ant nicht {@code null}
     * @throws IllegalStateException Wenn die Ameise nicht auf der Oberfläche gefunden wurde.
     * @throws NullPointerException  Wenn der Parameter <code>ant</code> {@code null} ist.
     */
    public void remove(Ant ant) {
        Objects.requireNonNull(ant, "Ant is null");

        remove((Object) ant);
    }

    /**
     * Entfernt einen Ameisenhaufen von der Simulationsoberfläche. Der
     * Ameisenhaufen muss zuvor auf die Oberfläche gesetzt worden sein.
     *
     * @param antHill nicht {@code null}
     * @throws IllegalStateException Wenn der Ameisenhaufen nicht auf der Oberfläche gefunden
     *                               wurde.
     * @throws NullPointerException  Wenn der Parameter <code>antHill</code> {@code null}
     *                               ist.
     */
    public void remove(AntHill antHill) {
        Objects.requireNonNull(antHill, "AntHill is null");

        remove((Object) antHill);
    }

    /**
     * Entfernt einen Futterhaufen von der Simulationsoberfläche. Der
     * Futterhaufen muss zuvor auf die Oberfläche gesetzt worden sein.
     *
     * @param food nicht {@code null}
     * @throws IllegalStateException Wenn der Ameisenhaufen nicht auf der Oberfläche gefunden
     *                               wurde.
     * @throws NullPointerException  Wenn der Parameter <code>food</code> {@code null} ist.
     */
    public void remove(Food food) {
        Objects.requireNonNull(food, "Food is null");

        remove((Object) food);
    }

    /**
     * Entfernt ein Simulationsmitglied von der Simulationsoberfläche. Das
     * Simulationsmitglied muss zuvor auf die Oberfläche gesetzt worden sein.
     *
     * @param simulationMember nicht {@code null}
     * @throws IllegalStateException Wenn das Simulationsmitglied nicht auf der Oberfläche
     *                               gefunden wurde.
     * @throws NullPointerException  Wenn der Parameter <code>simulationMember</code>
     *                               {@code null} ist.
     */
    public void remove(SimulationMember simulationMember) {
        Objects.requireNonNull(simulationMember, "SimulationMember is null");

        remove((Object) simulationMember);
    }

    private void add(Object object, Layer layer) {
        final SimulationMember simulationMember;

        if (object instanceof SimulationMember)
            simulationMember = (SimulationMember) object;
        else if (object instanceof Ant)
            simulationMember = new AntWrapper((Ant) object);
        else
            simulationMember = new ObjectWrapper(object);

        if (simulationMembersOnGroup.containsKey(simulationMember))
            throw new IllegalStateException(object.getClass().getSimpleName() + " already shown");

        SimulationGraphic simulationGraphic = simulationMember.getSimulationGraphic();

        // select layer
        final Group group;
        switch (layer) {
            case BOTTOM:
                group = bottomGroup;
                break;
            case MIDDLE:
                group = middleGroup;
                break;
            case TOP:
                group = topGroup;
                break;
            default:
                throw new RuntimeException("Unknown enum value: " + layer);
        }
        // add nodes
        group.getChildren().add(simulationGraphic.getUiNode());
        group.getChildren().add(simulationGraphic.getTextNode());

        simulationMembersOnGroup.put(simulationMember, group);

        if (simulationMember instanceof Clickable) {
            simulationGraphic.getUiNode().setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (simulationMember instanceof Clickable)
                        ((Clickable) simulationMember).onClick();
                }
            });
        }

        simulationGraphic.updateUi(simulationMember);
    }

    private void remove(Object object) {
        final SimulationMember simulationMember;

        if (object instanceof SimulationMember)
            simulationMember = (SimulationMember) object;
        else if (object instanceof Ant)
            simulationMember = new AntWrapper((Ant) object);
        else
            simulationMember = new ObjectWrapper(object);

        if (!simulationMembersOnGroup.containsKey(simulationMember))
            throw new IllegalStateException(object.getClass().getSimpleName() + " not found");

        // remove simulation members
        Group group = simulationMembersOnGroup.remove(simulationMember);

        // remove simulation graphics
        SimulationGraphic simulationGraphic = simulationMember.getSimulationGraphic();
        group.getChildren().remove(simulationGraphic.getUiNode());
        group.getChildren().remove(simulationGraphic.getTextNode());
    }

    /**
     * Liefert die Breite der Simulationsoberfläche.<br>
     * <br>
     * <i>Dieser Wert sollte nicht abgespeichert werden, da sich die Breite der
     * Oberfläche während der Simulation ändern könnte.</i>
     *
     * @return Breite der Simulationsoberfläche
     */
    public double getSimulationSurfaceWidth() {
        return root.getWidth();
    }

    /**
     * Liefert die Höhe der Simulationsoberfläche.<br>
     * <br>
     * <i>Dieser Wert sollte nicht abgespeichert werden da, sich die Höhe der
     * Oberfläche während der Simulation ändern könnte.</i>
     *
     * @return Höhe der Simulationsoberfläche
     */
    public double getSimulationSurfaceHeight() {
        return root.getHeight();
    }

    void doSimulationStep() {
        if (simulation != null)
            simulation.doSimulationStep();

        updateUi();

    }

    private void updateUi() {
        for (SimulationMember o : simulationMembersOnGroup.keySet())
            o.getSimulationGraphic().updateUi(o);
    }

    /**
     * Hält den Taktgeber der Simulation an, wenn der Taktgeber nicht läuft hat
     * ein Aufruf keinen Effekt.
     */
    public void pauseSimulation() {
        if (mainAnimation != null && legAnimation != null) {
            mainAnimation.pause();
            legAnimation.pause();
        } else
            throw new IllegalStateException("main animation or leg animation not set");
    }

    /**
     * Startet den Taktgeber der Simulation, wenn der Taktgeber läuft hat ein
     * Aufruf keinen Effekt.
     */
    public void playSimulation() {
        if (mainAnimation != null && legAnimation != null) {
            mainAnimation.play();
            legAnimation.play();
        } else
            throw new IllegalStateException("main animation or leg animation not set");
    }

    void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    void setMainAnimator(Timeline mainAnimation) {
        this.mainAnimation = mainAnimation;
    }

    void setLegAnimation(LegAnimation legAnimation) {
        this.legAnimation = legAnimation;
    }

    private static enum Layer {
        BOTTOM, MIDDLE, TOP
    }
}

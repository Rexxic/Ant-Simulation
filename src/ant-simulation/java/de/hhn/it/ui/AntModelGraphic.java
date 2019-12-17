package de.hhn.it.ui;

import de.hhn.it.simulation.entity.Ant;
import de.hhn.it.simulation.entity.SimulationMember;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;

import java.util.HashMap;
import java.util.Map;

/**
 * Mit der Klasse {@link AntModelGraphic} werden Ameisen in der Simulation
 * grafisch abgebildet.
 *
 * @see Ant
 */
public class AntModelGraphic extends SimulationGraphic {
    static final double ANT_CENTER_X = 12.5;
    static final double ANT_CENTER_Y = 9;

    static final double LEG_FRONT_LEFT_ROTATION_X = 14;
    static final double LEG_FRONT_LEFT_ROTATION_Y = 8;
    static final double LEG_FRONT_RIGHT_ROTATION_X = 14;
    static final double LEG_FRONT_RIGHT_ROTATION_Y = 10;
    static final double LEG_MIDDLE_LEFT_ROTATION_X = 13;
    static final double LEG_MIDDLE_LEFT_ROTATION_Y = 8;
    static final double LEG_MIDDLE_RIGHT_ROTATION_X = 13;
    static final double LEG_MIDDLE_RIGHT_ROTATION_Y = 10;
    static final double LEG_REAR_LEFT_ROTATION_X = 12;
    static final double LEG_REAR_LEFT_ROTATION_Y = 8;
    static final double LEG_REAR_RIGHT_ROTATION_X = 12;
    static final double LEG_REAR_RIGHT_ROTATION_Y = 10;

    static final LegAnimation LEG_ANIMATION = new LegAnimation();

    private static final String DEFAULT = "default";
    private static final Map<String, AntModelGraphicBuilder> BUILDERS = new HashMap<>();

    private static final boolean SHOW_ROTATION_POINTS = false;

    static {
        BUILDERS.put(
                DEFAULT,
                new AntModelGraphicBuilder(Color.BLACK, Color.rgb(40, 0, 0), Color.rgb(40, 0, 0), Color.rgb(63, 24, 0), Color
                        .rgb(45, 18, 0), Color.rgb(0, 255, 0)));
    }

    private final Rotate centerRotate = new Rotate(0, ANT_CENTER_X, ANT_CENTER_Y);
    private final Group antNode = new Group();
    private final Node food;

    /**
     * {@link AntModelGraphic} with custom colors
     *
     * @param legColor      not {@code null}
     * @param antennaeColor not {@code null}
     * @param headColor     not {@code null}
     * @param middleColor   not {@code null}
     * @param rearColor     not {@code null}
     * @param foodColor     not {@code null}
     */
    public AntModelGraphic(Color legColor, Color antennaeColor, Color headColor, Color middleColor, Color rearColor, Color foodColor, double scale) {
        this(getBuilder(legColor, antennaeColor, headColor, middleColor, rearColor, foodColor), scale);

    }

    /**
     * {@link AntModelGraphic} with default colors
     */
    public AntModelGraphic() {
        this(BUILDERS.get(DEFAULT), 1);
    }

    private AntModelGraphic(AntModelGraphicBuilder modelBuilder, double scale) {
        Node frontLeftLeg = modelBuilder.getFrontLeftLeg();
        Node frontRightLeg = modelBuilder.getFrontRightLeg();
        Node middleLeftLeg = modelBuilder.getMiddleLeftLeg();
        Node middleRightLeg = modelBuilder.getMiddleRightLeg();
        Node rearLeftLeg = modelBuilder.getRearLeftLeg();
        Node rearRightLeg = modelBuilder.getRearRightLeg();

        if (LEG_ANIMATION != null)
            LEG_ANIMATION.addAnimationToLegs(frontLeftLeg, frontRightLeg, middleLeftLeg, middleRightLeg, rearLeftLeg, rearRightLeg);

        Node bodyAndAntannae = modelBuilder.getBodyAndAntenna();

        food = modelBuilder.getFood();
        antNode.getChildren().addAll(frontLeftLeg, frontRightLeg, middleLeftLeg, middleRightLeg, rearLeftLeg, rearRightLeg,
                bodyAndAntannae, food);

        antNode.getTransforms().add(centerRotate);

        if (SHOW_ROTATION_POINTS) {
            antNode.getChildren().add(new Circle(LEG_FRONT_LEFT_ROTATION_X, LEG_FRONT_LEFT_ROTATION_Y, 1, Color.rgb(0, 255, 0)));
            antNode.getChildren().add(new Circle(LEG_FRONT_RIGHT_ROTATION_X, LEG_FRONT_RIGHT_ROTATION_Y, 1, Color.rgb(255, 0, 0)));
            antNode.getChildren().add(new Circle(LEG_MIDDLE_LEFT_ROTATION_X, LEG_MIDDLE_LEFT_ROTATION_Y, 1, Color.rgb(0, 0, 255)));
            antNode.getChildren().add(new Circle(LEG_MIDDLE_RIGHT_ROTATION_X, LEG_MIDDLE_RIGHT_ROTATION_Y, 1, Color.rgb(255, 255, 0)));
            antNode.getChildren().add(new Circle(LEG_REAR_LEFT_ROTATION_X, LEG_MIDDLE_LEFT_ROTATION_Y, 1, Color.rgb(255, 0, 255)));
            antNode.getChildren().add(new Circle(LEG_REAR_RIGHT_ROTATION_X, LEG_MIDDLE_RIGHT_ROTATION_Y, 1, Color.BLACK));
            antNode.getChildren().add(new Circle(ANT_CENTER_X, ANT_CENTER_Y, 1, Color.WHITE));
        }

        antNode.setTranslateX(-ANT_CENTER_X);
        antNode.setTranslateY(-ANT_CENTER_Y);
        antNode.getStyleClass().add("ant");
        antNode.setScaleX(scale);
        antNode.setScaleY(scale);
        textNode.getStyleClass().add("ant-text");

        antNode.setCacheHint(CacheHint.ROTATE);
        antNode.setCache(AntApplication.TURBO_MODE);
    }

    private static AntModelGraphicBuilder getBuilder(Color legColor, Color antennaeColor, Color headColor, Color middleColor,
                                                     Color rearColor, Color foodColor) {
        String key = AntModelGraphicBuilder.key(legColor, antennaeColor, headColor, middleColor, rearColor, foodColor);
        if (BUILDERS.containsKey(key))
            return BUILDERS.get(key);
        else {
            AntModelGraphicBuilder builder = new AntModelGraphicBuilder(legColor, antennaeColor, headColor, middleColor, rearColor,
                    foodColor);
            BUILDERS.put(key, builder);
            return builder;
        }
    }

    @Override
    protected void updateUi(SimulationMember simulationObject) {
        centerRotate.angleProperty().set(-simulationObject.getRotation());
        antNode.layoutXProperty().set(simulationObject.getX());
        antNode.layoutYProperty().set(simulationObject.getY());

        if (textNode.isVisible()) {
            textNode.layoutXProperty().set(simulationObject.getX() + 8);
            textNode.layoutYProperty().set(simulationObject.getY() + 16);
            textNode.textProperty().set(simulationObject.getText());
        }

        if (((Object) simulationObject) instanceof Ant) {
            food.setVisible(((Ant) ((Object) simulationObject)).isCarryingFood());
        } else if (simulationObject instanceof AntWrapper) {
            food.setVisible(((AntWrapper) simulationObject).isCarryingFood());
        }
    }

    @Override
    protected Node getUiNode() {
        return antNode;
    }
}

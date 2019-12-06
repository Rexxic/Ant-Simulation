package de.hhn.it.ui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 */
final class LegAnimation {
    private final int LEG_MOVEMENT_PIRIOT = 220; // ms

    private final Rotate rotateFR = new Rotate(0, AntModelGraphic.LEG_FRONT_RIGHT_ROTATION_X, AntModelGraphic.LEG_FRONT_RIGHT_ROTATION_Y);
    private final KeyValue startFR = new KeyValue(rotateFR.angleProperty(), 15, Interpolator.EASE_BOTH);
    private final KeyValue endFR = new KeyValue(rotateFR.angleProperty(), -15, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartFR = new KeyFrame(Duration.ZERO, startFR);
    private final KeyFrame frameEndFR = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endFR);

    private final Rotate rotateFL = new Rotate(0, AntModelGraphic.LEG_FRONT_LEFT_ROTATION_X, AntModelGraphic.LEG_FRONT_LEFT_ROTATION_Y);
    private final KeyValue startFL = new KeyValue(rotateFL.angleProperty(), 15, Interpolator.EASE_BOTH);
    private final KeyValue endFL = new KeyValue(rotateFL.angleProperty(), -15, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartFL = new KeyFrame(Duration.ZERO, startFL);
    private final KeyFrame frameEndFL = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endFL);

    private final Rotate rotateMR = new Rotate(0, AntModelGraphic.LEG_MIDDLE_RIGHT_ROTATION_X, AntModelGraphic.LEG_MIDDLE_RIGHT_ROTATION_Y);
    private final KeyValue startMR = new KeyValue(rotateMR.angleProperty(), -20, Interpolator.EASE_BOTH);
    private final KeyValue endMR = new KeyValue(rotateMR.angleProperty(), 30, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartMR = new KeyFrame(Duration.ZERO, startMR);
    private final KeyFrame frameEndMR = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endMR);

    private final Rotate rotateML = new Rotate(0, AntModelGraphic.LEG_MIDDLE_LEFT_ROTATION_X, AntModelGraphic.LEG_MIDDLE_LEFT_ROTATION_Y);
    private final KeyValue startML = new KeyValue(rotateML.angleProperty(), -20, Interpolator.EASE_BOTH);
    private final KeyValue endML = new KeyValue(rotateML.angleProperty(), 30, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartML = new KeyFrame(Duration.ZERO, startML);
    private final KeyFrame frameEndML = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endML);

    private final Rotate rotateRR = new Rotate(0, AntModelGraphic.LEG_REAR_RIGHT_ROTATION_X, AntModelGraphic.LEG_REAR_RIGHT_ROTATION_Y);
    private final KeyValue startRR = new KeyValue(rotateRR.angleProperty(), 10, Interpolator.EASE_BOTH);
    private final KeyValue endRR = new KeyValue(rotateRR.angleProperty(), -10, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartRR = new KeyFrame(Duration.ZERO, startRR);
    private final KeyFrame frameEndRR = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endRR);

    private final Rotate rotateRL = new Rotate(0, AntModelGraphic.LEG_REAR_LEFT_ROTATION_X, AntModelGraphic.LEG_REAR_LEFT_ROTATION_Y);
    private final KeyValue startRL = new KeyValue(rotateRL.angleProperty(), 10, Interpolator.EASE_BOTH);
    private final KeyValue endRL = new KeyValue(rotateRL.angleProperty(), -10, Interpolator.EASE_BOTH);
    private final KeyFrame frameStartRL = new KeyFrame(Duration.ZERO, startRL);
    private final KeyFrame frameEndRL = new KeyFrame(Duration.millis(LEG_MOVEMENT_PIRIOT), endRL);

    private final Animation legAnimation = new Timeline(frameStartFR, frameStartFL, frameStartMR, frameStartML, frameStartRR, frameStartRL,
            frameEndFR, frameEndFL, frameEndMR, frameEndML, frameEndRR, frameEndRL);

    LegAnimation() {
        legAnimation.autoReverseProperty().set(true);
        legAnimation.setCycleCount(Animation.INDEFINITE);
        legAnimation.setDelay(Duration.millis(500));
    }

    void addAnimationToLegs(Node fr, Node fl, Node mr, Node ml, Node rr, Node rl) {
        fr.getTransforms().add(rotateFR);
        fl.getTransforms().add(rotateFL);
        mr.getTransforms().add(rotateMR);
        ml.getTransforms().add(rotateML);
        rr.getTransforms().add(rotateRR);
        rl.getTransforms().add(rotateRL);
    }

    void pause() {
        legAnimation.pause();
    }

    void play() {
        if (!AntApplication.TURBO_MODE)
            legAnimation.play();
    }

    void stop() {
        legAnimation.stop();
    }
}

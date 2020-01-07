package de.hhn.it.ui;

import de.hhn.it.mapBuilder.NoiseImageBuilder;
import de.hhn.it.simulation.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

/**
 *
 */
public class AntApplication extends Application {
    /**
     * Length of each simulation frame in milliseconds.
     */
    public static final int SIMULATION_FRAME_LENGTH = 1000 / 60;
    /*
     * Increase SCALE if you want a bigger
     * surface
     */
    private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    private static final double SCALE = 4;

    private static final double SIMULATION_SURFACE_WIDTH = SCREEN_SIZE.getWidth() * SCALE;
    private static final double SIMULATION_SURFACE_HEIGHT = SCREEN_SIZE.getHeight() * SCALE;
    /*
     * Increase SCROLLPANE WIDTH and HEIGHT if you want a bigger window
     */
    private static final double SCROLLPANE_WIDTH = SCREEN_SIZE.getWidth() * 3 / 4;
    private static final double SCROLLPANE_HEIGHT = SCREEN_SIZE.getHeight() * 3 / 4;
    /*
     * If set to true, deactivates the leg animation
     */
    public static boolean TURBO_MODE = false;

    @Override
    public void start(final Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(SIMULATION_SURFACE_WIDTH, SIMULATION_SURFACE_HEIGHT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setPannable(false);
        scrollPane.setPrefSize(SCREEN_SIZE.getWidth(), SCREEN_SIZE.getHeight());
        scrollPane.getStyleClass().add("scroll-pane");

        //Pane mit festem Platz in einer Ecke des Bildschirms
        Pane controllPane = new Pane();
        double controllPaneWidth = 135;
        double controllPaneHeight = 35;
        controllPane.setPrefSize(controllPaneWidth, controllPaneHeight);
        controllPane.setBackground(new Background(new BackgroundFill(Color.DIMGRAY.deriveColor(1, 1, 1, 0.7), new CornerRadii(10), null)));

        Scene scene = new Scene(new Group(scrollPane, controllPane));

        scene.getStylesheets().add(AntApplication.class.getClassLoader().getResource("main.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Ameisensimulation \u00A9 Fakultaet fuer Informatik - Hochschule Heilbronn 2018");

        // background ...
        NoiseImageBuilder noiseImageBuilder = new NoiseImageBuilder((int) SIMULATION_SURFACE_WIDTH, (int) SIMULATION_SURFACE_HEIGHT);
        noiseImageBuilder.drawColor();
        ImageView background = new ImageView(noiseImageBuilder);
        background.setCache(true);
        root.getChildren().add(background);

        // 100x100 grid ...
        root.getChildren().add(new Grid(SIMULATION_SURFACE_WIDTH, SIMULATION_SURFACE_HEIGHT, 100));

        stage.setHeight(SCROLLPANE_HEIGHT);
        stage.setWidth(SCROLLPANE_WIDTH);
        stage.show();
        stage.setResizable(true);
        stage.setFullScreen(false);


        final UiManager uiManager = new UiManager(stage, root, controllPane, scrollPane);

        final Timeline mainAnimator = new Timeline(new KeyFrame(Duration.millis(SIMULATION_FRAME_LENGTH), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                uiManager.doSimulationStep();
            }
        }));
        mainAnimator.setCycleCount(Timeline.INDEFINITE);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                uiManager.setMainAnimator(mainAnimator);
                uiManager.setLegAnimation(AntModelGraphic.LEG_ANIMATION);
                uiManager.setSimulation(new Simulation(uiManager));
            }
        });

        mainAnimator.play();
        AntModelGraphic.LEG_ANIMATION.play();
    }

    public static double getSCALE() {
        return SCALE;
    }
}

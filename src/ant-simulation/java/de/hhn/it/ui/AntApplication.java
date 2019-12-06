package de.hhn.it.ui;

import de.hhn.it.simulation.Simulation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 */
public class AntApplication extends Application {
    /**
     * Length of each simulation frame in milliseconds.
     */
    public static final int SIMULATION_FRAME_LENGTH = 1000 / 50;
    /*
     * Increase SIMMULATION_SURFACE WIDHT and HEIGHT if you want a bigger
     * surface
     */
    private static final double SIMMULATION_SURFACE_WIDTH = 1920;
    private static final double SIMMULATION_SURFACE_HEIGHT = 1080;
    /*
     * Increase SCROLLPANE WIDTH and HEIGHT if you want a bigger window
     */
    private static final double SCROLLPANE_WIDTH = 918;
    private static final double SCROLLPANE_HEIGHT = 600;
    /*
     * If set to true, deactivates the leg animation
     */
    public static boolean TURBO_MODE = false;

    @Override
    public void start(final Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(SIMMULATION_SURFACE_WIDTH, SIMMULATION_SURFACE_HEIGHT);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setPannable(true);
        scrollPane.setPrefSize(SCROLLPANE_WIDTH - 10, SCROLLPANE_HEIGHT - 10);
        scrollPane.getStyleClass().add("scroll-pane");

        Scene scene = new Scene(scrollPane);

        scene.getStylesheets().add(AntApplication.class.getClassLoader().getResource("main.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Ameisensimulation \u00A9 Fakultaet fuer Informatik - Hochschule Heilbronn 2018");

        // background ...
        ImageView background = new ImageView(AntApplication.class.getClassLoader().getResource("sand.png").toExternalForm());
        background.setCache(true);
        root.getChildren().add(background);

        // 100x100 grid ...
        root.getChildren().add(new Grid(SIMMULATION_SURFACE_WIDTH, SIMMULATION_SURFACE_HEIGHT, 100));

        stage.show();
        stage.setResizable(true);
        stage.setFullScreen(true);

        final UiManager uiManager = new UiManager(scene, root);

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
}

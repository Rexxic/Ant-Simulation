package de.hhn.it.ui;

import de.hhn.it.simulation.SimulationMember;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Grafikklasse zum Abbilden von Grafikdateien auf der Simulationsoberfläche.
 */
public class ImageFileGraphic extends SimulationGraphic {
    private static final Map<String, Image> imagesCache = new HashMap<>();

    private final ImageView imageNode = new ImageView();
    private final Rotate centerRotate;

    /**
     * @param filename Datei im resources Verzeichnis
     */
    public ImageFileGraphic(String filename) {
        Image image;
        if (imagesCache.containsKey(filename))
            image = imagesCache.get(filename);
        else {
            URL resource = ImageFileGraphic.class.getClassLoader().getResource(filename);
            if (resource == null)
                throw new IllegalArgumentException(String.format("File %s not found", filename));

            image = new Image(resource.toExternalForm());
            imagesCache.put(filename, image);
        }

        imageNode.setImage(image);
        centerRotate = new Rotate(0, image.getWidth() / 2, image.getHeight() / 2);
        imageNode.getTransforms().add(centerRotate);
        imageNode.setTranslateX(-(image.getWidth() / 2));
        imageNode.setTranslateY(-(image.getHeight() / 2));

        textNode.getStyleClass().add("image-file-graphic-text");
    }

    @Override
    protected void updateUi(SimulationMember simulationObject) {
        centerRotate.angleProperty().set(-simulationObject.getRotation());
        imageNode.layoutXProperty().set(simulationObject.getX());
        imageNode.layoutYProperty().set(simulationObject.getY());

        if (textNode.isVisible()) {
            textNode.layoutXProperty().set(imageNode.layoutXProperty().get() + imageNode.getLayoutBounds().getWidth() / 2);
            textNode.layoutYProperty().set(imageNode.layoutYProperty().get() + imageNode.getLayoutBounds().getHeight() / 2 + 8);
            textNode.textProperty().set(simulationObject.getText());
        }
    }

    @Override
    protected Node getUiNode() {
        return imageNode;
    }
}

package de.hhn.it.mapBuilder;

import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * @author Cedric Seiz
 * Übertragung der funktion der Klasse Improved Noise in ein Writeable Image und gleichzeitig auch verarbeitung um die Textur
 * möglichst nah an die natürliche Textur von Sand anzupassen.
 */

public class NoiseImageBuilder extends WritableImage {
    private PixelWriter pixelWriter;

    public NoiseImageBuilder(int width, int height) {
        super(width, height);

        this.pixelWriter = super.getPixelWriter();
    }

    private static final double RESOLUTION_SCALE_DOWN = 900;
    private static final double X_SCALE = 4 / RESOLUTION_SCALE_DOWN;
    private static final double Y_SCALE = 1f / RESOLUTION_SCALE_DOWN;
    private static final double LAYER_2_RESOLUTION = 6;
    private static final double LAYER_3_RESOLUTION = 256;
    private static final int X_OFFSET = 0;
    private static final int Y_OFFSET = -512;

    public void drawColor() {
        for (int pixelX = X_OFFSET; pixelX < super.getWidth() + X_OFFSET; pixelX++) {
            for (int pixelY = Y_OFFSET; pixelY < super.getHeight() + Y_OFFSET; pixelY++) {

                double x = pixelX * X_SCALE;
                double y = pixelY * Y_SCALE;

                Color sandColor = Color.PALEGOLDENROD;

                double layer1 = (1 + ImprovedNoise.noise(x, y, 0)) * 2;
                double layer2 = (1 + ImprovedNoise.noise(x * LAYER_2_RESOLUTION, y * LAYER_2_RESOLUTION, 0));
                double layer3 = (1 + ImprovedNoise.noise((x / (X_SCALE * RESOLUTION_SCALE_DOWN)) * LAYER_3_RESOLUTION, y * LAYER_3_RESOLUTION, 0)) / 2;

                double colorValue = (layer1 + layer2 - (1 - (layer1 + layer2 / 6)) * layer3) / 7 * 0.9;

                double border = 0.5;
                Color color;
                if (colorValue > border) {
                    colorValue = 1 - (1 - colorValue * 1.1) / (1 - border);

                    color = sandColor.interpolate(Color.FLORALWHITE, colorValue);
                } else {
                    colorValue = 1 - (colorValue * 1.1 / border);

                    color = sandColor.interpolate(Color.BLACK, colorValue);
                }
                pixelWriter.setColor(pixelX - X_OFFSET, pixelY - Y_OFFSET, color);

            }
        }
    }
}

package de.hhn.it.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 */
final class Grid extends Canvas {
    Grid(double width, double height, int raster) {
        super(width, height);

        GraphicsContext gridContext = getGraphicsContext2D();
        for (double x = raster + .5; x < width; x += raster) {
            gridContext.strokeLine(x, 0, x, height);

            if (x == raster + .5)
                for (double y = raster + .5; y < height; y += raster)
                    gridContext.strokeLine(0, y, width, y);
        }
        setCache(true);
        getStyleClass().add("grid");
    }
}

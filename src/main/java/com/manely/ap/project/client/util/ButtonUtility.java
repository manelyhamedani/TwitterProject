package com.manely.ap.project.client.util;

import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class ButtonUtility {

    public static Image setColorButtonImage(Button button) {
        ImageView imageView = (ImageView) button.getGraphic();
        Image image = imageView.getImage();
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        WritableImage finalImage = new WritableImage(width, height);

        PixelReader reader = image.getPixelReader();
        PixelWriter writer = finalImage.getPixelWriter();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (reader.getColor(x, y).equals(Color.BLACK)) {
                    writer.setArgb(x, y, 0xff36b9ff);
                }
                else {
                    writer.setArgb(x, y, reader.getArgb(x, y));
                }
            }
        }

        return finalImage;
    }
}

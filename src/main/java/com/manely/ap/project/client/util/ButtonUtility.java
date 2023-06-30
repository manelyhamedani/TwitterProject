package com.manely.ap.project.client.util;

import javafx.application.Platform;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ButtonUtility {

    public static Image setColorButtonImage(Button button, int colorARGB) {
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
                    writer.setArgb(x, y, colorARGB);
                }
                else {
                    writer.setArgb(x, y, reader.getArgb(x, y));
                }
            }
        }

        return finalImage;
    }

    public static void setRoundedImageView(ImageView imageView, Image image) {
        double radius = imageView.getFitHeight() / 2;
        Circle clip = new Circle(radius);
        clip.setCenterX(radius);
        clip.setCenterY(radius);

        imageView.setImage(image);
        imageView.setClip(clip);
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Platform.runLater(() -> {
            WritableImage roundedImage = imageView.snapshot(parameters, null);
            imageView.setClip(null);
            imageView.setImage(roundedImage);
        });
    }
}

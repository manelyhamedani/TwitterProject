package com.manely.ap.project.common.model;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class Image {
    private final String format;
    private byte[] imageBytes;
    private final KIND kind;

    public enum KIND {
        AVATAR, HEADER, TWEET
    }

    public static final int VALID_AVATAR_HEIGHT = 400;
    public static final int VALID_AVATAR_WIDTH = 400;
    public static final int MAX_AVATAR_SIZE = 1;

    public static final int VALID_HEADER_HEIGHT = 500;
    public static final int VALID_HEADER_WIDTH = 1500;
    public static final int MAX_HEADER_SIZE = 2;

    public static final int VALID_TWEET_HEIGHT = 900;
    public static final int VALID_TWEET_WIDTH = 1600;

    public Image(String format, byte[] imageBytes, KIND kind) throws IOException {
        if (!format.equals("jpg") && !format.equals("png"))
            throw new IllegalArgumentException();
        this.format = format;
        if (imageBytes.length == 0)
            throw new IllegalArgumentException();
        this.imageBytes = imageBytes;
        this.kind = kind;
        fitDimension();
        if (kind != KIND.TWEET) {
            fitSize();
        }
    }

    public String getFormat() {
        return format;
    }

    public KIND getKind() {
        return kind;
    }

    public byte[] getBytes() {
        return imageBytes;
    }

    private void fitSize() throws IOException {
        double sizeInMB;
        boolean fit = false;
        while (!fit) {
            sizeInMB = ((double) imageBytes.length) / (1024L * 1024L);
            switch (kind) {
                case AVATAR -> {
                    if (sizeInMB <= MAX_AVATAR_SIZE)
                        fit = true;
                }
                case HEADER -> {
                    if (sizeInMB <= MAX_HEADER_SIZE)
                        fit = true;
                }
            }
            compress();
        }
    }

    private void compress() throws IOException {
        BufferedImage buffer = ImageIO.read(new ByteArrayInputStream(imageBytes));
        Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(format);
        if (!imageWriters.hasNext()) {
            throw new IllegalStateException("No image writer found!");
        }
        float imageQuality = 0.8f;
        ImageWriter imageWriter = imageWriters.next();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(baos);
        imageWriter.setOutput(imageOutputStream);
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality(imageQuality);

        imageWriter.write(null, new IIOImage(buffer, null, null), imageWriteParam);
        imageWriter.dispose();

        this.imageBytes = baos.toByteArray();
    }

    private void fitDimension() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
        ImageInputStream inputStream = ImageIO.createImageInputStream(bais);
        if (!ImageIO.getImageReaders(inputStream).hasNext()) {
            throw new IllegalStateException("No image reader found!");
        }
        BufferedImage buffer = ImageIO.read(inputStream);
        Dimension imageSize = new Dimension(buffer.getWidth(), buffer.getHeight());
        Dimension targetSize = new Dimension();
        switch (kind) {
            case AVATAR -> {
                if (imageSize.width == VALID_AVATAR_WIDTH && imageSize.height == VALID_AVATAR_HEIGHT) {
                    return;
                }
                targetSize = new Dimension(VALID_AVATAR_WIDTH, VALID_AVATAR_HEIGHT);
            }
            case HEADER -> {
                if (imageSize.width == VALID_HEADER_WIDTH && imageSize.height == VALID_HEADER_HEIGHT) {
                    return;
                }
                targetSize = new Dimension(VALID_HEADER_WIDTH, VALID_HEADER_HEIGHT);
            }
            case TWEET -> {
                if (imageSize.width <= VALID_TWEET_WIDTH && imageSize.height <= VALID_TWEET_HEIGHT) {
                    return;
                }
                targetSize = getValidDimension(new Dimension(VALID_TWEET_WIDTH, VALID_TWEET_HEIGHT), imageSize);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage resizedImage = resizeImage(buffer, targetSize);
        ImageIO.write(resizedImage, format, baos);
        this.imageBytes = baos.toByteArray();
    }

    private BufferedImage resizeImage(BufferedImage originalImage, Dimension targetSize) {
        java.awt.Image resultingImage = originalImage.getScaledInstance(targetSize.width, targetSize.height, java.awt.Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetSize.width, targetSize.height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private Dimension getValidDimension(Dimension boundary, Dimension imageSize) {
        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (imageSize.width  * ratio), (int) (imageSize.height * ratio));
    }

}

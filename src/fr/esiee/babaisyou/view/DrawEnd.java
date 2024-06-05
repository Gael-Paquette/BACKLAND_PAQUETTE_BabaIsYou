package fr.esiee.babaisyou.view;

import com.github.forax.zen.ApplicationContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

public class DrawEnd {
  private final int xOrigin;
  private final int yOrigin;
  private final int width;
  private final int height;
  private final ImagesEndLoader imagesEndLoader;

  public DrawEnd(int xOrigin, int yOrigin, int width, int height, ImagesEndLoader imagesEndLoader) {
    Objects.requireNonNull(imagesEndLoader);
    if (xOrigin < 0 || yOrigin < 0 || width < 0 || height < 0) {
      throw new IllegalArgumentException("xOrigin, yOrigin, width and height are negative.");
    }
    this.xOrigin = xOrigin;
    this.yOrigin = yOrigin;
    this.width = width;
    this.height = height;
    this.imagesEndLoader = imagesEndLoader;
  }

  private void drawImage(Graphics2D graphics, Image image, int x, int y, float dimX, float dimY) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(image);
    if (x < 0 || y < 0 || dimX < 0 || dimY < 0) {
      throw new IllegalArgumentException("x, y, dimX and dimY must be positive");
    }

    var width = image.getWidth(null);
    var height = image.getHeight(null);
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
            y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }

  private void drawSquare(Graphics2D graphics, String nameImage, int x, int y) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(nameImage);
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("x and y must be positive");
    }

    var image = imagesEndLoader.getImage(nameImage);
    var dimX = image.getWidth(null);
    var dimY = image.getHeight(null);
    drawImage(graphics, image, x - (dimX / 2), y - (dimY / 2), dimX, dimY);
  }

  private void drawEnd(Graphics2D graphics, String nameImage) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(nameImage);

    graphics.clearRect(xOrigin, yOrigin, width, height);
    graphics.setColor(Color.BLACK);
    graphics.fillRect(xOrigin, yOrigin, width, height);
    drawSquare(graphics, nameImage, xOrigin + (width / 2), yOrigin + (height / 2));
  }

  public static void draw(ApplicationContext context, DrawEnd view, String nameImage) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(view);

    context.renderFrame(graphics -> view.drawEnd(graphics, nameImage));

  }
}

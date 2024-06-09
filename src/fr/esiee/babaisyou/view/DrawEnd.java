package fr.esiee.babaisyou.view;

import com.github.forax.zen.ApplicationContext;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * The {@code DrawEnd} class is responsible for drawing the end game screens, such as victory or defeat.
 */
public class DrawEnd {
  private final int xOrigin;
  private final int yOrigin;
  private final int width;
  private final int height;
  private final ImagesEndLoader imagesEndLoader;

  /**
   * Constructs a {@code DrawEnd} instance with specified origin, dimensions, and image loader.
   *
   * @param xOrigin the x-coordinate of the origin
   * @param yOrigin the y-coordinate of the origin
   * @param width the width of the drawing area
   * @param height the height of the drawing area
   * @param imagesEndLoader the loader for end game images
   * @throws IllegalArgumentException if any of the dimensions are negative
   * @throws NullPointerException if {@code imagesEndLoader} is null
   */
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

  /**
   * Draws an image at the specified location with the specified dimensions.
   *
   * @param graphics the graphics context
   * @param image the image to draw
   * @param x the x-coordinate to draw the image
   * @param y the y-coordinate to draw the image
   * @param dimX the width of the area to draw the image
   * @param dimY the height of the area to draw the image
   * @throws IllegalArgumentException if any of the coordinates or dimensions are negative
   * @throws NullPointerException if {@code graphics} or {@code image} is null
   */
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

  /**
   * Draws a square image at the specified location.
   *
   * @param graphics the graphics context
   * @param nameImage the name of the image to draw
   * @param x the x-coordinate to draw the image
   * @param y the y-coordinate to draw the image
   * @throws IllegalArgumentException if {@code x} or {@code y} is negative
   * @throws NullPointerException if {@code graphics} or {@code nameImage} is null
   */
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

  /**
   * Draws the end game screen with the specified image.
   *
   * @param graphics the graphics context
   * @param nameImage the name of the image to draw
   * @throws NullPointerException if {@code graphics} or {@code nameImage} is null
   */
  private void drawEnd(Graphics2D graphics, String nameImage) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(nameImage);

    graphics.clearRect(xOrigin, yOrigin, width, height);
    graphics.setColor(Color.BLACK);
    graphics.fillRect(xOrigin, yOrigin, width, height);
    drawSquare(graphics, nameImage, xOrigin + (width / 2), yOrigin + (height / 2));
  }

  /**
   * Draws the end game screen using the specified application context and {@code DrawEnd} view.
   *
   * @param context the application context
   * @param view the {@code DrawEnd} instance specifying the drawing parameters
   * @param nameImage the name of the image to draw
   * @throws NullPointerException if {@code context}, {@code view}, or {@code nameImage} is null
   */
  public static void draw(ApplicationContext context, DrawEnd view, String nameImage) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(view);

    context.renderFrame(graphics -> view.drawEnd(graphics, nameImage));

  }
}

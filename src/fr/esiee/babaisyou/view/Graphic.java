package fr.esiee.babaisyou.view;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import com.github.forax.zen.ApplicationContext;
import fr.esiee.babaisyou.model.GameBoard;

public class Graphic {

  public static void drawImage(Graphics2D graphics2D, BufferedImage image, float x, float y, float dimX, float dimY) {
    Objects.requireNonNull(graphics2D);
    Objects.requireNonNull(image);
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("Invalid x: " + x + ", y: " + y);
    }
    var width = image.getWidth();
    var height = image.getHeight();
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
            y + (dimY - scale * height) / 2);
    graphics2D.drawImage(image, transform, null);
  }

  public static void drawCell(Graphics2D graphics, GameBoard gameBoard, ImagesLoader imagesLoader, int row, int col, int width, int height) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(gameBoard);
    var widthSquare = width / gameBoard.getRows() - 10;
    var heightSquare = height / gameBoard.getRows() - 10;
    var x = row * widthSquare;
    var y = col * heightSquare;
    var square = gameBoard.getSquare(row, col);
    if (square.isObject()) {
      var image = imagesLoader.getImageObject(square.name());
      drawImage(graphics, image, x + 2, y + 2, widthSquare - 4, heightSquare - 4);
    } else {
      var image = imagesLoader.getImageText(square.name());
      drawImage(graphics, image, x + 2, y + 2, widthSquare - 4, heightSquare - 4);
    }
  }

  public static void drawGameBoard(Graphics2D graphics, GameBoard gameBoard, ImagesLoader imagesLoader, int width, int height) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(gameBoard);
    Objects.requireNonNull(imagesLoader);
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height must be greater than 0");
    }
    graphics.setColor(Color.BLACK);
    graphics.fill(new Rectangle2D.Float(0, 0, width, height));
    for (var row = 0; row < gameBoard.getRows(); row++) {
      for (var col = 0; col < gameBoard.getCols(); col++) {
        drawCell(graphics, gameBoard, imagesLoader, row, col, width, height);
      }
    }
  }

  public static void draw(ApplicationContext context, GameBoard gameBoard, ImagesLoader imagesLoader, int width, int height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(gameBoard);
    Objects.requireNonNull(imagesLoader);
    context.renderFrame(graphics -> {
      drawGameBoard(graphics, gameBoard, imagesLoader, width, height);
    });
  }
}

package fr.esiee.babaisyou.view;

import com.github.forax.zen.ApplicationContext;
import fr.esiee.babaisyou.model.GameBoard;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

/**
 * The {@code DrawGame} class is responsible for drawing the game board and its elements.
 */
public class DrawGame {
  private final int xOrigin;
  private final int yOrigin;
  private final int width;
  private final int height;
  private final int squareSize;
  private final GameBoard board;
  private final ImagesLoader imagesLoader;

  /**
   * Constructs a {@code DrawGame} instance with specified origin, dimensions, game board, and image loader.
   *
   * @param xOrigin the x-coordinate of the origin
   * @param yOrigin the y-coordinate of the origin
   * @param width the width of the drawing area
   * @param height the height of the drawing area
   * @param board the game board to be drawn
   * @param imagesLoader the loader for game images
   * @throws IllegalArgumentException if any of the origin coordinates or dimensions are negative
   * @throws NullPointerException if {@code board} or {@code imagesLoader} is null
   */
  public DrawGame(int xOrigin, int yOrigin, int width, int height, GameBoard board, ImagesLoader imagesLoader) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(imagesLoader);
    if (xOrigin < 0 || yOrigin < 0 || width < 0 || height < 0) {
      throw new IllegalArgumentException("xOrigin and yOrigin must be not-negative");
    }
    this.squareSize = Math.min(width / board.getCols(), height / board.getRows());
    this.width = squareSize * board.getCols();
    this.height = squareSize * board.getRows();
    this.xOrigin = (width - this.width) / 2;
    this.yOrigin = (height - this.height) / 2;
    this.board = board;
    this.imagesLoader = imagesLoader;
  }

  /**
   * Calculates the x-coordinate for a given column index.
   *
   * @param j the column index
   * @return the x-coordinate
   */
  private float xFromJ(int j) {
    return xOrigin + (j * squareSize);
  }

  /**
   * Calculates the y-coordinate for a given row index.
   *
   * @param i the row index
   * @return the y-coordinate
   */
  private float yFromI(int i) {
    return yOrigin + (i * squareSize);
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
   * @throws NullPointerException if {@code graphics} or {@code image} is null
   */
  public static void drawImage(Graphics2D graphics, Image image, float x, float y, float dimX, float dimY) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(image);

    var width = image.getWidth(null);
    var height = image.getHeight(null);
    var scale = Math.min(dimX / width, dimY / height);
    var transform = new AffineTransform(scale, 0, 0, scale, x + (dimX - scale * width) / 2,
            y + (dimY - scale * height) / 2);
    graphics.drawImage(image, transform, null);
  }

  /**
   * Draws a square at the specified row and column indices.
   *
   * @param graphics the graphics context
   * @param i the row index
   * @param j the column index
   * @throws NullPointerException if {@code graphics} is null
   */
  private void drawSquare(Graphics2D graphics, int i, int j) {
    Objects.requireNonNull(graphics);

    var y = yFromI(i);
    var x = xFromJ(j);
    var square = board.getSquare(i, j);
    if (square.isObject()) {
      if (!square.name().equals("NULL")) {
        drawImage(graphics, imagesLoader.getImageObject(square.name()), x + 2, y + 2, squareSize, squareSize);
      }
    } else {
      drawImage(graphics, imagesLoader.getImageText(square.name()), x + 2, y + 2, squareSize, squareSize);
    }
  }

  /**
   * Draws the game board.
   *
   * @param graphics the graphics context
   * @param board the game board to be drawn
   * @throws NullPointerException if {@code graphics} or {@code board} is null
   */
  private void drawGameBoard(Graphics2D graphics, GameBoard board) {
    Objects.requireNonNull(graphics);
    Objects.requireNonNull(board);

    graphics.clearRect(xOrigin, yOrigin, width, height);
    graphics.setColor(Color.BLACK);
    graphics.fill(new Rectangle2D.Float(xOrigin, yOrigin, width, height));
    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j < board.getCols(); j++) {
        drawSquare(graphics, i, j);
      }
    }
  }

  /**
   * Draws the game board using the specified application context and {@code DrawGame} view.
   *
   * @param context the application context
   * @param board the game board to be drawn
   * @param view the {@code DrawGame} instance specifying the drawing parameters
   * @throws NullPointerException if {@code context}, {@code board}, or {@code view} is null
   */
  public static void draw(ApplicationContext context, GameBoard board, DrawGame view) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(board);
    Objects.requireNonNull(view);

    context.renderFrame(graphics -> view.drawGameBoard(graphics, board));
  }

}
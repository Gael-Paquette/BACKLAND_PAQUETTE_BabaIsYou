package fr.esiee.babaisyou.view;

import com.github.forax.zen.ApplicationContext;

import java.awt.*;
import java.util.Objects;

/**
 * The {@code ClearWindow} class is responsible for clearing a specified area of the window.
 */
public class ClearWindow {
  private final int xOrigin;
  private final int yOrigin;
  private final int width;
  private final int height;

  /**
   * Constructs a {@code ClearWindow} instance with the specified origin and dimensions.
   *
   * @param xOrigin the x-coordinate of the origin
   * @param yOrigin the y-coordinate of the origin
   * @param width the width of the area to clear
   * @param height the height of the area to clear
   * @throws IllegalArgumentException if any of the parameters are negative
   */
  public ClearWindow(int xOrigin, int yOrigin, int width, int height) {
    if (xOrigin < 0 || yOrigin < 0 || width < 0 || height < 0) {
      throw new IllegalArgumentException("xOrigin, yOrigin, width and height are negative.");
    }
    this.xOrigin = xOrigin;
    this.yOrigin = yOrigin;
    this.width = width;
    this.height = height;
  }

  /**
   * Clears the specified area using the given graphics context.
   *
   * @param graphics the graphics context to use for clearing
   * @throws NullPointerException if the graphics context is null
   */
  private void clearAll(Graphics2D graphics) {
    Objects.requireNonNull(graphics);
    graphics.clearRect(xOrigin, yOrigin, width, height);
  }

  /**
   * Clears the window using the specified application context and {@code ClearWindow} view.
   *
   * @param context the application context
   * @param view the {@code ClearWindow} instance specifying the area to clear
   * @throws NullPointerException if the context or view is null
   */
  public static void clearWindow(ApplicationContext context, ClearWindow view) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(view);

    context.renderFrame(view::clearAll);
  }
}

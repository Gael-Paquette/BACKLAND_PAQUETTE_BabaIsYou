package fr.esiee.babaisyou.view;

import com.github.forax.zen.ApplicationContext;
import fr.esiee.babaisyou.model.GameBoard;

import java.awt.*;
import java.util.Objects;

public class ClearWindow {
  private final int xOrigin;
  private final int yOrigin;
  private final int width;
  private final int height;

  public ClearWindow(int xOrigin, int yOrigin, int width, int height) {
    if (xOrigin < 0 || yOrigin < 0 || width < 0 || height < 0) {
      throw new IllegalArgumentException("xOrigin, yOrigin, width and height are negative.");
    }
    this.xOrigin = xOrigin;
    this.yOrigin = yOrigin;
    this.width = width;
    this.height = height;
  }

  private void clearAll(Graphics2D graphics) {
    Objects.requireNonNull(graphics);
    graphics.clearRect(xOrigin, yOrigin, width, height);
  }

  public static void clearWindow(ApplicationContext context, ClearWindow view) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(view);

    context.renderFrame(view::clearAll);
  }
}

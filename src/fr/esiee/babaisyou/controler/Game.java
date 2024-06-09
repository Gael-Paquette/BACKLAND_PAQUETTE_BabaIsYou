package fr.esiee.babaisyou.controler;

import com.github.forax.zen.ApplicationContext;
import com.github.forax.zen.Event;
import fr.esiee.babaisyou.model.Direction;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Rule;
import fr.esiee.babaisyou.model.Square;
import fr.esiee.babaisyou.view.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The {@code Game} class contains the main logic for running the "Baba Is You" game.
 */
public class Game {
  /**
   * Creates a new {@code Rule} instance.
   *
   * @return a new {@code Rule} instance
   */
  public static Rule newRule() {
    return new Rule();
  }

  /**
   * Creates a new {@code ImagesLoader} instance with predefined lists of images.
   *
   * @return a new {@code ImagesLoader} instance
   */
  public static ImagesLoader newImagesLoader() {
    return new ImagesLoader(
            List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
            List.of("IS", "ON", "HAS", "AND"),
            List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
    );
  }

  /**
   * Creates a new {@code ImagesEndLoader} instance with predefined lists of end images.
   *
   * @return a new {@code ImagesEndLoader} instance
   */
  public static ImagesEndLoader newImagesEndLoader() {
    return new ImagesEndLoader(
            List.of("CONGRATULATIONS", "DEFEAT")
    );
  }

  /**
   * Draws the defeat screen and exits the game.
   *
   * @param context the application context
   * @param drawEnd the draw end instance
   */
  public static void drawDefeat(ApplicationContext context, DrawEnd drawEnd) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(drawEnd);
    DrawEnd.draw(context, drawEnd, "DEFEAT");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {}
    System.exit(0);
  }

  /**
   * Draws the victory screen.
   *
   * @param context the application context
   * @param drawEnd the draw end instance
   */
  public static void drawVictory(ApplicationContext context, DrawEnd drawEnd) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(drawEnd);
    DrawEnd.draw(context, drawEnd, "CONGRATULATIONS");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException ignored) {}
  }

  /**
   * Draws the end screen based on the game state.
   *
   * @param context the application context
   * @param board the game board
   * @param rule the game rules
   * @param drawEnd the draw end instance
   */
  public static void drawEnd(ApplicationContext context, GameBoard board, Rule rule, DrawEnd drawEnd) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(board);
    Objects.requireNonNull(rule);
    Objects.requireNonNull(drawEnd);

    if (!rule.playerIsWin(board)) {
      Game.drawDefeat(context, drawEnd);
    } else if (!rule.playerIsPresent(board)) {
      Game.drawDefeat(context, drawEnd);
    } else if(rule.playerHasLost(board)) {
      Game.drawDefeat(context, drawEnd);
    } else {
      Game.drawVictory(context, drawEnd);
    }
  }

  /**
   * Clears the game window.
   *
   * @param context the application context
   * @param width the width of the window
   * @param height the height of the window
   */
  public static void clearWindow(ApplicationContext context, int width, int height) {
    Objects.requireNonNull(context);
    var clearWidow = new ClearWindow(0, 0, width, height);
    ClearWindow.clearWindow(context, clearWidow);
  }

  /**
   * Retrieves the list of player squares from the game board.
   *
   * @param board the game board
   * @return the list of player squares
   */
  public static List<Square> getSquaresPlayer(GameBoard board) {
    Objects.requireNonNull(board);
    return board.getSquaresPlayer();
  }

  /**
   * Draws the current state of the game.
   *
   * @param context the application context
   * @param board the game board
   * @param imagesLoader the images loader
   * @param width the width of the window
   * @param height the height of the window
   * @throws IllegalArgumentException if width or height is less than 0
   */
  public static void drawGame(ApplicationContext context, GameBoard board, ImagesLoader imagesLoader, int width, int height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(board);
    Objects.requireNonNull(imagesLoader);
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("width and height must be greater than 0");
    }

    var drawGame = new DrawGame(0, 0, width, height, board, imagesLoader);
    DrawGame.draw(context, board, drawGame);
  }

  /**
   * Handles player movement based on the direction.
   *
   * @param board the game board
   * @param rule the game rules
   * @param player the list of player squares
   * @param direction the direction to move
   */
  public static void eventWithADirection(GameBoard board, Rule rule, List<Square> player, Direction direction) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(rule);
    Objects.requireNonNull(player);
    Objects.requireNonNull(direction);

    for (var square : player) {
      if (board.facingABlock(square, direction))
        board.push(square, direction);
      board.movePlayer(square, direction);
    }
    if(rule.namesToTransform(board) != null)
      board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
  }

  /**
   * Manages events in the game.
   *
   * @param event the event to manage
   * @param board the game board
   * @param rule the game rules
   * @param player the list of player squares
   */
  public static void manageEventGame(Event event, GameBoard board, Rule rule, List<Square> player) {
    Objects.requireNonNull(event);
    Objects.requireNonNull(board);
    Objects.requireNonNull(rule);
    Objects.requireNonNull(player);

    var eventGame = new EventGame();
    var code = eventGame.manageEvent(event);
    switch (code) {
      case EXIT -> System.exit(0);
      case UP -> Game.eventWithADirection(board, rule, player, Direction.UP);
      case DOWN -> Game.eventWithADirection(board, rule, player, Direction.DOWN);
      case LEFT -> Game.eventWithADirection(board, rule, player, Direction.LEFT);
      case RIGHT -> Game.eventWithADirection(board, rule, player, Direction.RIGHT);
      case AVOID -> {}
    }
  }

  /**
   * Plays a single level of the game.
   *
   * @param context the application context
   * @param board the game board
   * @param rule the game rules
   * @param imagesLoader the images loader
   * @param width the width of the window
   * @param height the height of the window
   * @throws IllegalArgumentException if width or height is less than 0
   */
  public static void playGameLevel(ApplicationContext context, GameBoard board, Rule rule, ImagesLoader imagesLoader, int width, int height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(board);
    Objects.requireNonNull(rule);
    Objects.requireNonNull(imagesLoader);
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("width and height must be greater than 0");
    }

    do {
      var player = Game.getSquaresPlayer(board);
      Game.drawGame(context, board, imagesLoader, width, height);
      var event = context.pollOrWaitEvent(100);
      if (event != null) {
        Game.manageEventGame(event, board, rule, player);
      }
    } while(rule.playerIsPresent(board) && !rule.playerIsWin(board) && !rule.playerHasLost(board));
  }

  /**
   * Plays the game through multiple levels.
   *
   * @param context the application context
   * @param imagesEndLoader the images end loader
   * @param imagesLoader the images loader
   * @param rule the game rules
   * @param width the width of the window
   * @param height the height of the window
   * @throws IllegalArgumentException if width or height is less than 0
   */
  public static void playGame(ApplicationContext context, ImagesEndLoader imagesEndLoader, ImagesLoader imagesLoader, Rule rule, int width, int height) {
    Objects.requireNonNull(context);
    Objects.requireNonNull(imagesEndLoader);
    Objects.requireNonNull(imagesLoader);
    Objects.requireNonNull(rule);
    if (width < 0 || height < 0) {
      throw new IllegalArgumentException("width and height must be greater than 0");
    }
    var level = 0;
    do {
      try {
        Game.clearWindow(context, width, height);
        var drawEnd = new DrawEnd(0, 0, width, height, imagesEndLoader);
        GameBoard board = new GameBoard(Paths.get("levels/level" + level + ".txt"));
        Game.playGameLevel(context, board, rule, imagesLoader, width, height);
        Game.drawEnd(context, board, rule, drawEnd);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      level++;
    } while (level < 7);
  }
}

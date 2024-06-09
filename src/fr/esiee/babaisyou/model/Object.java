package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents an Object on the game board.
 * An Object has specific characteristics and behaviors defined by its type.
 * Valid objects are: "NULL", "BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", and "FLOWER".
 *
 * @param x    the x-coordinate of the Object.
 * @param y    the y-coordinate of the Object.
 * @param name the name of the Object.
 * @throws IllegalArgumentException if the object name is invalid or if the coordinates are negative.
 * @throws NullPointerException     if the name is null.
 */
public record Object(int x, int y, String name) implements Square {

  /**
   * Constructs a new Object with the specified coordinates and name.
   *
   * @throws IllegalArgumentException if the object name is not valid or if x or y are less than 0.
   * @throws NullPointerException if the name is null.
   */
  public Object {
    Objects.requireNonNull(name);

    var objects = List.of("NULL", "BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if (!objects.contains(name)) {
      throw new IllegalArgumentException("Invalid object : " + name);
    }
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("X and Y coordinates must be greater than or equal to 0");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isName() { return false; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isOperator() { return false; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProperty() { return false; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isObject() { return true; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() { return this.name.equals("NULL"); }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPushable(GameBoard board) {
    Objects.requireNonNull(board);
    Rule rule = new Rule();
    return switch (this.name) {
      case "BABA" -> rule.isValidRule(board,"BABA", "IS", "PUSH");
      case "FLAG" -> rule.isValidRule(board, "FLAG", "IS", "PUSH");
      case "WALL" -> rule.isValidRule(board, "WALL", "IS", "PUSH");
      case "WATER" -> rule.isValidRule(board, "WATER", "IS", "PUSH");
      case "SKULL" -> rule.isValidRule(board, "SKULL", "IS", "PUSH");
      case "LAVA" -> rule.isValidRule(board, "LAVA", "IS", "PUSH");
      case "ROCK" -> rule.isValidRule(board, "ROCK", "IS", "PUSH");
      case "FLOWER" -> rule.isValidRule(board, "FLOWER", "IS", "PUSH");
      default -> false;
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTraversable(GameBoard board) {
    Objects.requireNonNull(board);
    Rule rule = new Rule();
    if(this.isEmpty())
      return true;
    else
      return rule.isTraversable(board, this.name());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String representation() {
    return switch (name) {
      case "BABA" -> "X";
      case "FLAG" -> "⚑";
      case "WALL" -> "■";
      case "WATER" -> "~";
      case "SKULL" -> "¤";
      case "LAVA" -> "§";
      case "ROCK" -> "*";
      case "FLOWER" -> "#";
      default -> " ";
    };
  }

  /**
   * Returns a string representation of the Object.
   *
   * @return a string representing the Object.
   */
  @Override
  public String toString() {
    return ("(Object) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

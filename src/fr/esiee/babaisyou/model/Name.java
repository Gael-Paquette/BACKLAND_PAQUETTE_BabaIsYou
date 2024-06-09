package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Name on the game board.
 * A Name has specific characteristics and behaviors defined by its type.
 * Valid names are: "BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", and "FLOWER".
 *
 * @param x    the x-coordinate of the Name.
 * @param y    the y-coordinate of the Name.
 * @param name the name of the Name.
 * @throws IllegalArgumentException if the name is invalid or if the coordinates are negative.
 * @throws NullPointerException     if the name is null.
 */
public record Name(int x, int y, String name) implements Square {

  /**
   * Constructs a new Name with the specified coordinates and name.
   *
   * @throws IllegalArgumentException if the name is not valid or if x or y are less than 0.
   * @throws NullPointerException if the name is null.
   */
  public Name {
    Objects.requireNonNull(name);

    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if (!names.contains(name)) {
      throw new IllegalArgumentException("Invalid name : " + name);
    }
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("X and Y coordinates must be greater than or equal to 0");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isName() { return true; }

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
  public boolean isObject() { return false; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEmpty() { return false; }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isPushable(GameBoard board) {
    Objects.requireNonNull(board);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isTraversable(GameBoard board) {
    Objects.requireNonNull(board);
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String representation() {
    return switch (name) {
      case "BABA" -> "BABA";
      case "FLAG" -> "FLAG";
      case "WALL" -> "WALL";
      case "WATER" -> "WATER";
      case "SKULL" -> "SKULL";
      case "LAVA" -> "LAVA";
      case "ROCK" -> "ROCK";
      case "FLOWER" -> "FLOWER";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }

  /**
   * Returns a string representation of the Name.
   *
   * @return a string representing the Name.
   */
  @Override
  public String toString() {
    return ("(Name) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

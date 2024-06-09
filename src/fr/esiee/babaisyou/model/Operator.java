package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents an Operator on the game board.
 * An Operator has specific characteristics and behaviors defined by its type.
 * Valid operators are: "IS", "ON", "HAS", and "ROCK".
 *
 * @param x    the x-coordinate of the Operator.
 * @param y    the y-coordinate of the Operator.
 * @param name the name of the Operator.
 * @throws IllegalArgumentException if the operator name is invalid or if the coordinates are negative.
 * @throws NullPointerException     if the name is null.
 */
public record Operator(int x, int y, String name) implements Square {

  /**
   * Constructs a new Operator with the specified coordinates and name.
   *
   * @throws IllegalArgumentException if the operator name is not valid or if x or y are less than 0.
   * @throws NullPointerException if the name is null.
   */
  public Operator {
    Objects.requireNonNull(name);

    var operators = List.of("IS", "ON", "HAS", "ROCK");
    if (!operators.contains(name)) {
      throw new IllegalArgumentException("Invalid operator : " + name);
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
  public boolean isOperator() { return true; }

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
      case "IS" -> "IS";
      case "ON" -> "ON";
      case "HAS" -> "HAS";
      case "ROCK" -> "ROCK";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }

  /**
   * Returns a string representation of the Operator.
   *
   * @return a string representing the Operator.
   */
  @Override
  public String toString() {
    return ("(Operator) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

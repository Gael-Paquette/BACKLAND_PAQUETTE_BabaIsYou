package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a Property on the game board.
 * A Property has specific characteristics and behaviors defined by its type.
 * Valid properties are: "YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", and "SINK".
 *
 * @param x    the x-coordinate of the Property.
 * @param y    the y-coordinate of the Property.
 * @param name the name of the Property.
 * @throws IllegalArgumentException if the property name is invalid or if the coordinates are negative.
 * @throws NullPointerException     if the name is null.
 */
public record Property(int x, int y, String name) implements Square {

  /**
   * Constructs a new Property with the specified coordinates and name.
   *
   * @throws IllegalArgumentException if the property name is not valid or if x or y are less than 0.
   * @throws NullPointerException if the name is null.
   */
  public Property {
    Objects.requireNonNull(name);

    var properties = List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK");
    if (!properties.contains(name)) {
      throw new IllegalArgumentException("Invalid property : " + name);
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
  public boolean isProperty() { return true; }

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
      case "YOU" -> "YOU";
      case "WIN" -> "WIN";
      case "STOP" -> "STOP";
      case "PUSH" -> "PUSH";
      case "MELT" -> "MELT";
      case "HOT" -> "HOT";
      case "DEFEAT" -> "DEFEAT";
      case "SINK" -> "SINK";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }

  /**
   * Returns a string representation of the Property.
   *
   * @return a string representing the Property.
   */
  @Override
  public String toString() {
    return ("(Property) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

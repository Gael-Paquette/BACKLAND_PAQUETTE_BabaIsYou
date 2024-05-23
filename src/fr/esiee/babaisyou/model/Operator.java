package fr.esiee.babaisyou.model;

import java.util.Objects;

public record Operator(int x, int y, String name) implements Square {
  public Operator {
    Objects.requireNonNull(name);
    if (!name.equals("IS") && !name.equals("ON") && !name.equals("HAS") && !name.equals("ROCK")) {
      throw new IllegalArgumentException("Operator must be 'IS' or 'ON' or 'HAS' or 'ROCK'");
    }
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("X and Y coordinates must be greater than or equal to 0");
    }
  }

  @Override
  public int getX() { return x; }

  @Override
  public int getY() { return y; }

  @Override
  public boolean isName() { return false; }

  @Override
  public boolean isOperator() { return true; }

  @Override
  public boolean isProperty() { return false; }

  @Override
  public boolean isObject() { return false; }

  @Override
  public boolean isEmpty() { return false; }

  @Override
  public boolean isPushable() { return false; }

  @Override
  public String representation() {
    return switch (name) {
      case "IS" -> "I";
      case "ON" -> "O";
      case "HAS" -> "H";
      case "ROCK" -> "R";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }
}

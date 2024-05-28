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
  public boolean isPushable(GameBoard board) {
    Objects.requireNonNull(board);
    return true;
  }

  @Override
  public boolean isTraversable(GameBoard board) {
    Objects.requireNonNull(board);
    return false;
  }

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
}

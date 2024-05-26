package fr.esiee.babaisyou.model;

import java.util.Objects;

public record Property(int x, int y, String name) implements Square {
  public Property {
    Objects.requireNonNull(name);
    if (!name.equals("YOU") && !name.equals("WIN") && !name.equals("STOP") && !name.equals("PUSH") && !name.equals("MELT") && !name.equals("HOT") && !name.equals("DEFEAT") && !name.equals("SINK")) {
      throw new IllegalArgumentException("Invalid property : " + name);
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
  public boolean isOperator() { return false; }

  @Override
  public boolean isProperty() { return true; }

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
      case "YOU" -> "Y";
      case "WIN" -> "W";
      case "STOP" -> "S";
      case "PUSH" -> "P";
      case "MELT" -> "M";
      case "HOT" -> "H";
      case "DEFEAT" -> "D";
      case "SINK" -> "S";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }
}

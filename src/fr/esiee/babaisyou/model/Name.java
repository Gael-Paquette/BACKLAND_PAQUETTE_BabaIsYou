package fr.esiee.babaisyou.model;

import java.util.Objects;

public record Name(int x, int y, String name) implements Square {
  public Name {
    Objects.requireNonNull(name);
    Objects.requireNonNull(name);
    if (!name.equals("BABA") && !name.equals("FLAG") && !name.equals("WALL") && !name.equals("WATER") && !name.equals("SKULL") && !name.equals("LAVA") && !name.equals("ROCK") && !name.equals("FLOWER")) {
      throw new IllegalArgumentException("Invalid name: " + name);
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
  public boolean isName() { return true; }

  @Override
  public boolean isOperator() { return false; }

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
      case "BABA" -> "B";
      case "FLAG" -> "F";
      case "WALL" -> "W";
      case "WATER" -> "W";
      case "SKULL" -> "S";
      case "LAVA" -> "L";
      case "ROCK" -> "R";
      case "FLOWER" -> "F";
      default -> throw new IllegalStateException("Unexpected value: " + name);
    };
  }
}

package fr.esiee.babaisyou.model;

import java.util.Objects;

public record Object(int x, int y, String name) implements Square {
  public Object {
    Objects.requireNonNull(name);
    if (!name.equals("NULL") && !name.equals("BABA") && !name.equals("FLAG") && !name.equals("WALL") && !name.equals("WATER") && !name.equals("SKULL") && !name.equals("LAVA") && !name.equals("ROCK") && !name.equals("FLOWER")) {
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
  public boolean isName() { return false; }

  @Override
  public boolean isOperator() { return false; }

  @Override
  public boolean isProperty() { return false; }

  @Override
  public boolean isObject() { return true; }

  @Override
  public boolean isEmpty() { return this.name.equals("NULL"); }

  @Override
  public boolean isPushable() { return this.name.equals("ROCK"); }

  @Override
  public String representation() {
    return switch (name) {
      case "BABA" -> "X";
      case "FLAG" -> "F";
      case "WALL" -> "■";
      case "WATER" -> "~";
      case "SKULL" -> "¤";
      case "LAVA" -> "§";
      case "ROCK" -> "*";
      default -> " ";
    };
  }
}

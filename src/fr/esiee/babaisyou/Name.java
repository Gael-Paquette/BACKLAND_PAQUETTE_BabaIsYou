package fr.esiee.babaisyou;

import java.util.Objects;

public record Name(int x, int y, String name) implements Square {
  public Name {
    Objects.requireNonNull(name);
    if(x < 0 || y < 0)
      throw new IllegalArgumentException("x and y are negative");
    if (!name.equals("Baba") && !name.equals("Flag") && !name.equals("Wall") && !name.equals("Water") && !name.equals("Skull") && !name.equals("Lava") && !name.equals("Rock")) {
      throw new IllegalArgumentException("Invalid name: " + name);
    }
  }

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
  public String representation() {
      return switch (name) {
          case "Baba" -> "X";
          case "Flag" -> "F";
          case "Wall" -> "■";
          case "Water" -> "~";
          case "Skull" -> "\\uD83D\\uDC80";
          case "Lava" -> "§S";
          case "Rock" -> "*";
          default -> "";
      };
  }

}

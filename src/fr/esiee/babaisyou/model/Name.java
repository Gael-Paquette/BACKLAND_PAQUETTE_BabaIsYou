package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

public record Name(int x, int y, String name) implements Square {
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

  @Override
  public String toString() {
    return ("(Name) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

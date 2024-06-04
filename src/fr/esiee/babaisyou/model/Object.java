package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

public record Object(int x, int y, String name) implements Square {
  public Object {
    Objects.requireNonNull(name);

    var objects = List.of("NULL", "BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if (!objects.contains(name)) {
      throw new IllegalArgumentException("Invalid object : " + name);
    }
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("X and Y coordinates must be greater than or equal to 0");
    }
  }

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

  /*
   return switch (direction) {
    case Direction.LEFT -> getSquare(row, col - 1);
    case Direction.RIGHT -> getSquare(row, col + 1);
    case Direction.UP -> getSquare(row - 1, col);
    case Direction.DOWN -> getSquare(row + 1, col);
  };
  */

  @Override
  public boolean isPushable(GameBoard board) {
    Objects.requireNonNull(board);
    Rule rule = new Rule();
    return switch (this.name) {
      case "BABA" -> rule.isValidRule(board,"BABA", "IS", "PUSH");
      case "FLAG" -> rule.isValidRule(board, "FLAG", "IS", "PUSH");
      case "WALL" -> rule.isValidRule(board, "WALL", "IS", "PUSH");
      case "WATER" -> rule.isValidRule(board, "WATER", "IS", "PUSH");
      case "SKULL" -> rule.isValidRule(board, "SKULL", "IS", "PUSH");
      case "LAVA" -> rule.isValidRule(board, "LAVA", "IS", "PUSH");
      case "ROCK" -> rule.isValidRule(board, "ROCK", "IS", "PUSH");
      case "FLOWER" -> rule.isValidRule(board, "FLOWER", "IS", "PUSH");
      default -> false;
    };
  }

  @Override
  public boolean isTraversable(GameBoard board) {
    Objects.requireNonNull(board);
    Rule rule = new Rule();
    if(this.isEmpty())
      return true;
    else
      return rule.hasNoRules(board, this.name());
  }

  @Override
  public String representation() {
    return switch (name) {
      case "BABA" -> "X";
      case "FLAG" -> "⚑";
      case "WALL" -> "■";
      case "WATER" -> "~";
      case "SKULL" -> "¤";
      case "LAVA" -> "§";
      case "ROCK" -> "*";
      case "FLOWER" -> "#";
      default -> " ";
    };
  }

  @Override
  public String toString() {
    return ("(Object) " + this.name + " x : " + this.x + ", y : " + this.y + ", representation : " + this.representation());
  }
}

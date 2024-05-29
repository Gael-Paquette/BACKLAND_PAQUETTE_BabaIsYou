package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

public record Property(int x, int y, String name) implements Square {
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
}

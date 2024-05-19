package fr.esiee.babaisyou;

import java.util.Objects;

public record Property(int x, int y, String name) implements Square {
  public Property {
    Objects.requireNonNull(name);
    if(x < 0 || y < 0)
      throw new IllegalArgumentException("x and y are negative");
    if (!name.equals("You") && !name.equals("Win") && !name.equals("Stop") && !name.equals("Push") && !name.equals("Melt") && !name.equals("Hot") && !name.equals("Defeat") && !name.equals("Sink")) {
      throw new IllegalArgumentException("Invalid name : " + name);
    }
  }

  @Override
  public boolean isName() { return true; }

  @Override
  public boolean isOperator() { return false; }

  @Override
  public boolean isProperty() { return false; }

  @Override
  public boolean isElement() {
    return false;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public String representation() {
    return "";
  }


}

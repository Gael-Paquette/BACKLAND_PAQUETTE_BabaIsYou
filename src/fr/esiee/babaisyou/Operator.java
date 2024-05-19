package fr.esiee.babaisyou;

import java.util.Objects;

public record Operator(int x, int y, String name) implements Square {
  public Operator {
    Objects.requireNonNull(name);
    if(x < 0 || y < 0)
      throw new IllegalArgumentException("x and y are negative");
    if (!name.equals("Is")) {
      throw new IllegalArgumentException("Operator is not 'Is'");
    }
  }

  @Override
  public boolean isName() { return false; }

  @Override
  public boolean isOperator() { return true; }

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

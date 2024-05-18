package fr.esiee.babaisyou;

import java.util.Objects;

public record Operator(String operator) implements Word {
  public Operator {
    Objects.requireNonNull(operator);
    if (!operator.equals("Is")) {
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
  public boolean isEmpty() { return false; }

  @Override
  public String representation() { return operator; }

  @Override
  public boolean isWall() { return false; }

  @Override
  public boolean isPlayer() { return false; }

  @Override
  public boolean isWater() { return false;}

  @Override
  public boolean isSkull() { return false; }

  @Override
  public boolean isLava() { return false; }

  @Override
  public boolean isRock() { return false; }

  @Override
  public boolean isFlower() { return false; }

}

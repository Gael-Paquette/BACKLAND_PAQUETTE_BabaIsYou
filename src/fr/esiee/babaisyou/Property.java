package fr.esiee.babaisyou;

import java.util.Objects;

public record Property(String property) implements Word {
  public Property {
    Objects.requireNonNull(property);
    if (!property.equals("You") && !property.equals("Win") && !property.equals("Stop") && !property.equals("Push") && !property.equals("Melt") && !property.equals("Hot") && !property.equals("Defeat") && !property.equals("Sink")) {
      throw new IllegalArgumentException("Invalid property : " + property);
    }
  }

  @Override
  public boolean isName() { return true; }

  @Override
  public boolean isOperator() { return false; }

  @Override
  public boolean isProperty() { return false; }

  @Override
  public boolean isEmpty() { return false; }

  @Override
  public String representation() { return property; }

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

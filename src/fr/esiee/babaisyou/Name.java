package fr.esiee.babaisyou;

import java.util.Objects;

public record Name(String name) implements Word {
  public Name {
    Objects.requireNonNull(name);
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
  public boolean isEmpty() { return false; }

  @Override
  public String representation() { return name; }

  @Override
  public boolean isWall() { return false; }

  @Override
  public boolean isPlayer() { return false; }

  @Override
  public boolean isWater() { return false; }

  @Override
  public boolean isSkull() { return false; }

  @Override
  public boolean isLava() { return false; }

  @Override
  public boolean isRock() { return false; }

  @Override
  public boolean isFlower() { return false; }
}

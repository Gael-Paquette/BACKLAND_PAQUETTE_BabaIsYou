package fr.esiee.babaisyou;

public record Rock() implements Element {
  @Override
  public String representation() { return "\uD83D\uDC80"; }

  @Override
  public boolean isName() { return false; }

  @Override
  public boolean isOperator() { return false; }

  @Override
  public boolean isProperty() { return false; }

  @Override
  public boolean isEmpty() { return false; }

  @Override
  public boolean isWall() { return true; }

  @Override
  public boolean isPlayer() { return false; }

  @Override
  public boolean isWater() { return false; }

  @Override
  public boolean isSkull() { return false; }

  @Override
  public boolean isLava() { return false; }

  @Override
  public boolean isRock() { return true; }

  @Override
  public boolean isFlower() { return false; }
}

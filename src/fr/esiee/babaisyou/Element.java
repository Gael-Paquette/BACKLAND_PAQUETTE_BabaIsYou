package fr.esiee.babaisyou;

public interface Element {
    String representation();
    
    boolean isName();
  
    boolean isOperator();
  
    boolean isProperty();
  
    boolean isEmpty();

    boolean isWall();

    boolean isPlayer();

    boolean isWater();

    boolean isSkull();

    boolean isLava();

    boolean isRock();

    boolean isFlower();

}

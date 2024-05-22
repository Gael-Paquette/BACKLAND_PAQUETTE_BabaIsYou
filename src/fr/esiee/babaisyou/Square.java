package fr.esiee.babaisyou;

public sealed interface Square permits Name, Operator, Property, Object {
  
  int x();

  int y();

  String name();

  boolean isName();

  boolean isOperator();

  boolean isProperty();

  boolean isObject();

  boolean isPushable(GameBoard board, Square square);

  boolean isEmpty();

  String representation();

}
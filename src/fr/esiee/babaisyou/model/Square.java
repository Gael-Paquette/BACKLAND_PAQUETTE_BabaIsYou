package fr.esiee.babaisyou.model;

public sealed interface Square permits Name, Operator, Property, Object {
  int x();

  int y();

  boolean isName();

  boolean isOperator();

  boolean isProperty();

  boolean isObject();

  boolean isEmpty();

  boolean isPushable(GameBoard board);

  boolean isTraversable(GameBoard board);

  String representation();

  String name();
}

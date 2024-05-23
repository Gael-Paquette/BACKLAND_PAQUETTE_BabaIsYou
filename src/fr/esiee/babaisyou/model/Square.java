package fr.esiee.babaisyou.model;

public sealed interface Square permits Name, Object, Operator, Property {
  int getX();

  int getY();

  boolean isName();

  boolean isOperator();

  boolean isProperty();

  boolean isObject();

  boolean isEmpty();

  boolean isPushable();

  String representation();

  String name();

}
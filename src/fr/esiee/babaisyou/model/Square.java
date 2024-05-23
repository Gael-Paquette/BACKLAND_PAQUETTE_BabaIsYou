package fr.esiee.babaisyou.model;

public sealed interface Square permits Name, Operator, Property, Object {
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

package fr.esiee.babaisyou;

public sealed interface Word permits Name, Operator, Property {
  boolean isName();

  boolean isOperator();

  boolean isProperty();

  String word();
}

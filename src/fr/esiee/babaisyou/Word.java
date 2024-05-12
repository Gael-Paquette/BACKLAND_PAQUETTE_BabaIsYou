package fr.esiee.babaisyou;

public sealed interface Word extends Element permits Name, Operator, Property {
  @Override
  String representation();

  @Override
  boolean isName();

  @Override
  boolean isOperator();

  @Override
  boolean isProperty();

}

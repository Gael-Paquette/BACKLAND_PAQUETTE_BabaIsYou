package fr.esiee.babaisyou;

public sealed interface RightOperand permits Name, Property {
  boolean isName();

  boolean isProperty();

  String word();
}

package fr.esiee.babaisyou;

public sealed interface LeftOperand permits Name, Property {
  boolean isName();

  boolean isProperty();

  String word();
}

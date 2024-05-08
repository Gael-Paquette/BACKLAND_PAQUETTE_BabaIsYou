package fr.esiee.babaisyou;

import java.util.Objects;

public record Rule(LeftOperand leftOperand, Operator operator, RightOperand rightOperand) {
  public Rule {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
  }




}

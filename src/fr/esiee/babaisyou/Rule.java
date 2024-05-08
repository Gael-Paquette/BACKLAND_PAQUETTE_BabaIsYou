package fr.esiee.babaisyou;

import java.util.Objects;

public record Rule(Word leftOperand, Word operator, Word rightOperand) {
  public Rule {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
  }




}

package fr.esiee.babaisyou;

import java.util.Objects;

public record Operator(String operator) implements Word {
  public Operator {
    Objects.requireNonNull(operator);
    if (!operator.equals("Is")) {
      throw new IllegalArgumentException("Operator is not 'Is'");
    }
  }
}


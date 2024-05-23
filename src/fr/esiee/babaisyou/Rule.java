package fr.esiee.babaisyou;

import java.util.List;
import java.util.Objects;

public class Rule {

  public boolean isValidRule(GameBoard board, String name, String operator, String property) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);

    var names = List.of("Baba", "Flag", "Wall", "Water", "Skull", "Lava", "Rock");
    var operators = List.of("Is");
    var properties = List.of("You", "Win", "Stop", "Push", "Melt", "Hot", "Defeat", "Sink");

    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    if(!operators.contains(operator)) throw new IllegalArgumentException("Operator is not 'Is'");
    if(!properties.contains(property)) throw new IllegalArgumentException("Invalid property : " + property);

    return isValidRuleInDirectionHorizontalOrVertical(board, name, operator, property);
  }

  public boolean isValidRuleCombination(Square leftOperand, Square operator, Square rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    return (leftOperand.isName() && operator.isOperator() && rightOperand.isProperty());
  }

  public boolean isMatchingRule(GameBoard board, int x, int y, String name, String operator, String property, String direction) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);
    Objects.requireNonNull(direction);

    Square s, next1, next2;
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }
    s = board.getSquare(x, y);
    next1 = board.nextSquare(s.x(), s.y(), direction);
    next2 = board.nextSquare(next1.x(), next1.y(), direction);
    return s.name().equals(name) && next1.name().equals(operator) && next2.name().equals(property);
  }

  public boolean isValidRuleInDirectionHorizontalOrVertical(GameBoard board, String name, String operator, String property) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);
    int i, j;
    for (i = 0; i < board.getRows(); i++) {
      for (j = 0; j < board.getCols() - 2; j++) {
        if(isMatchingRule(board, i, j, name, operator, property, "right"))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, "right"), board.nextSquare(i, j+1, "right"));
      }
    }
    for (i = 0; i < board.getRows() - 2; i++) {
      for (j = 0; j < board.getCols(); j++) {
        if (isMatchingRule(board, i, j, name, operator, property, "down"))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, "down"), board.nextSquare(i + 1, j, "down"));
      }
    }
    return false;
  }

}

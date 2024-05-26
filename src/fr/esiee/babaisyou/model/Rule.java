package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

public class Rule {

  public boolean isValidRule(GameBoard board, String name, String operator, String property) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);

    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    var operators = List.of("IS", "ON", "HAS", "AND");
    var properties = List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK");

    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    if(!operators.contains(operator)) throw new IllegalArgumentException("Invalid name : " + operator);
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
    next1 = board.nextSquare(s.getX(), s.getY(), direction);
    next2 = board.nextSquare(next1.getX(), next1.getY(), direction);
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
        if(isMatchingRule(board, i, j, name, operator, property, "RIGHT"))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, "RIGHT"), board.nextSquare(i, j+1, "RIGHT"));
      }
    }
    for (i = 0; i < board.getRows() - 2; i++) {
      for (j = 0; j < board.getCols(); j++) {
        if (isMatchingRule(board, i, j, name, operator, property, "DOWN"))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, "DOWN"), board.nextSquare(i + 1, j, "DOWN"));
      }
    }
    return false;
  }

  public boolean hasNoRules(GameBoard board, String name) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    return !isValidRule(board, name, "IS", "PUSH") && !isValidRule(board, name, "IS", "STOP");
  }
}

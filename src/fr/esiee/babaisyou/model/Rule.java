package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

public class Rule {

  public boolean isValidRule(GameBoard board, String name, String operator, String propertyOrName) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(propertyOrName);

    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    var operators = List.of("IS", "ON", "HAS", "AND");
    var properties = List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK");

    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    if(!operators.contains(operator)) throw new IllegalArgumentException("Invalid name : " + operator);
    if(!names.contains(propertyOrName) && !properties.contains(propertyOrName)) throw new IllegalArgumentException("Invalid property/name : " + propertyOrName);

    return isValidRuleInDirectionHorizontalOrVertical(board, name, operator, propertyOrName);
  }

  public boolean isValidRuleCombination(Square leftOperand, Square operator, Square rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    return (leftOperand.isName() && operator.isOperator() && (rightOperand.isProperty() || rightOperand.isName()));
  }

  public boolean isMatchingRule(GameBoard board, int row, int col, String name, String operator, String property, Direction direction) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);
    Objects.requireNonNull(direction);

    Square s, next1, next2;
    if (row < 0 || row > board.getRows() || col < 0 || col > board.getCols()) {
      throw new IllegalArgumentException("row or col out of bounds");
    }
    s = board.getSquare(row, col);
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
        if(isMatchingRule(board, i, j, name, operator, property, Direction.RIGHT))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, Direction.RIGHT), board.nextSquare(i, j+1, Direction.RIGHT));
      }
    }
    for (i = 0; i < board.getRows() - 2; i++) {
      for (j = 0; j < board.getCols(); j++) {
        if (isMatchingRule(board, i, j, name, operator, property, Direction.DOWN))
          return isValidRuleCombination(board.getSquare(i, j), board.nextSquare(i, j, Direction.DOWN), board.nextSquare(i + 1, j, Direction.DOWN));
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

  public String[] namesToTransform(GameBoard board) {
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    String [] tab = new String[2];
    for(var name1 : names) {
      for(var name2 : names) {
        if(isValidRule(board, name1, "IS", name2)) {
          tab[0] = name1;
          tab[1] = name2;
          return tab;
        }
      }
    }
    return null;
  }

  public String typeOfPlayerPresent(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    return players.stream().filter(p -> isValidRule(board, p, "IS", "YOU")).findFirst().orElse("");
  }

  public boolean playerIsPresent(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    return players.stream().anyMatch(p -> isValidRule(board, p, "IS", "YOU")) && !board.typeofSquare(typeOfPlayerPresent(board)).isEmpty();
  }

  public boolean playerHasLost(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    for(var name : players) {
      if((isValidRule(board, name, "IS", "DEFEAT") || (isValidRule(board, name, "IS", "SINK"))) && board.typeofSquare(name).stream().anyMatch(board::isPlayerOn))
        return true;
    }
    return false;
  }

  public boolean playerIsWin(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
    for(var name : players) {
      if((isValidRule(board, name, "IS", "WIN")) && board.typeofSquare(name).stream().anyMatch(board::isPlayerOn))
        return true;
    }
    return false;
  }
}

package fr.esiee.babaisyou;

import java.util.Objects;

public class Rule {
  public boolean isValidRuleCombination(Square leftOperand, Square operator, Square rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    return (leftOperand.isName() && operator.isOperator() && rightOperand.isProperty());
  }

  public boolean isValidHorizontalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }
    Square next1 = board.nextSquare(x, y, "right");
    Square next2 = board.nextSquare(next1.x(), next1.y(), "right");
    if(board.notInTheBoard(next1) || board.notInTheBoard(next2))
      return false;
    return isValidRuleCombination(board.getSquare(x, y), next1, next2);
  }

  public boolean isValidVerticalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }
    Square next1 = board.nextSquare(x, y, "down");
    Square next2 = board.nextSquare(next1.x(), next1.y(), "down");
    if(board.notInTheBoard(next1) || board.notInTheBoard(next2))
      return false;
    return isValidRuleCombination(board.getSquare(x, y), next1, next2);
  }

  public boolean isPlayerPresent(GameBoard board) {
    Objects.requireNonNull(board);
    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j< board.getCols(); j++) {
        if (board.getSquare(i, j).isProperty() && board.getSquare(i, j).representation().equals("You")) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isWinConditionPresent(GameBoard board) {
    Objects.requireNonNull(board);
    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j< board.getCols(); j++) {
        if (board.getSquare(i, j).isProperty() && board.getSquare(i, j).representation().equals("Win")) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isValidRule1(GameBoard board, String name, String operator, String property) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(property);
    Square s1, s2, s3;
    int i, j;

    if (!name.equals("Baba") && !name.equals("Flag") && !name.equals("Wall") && !name.equals("Water") && !name.equals("Skull") && !name.equals("Lava") && !name.equals("Rock"))
      throw new IllegalArgumentException("Invalid name: " + name);
    if (!operator.equals("Is"))
      throw new IllegalArgumentException("Operator is not 'Is'");
    if (!property.equals("You") && !property.equals("Win") && !property.equals("Stop") && !property.equals("Push") && !property.equals("Melt") && !property.equals("Hot") && !property.equals("Defeat") && !property.equals("Sink"))
      throw new IllegalArgumentException("Invalid name : " + name);

    for (i = 0; i < board.getRows(); i++) {
      for (j = 0; j < board.getCols() - 2; j++) {
        s1 = board.getSquare(i, j);
        s2 = board.nextSquare(s1.x(), s1.y(), "right");
        s3 = board.nextSquare(s2.x(), s2.y(), "right");
        if (s1.name().equals(name) && s2.name().equals(operator) && s3.name().equals(property))
          return isValidRuleCombination(s1, s2, s3);
      }
    }

    for (i = 0; i < board.getRows() - 2; i++) {
      for (j = 0; j < board.getCols(); j++) {
        s1 = board.getSquare(i, j);
        s2 = board.nextSquare(s1.x(), s1.y(), "down");
        s3 = board.nextSquare(s2.x(), s2.y(), "down");
        if (s1.name().equals(name) && s2.name().equals(operator) && s3.name().equals(property))
          return isValidRuleCombination(s1, s2, s3);
      }
    }

    return false;
  }

  public boolean isValidRule2(GameBoard board) {
    Objects.requireNonNull(board);

    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j < board.getCols() - 2; j++) {
        if (isValidHorizontalRule(board, i, j)) {
          return true;
        }
      }
    }

    for (var i = 0; i < board.getRows() - 2; i++) {
      for (var j = 0; j < board.getCols(); j++) {
        if (isValidVerticalRule(board, i, j)) {
          return true;
        }
      }
    }

    return isPlayerPresent(board) && isWinConditionPresent(board);
  }

}

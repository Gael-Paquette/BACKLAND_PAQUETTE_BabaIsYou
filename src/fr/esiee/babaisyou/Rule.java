package fr.esiee.babaisyou;

import java.util.Objects;

public class Rule {
  private static boolean isValidRuleCombination(Square leftOperand, Square operator, Square rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    if (!(leftOperand.isName()) || !(operator.isOperator()) || !(rightOperand.isProperty()))
      return false;

    return (leftOperand.representation().equals(operator.representation()) || leftOperand.representation().equals(rightOperand.representation()));
  }

  private static boolean isValidHorizontalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }

    return isValidRuleCombination(board.getSquare(x, y), board.getSquare(x + 1, y), board.getSquare(x + 2, y));
  }

  private static boolean isValidVerticalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }

    return isValidRuleCombination(board.getSquare(x, y), board.getSquare(x, y + 1), board.getSquare(x, y + 2));
  }

  private static boolean isPlayerPresent(GameBoard board) {
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

  private static boolean isWinConditionPresent(GameBoard board) {
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


  public boolean isValidRule(GameBoard board) {
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

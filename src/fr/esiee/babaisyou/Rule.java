package fr.esiee.babaisyou;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.util.Objects;

public class Rule {
  // Vérifie si une combinaison spécifique de mots constitue une règle valide selon les conditions données dans le jeu Baba Is You
  private static boolean isValidRuleCombination(Element leftOperand, Element operator, Element rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    if (!(leftOperand.isName()) || !(operator.isOperator()) || !(rightOperand.isProperty())) {
      return false;
    }

    return (leftOperand.representation().equals(operator.representation()) || leftOperand.representation().equals(rightOperand.representation()));
  }

  private static boolean isValidHorizontalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }
    var leftOperand = board.getElement(x, y);
    var operator = board.getElement(x + 1, y);
    var rightOperand = board.getElement(x + 2, y);

    return isValidRuleCombination(leftOperand, operator, rightOperand);
  }

  private static boolean isValidVerticalRule(GameBoard board, int x, int y) {
    Objects.requireNonNull(board);
    if (x < 0 || x > board.getRows() || y < 0 || y > board.getCols()) {
      throw new IllegalArgumentException("x or y out of bounds");
    }
    var leftOperand = board.getElement(x, y);
    var operator = board.getElement(x, y + 1);
    var rightOperand = board.getElement(x, y + 2);

    return isValidRuleCombination(leftOperand, operator, rightOperand);
  }

  private static boolean isPlayerPresent(GameBoard board) {
    Objects.requireNonNull(board);
    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j< board.getCols(); j++) {
        var element = board.getElement(i, j);
        if (element.isProperty() && element.representation().equals("You")) {
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
        var element = board.getElement(i, j);
        if (element.isProperty() && element.representation().equals("Win")) {
          return true;
        }
      }
    }
    return false;
  }

  // Vérifie si une règle est valide sur le plateaude jeu fourni. Les règles sont vérifiées horizontalement et verticalement.
  public boolean isValidRule(GameBoard board) {
    Objects.requireNonNull(board);
    // Check all horizontal rules
    for (var i = 0; i < board.getRows(); i++) {
      for (var j = 0; j < board.getCols() - 2; j++) {
        if (isValidHorizontalRule(board, i, j)) {
          return true;
        }
      }
    }
    // Check all vertical rules
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

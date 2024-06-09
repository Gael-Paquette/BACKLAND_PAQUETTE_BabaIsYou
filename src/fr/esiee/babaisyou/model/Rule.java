package fr.esiee.babaisyou.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents the rules and logic of the game.
 * This class contains methods to validate and check various game rules and states.
 */
public class Rule {

  /**
   * Validates a rule based on the given name, operator, and property or name.
   *
   * @param board          the game board to check against.
   * @param name           the name to validate.
   * @param operator       the operator to validate.
   * @param propertyOrName the property or name to validate.
   * @return true if the rule is valid, false otherwise.
   * @throws NullPointerException     if any of the parameters are null.
   * @throws IllegalArgumentException if any of the parameters are invalid.
   */
  public boolean isValidRule(GameBoard board, String name, String operator, String propertyOrName) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(propertyOrName);

    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    var operators = List.of("IS", "ON", "HAS", "AND");
    var properties = List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK");

    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    if(!operators.contains(operator)) throw new IllegalArgumentException("Invalid name : " + operator);
    if(!names.contains(propertyOrName) && !properties.contains(propertyOrName)) throw new IllegalArgumentException("Invalid property/name : " + propertyOrName);

    return isValidRuleInDirectionHorizontalOrVertical(board, name, operator, propertyOrName);
  }

  /**
   * Validates if a given rule combination is valid.
   *
   * @param leftOperand  the left operand of the rule.
   * @param operator     the operator of the rule.
   * @param rightOperand the right operand of the rule.
   * @return true if the rule combination is valid, false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   */
  public boolean isValidRuleCombination(Square leftOperand, Square operator, Square rightOperand) {
    Objects.requireNonNull(leftOperand);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(rightOperand);
    return (leftOperand.isName() && operator.isOperator() && (rightOperand.isProperty() || rightOperand.isName()));
  }

  /**
   * Checks if a matching rule exists at the specified position and direction.
   *
   * @param board    the game board to check against.
   * @param row      the row to check.
   * @param col      the column to check.
   * @param name     the name to match.
   * @param operator the operator to match.
   * @param property the property to match.
   * @param direction the direction to check.
   * @return true if a matching rule exists, false otherwise.
   * @throws NullPointerException     if any of the parameters are null.
   * @throws IllegalArgumentException if row or col are out of bounds.
   */
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

  /**
   * Validates if a rule is valid in either horizontal or vertical direction.
   *
   * @param board    the game board to check against.
   * @param name     the name to validate.
   * @param operator the operator to validate.
   * @param property the property to validate.
   * @return true if the rule is valid in either direction, false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   */
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

  /**
   * Checks if a given name is traversable on the game board.
   *
   * @param board the game board to check against.
   * @param name  the name to check.
   * @return true if the name is traversable, false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   * @throws IllegalArgumentException if the name is invalid.
   */
  public boolean isTraversable(GameBoard board, String name) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(name);
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    return !isValidRule(board, name, "IS", "PUSH") && !isValidRule(board, name, "IS", "STOP");
  }

  /**
   * Finds names that need to be transformed based on game rules.
   *
   * @param board the game board to check against.
   * @return an array containing the names to be transformed, or null if none.
   */
  public String[] namesToTransform(GameBoard board) {
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
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

  /**
   * Checks if a given name is "MELT" based on game rules.
   *
   * @param board the game board to check against.
   * @param name  the name to check.
   * @return true if the name is "MELT", false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   * @throws IllegalArgumentException if the name is invalid.
   */
  public boolean isMelt(GameBoard board, String name) {
    Objects.requireNonNull(board);
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    return isValidRule(board, name, "IS", "MELT");
  }

  /**
   * Checks if a given name is "HOT" based on game rules.
   *
   * @param board the game board to check against.
   * @param name  the name to check.
   * @return true if the name is "HOT", false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   * @throws IllegalArgumentException if the name is invalid.
   */
  public boolean isHot(GameBoard board, String name) {
    Objects.requireNonNull(board);
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    return isValidRule(board, name, "IS", "HOT");
  }


  /**
   * Checks if a given name is "SINK" based on game rules.
   *
   * @param board the game board to check against.
   * @param name  the name to check.
   * @return true if the name is "SINK", false otherwise.
   * @throws NullPointerException if any of the parameters are null.
   * @throws IllegalArgumentException if the name is invalid.
   */
  public boolean isSink(GameBoard board, String name) {
    Objects.requireNonNull(board);
    var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
    return isValidRule(board, name, "IS", "SINK");
  }

  /**
   * Gets the type of player present on the game board based on game rules.
   *
   * @param board the game board to check against.
   * @return the type of player present, or an empty string if none.
   * @throws NullPointerException if the board is null.
   */
  public String typeOfPlayerPresent(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    return players.stream().filter(p -> isValidRule(board, p, "IS", "YOU")).findFirst().orElse("");
  }

  /**
   * Checks if a player is present on the game board based on game rules.
   *
   * @param board the game board to check against.
   * @return true if a player is present, false otherwise.
   * @throws NullPointerException if the board is null.
   */
  public boolean playerIsPresent(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    return players.stream().anyMatch(p -> isValidRule(board, p, "IS", "YOU")) && !board.typeofSquare(typeOfPlayerPresent(board)).isEmpty();
  }

  /**
   * Checks if the player has lost based on game rules.
   *
   * @param board the game board to check against.
   * @return true if the player has lost, false otherwise.
   * @throws NullPointerException if the board is null.
   */
  public boolean playerHasLost(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    for(var name : players) {
      if((isValidRule(board, name, "IS", "DEFEAT") || (isValidRule(board, name, "IS", "SINK"))) && board.typeofSquare(name).stream().anyMatch(board::isPlayerOn))
        return true;
      else if(isValidRule(board, typeOfPlayerPresent(board), "IS", "MELT") && isValidRule(board, name,"IS", "HOT") && board.typeofSquare(name).stream().anyMatch(board::isPlayerOn))
        return true;
    }
    return false;
  }

  /**
   * Checks if the player has won based on game rules.
   *
   * @param board the game board to check against.
   * @return true if the player has won, false otherwise.
   * @throws NullPointerException if the board is null.
   */
  public boolean playerIsWin(GameBoard board) {
    Objects.requireNonNull(board);
    var players = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
    for(var name : players) {
      if((isValidRule(board, name, "IS", "WIN")) && board.typeofSquare(name).stream().anyMatch(board::isPlayerOn))
        return true;
    }
    return false;
  }
}

package fr.esiee.babaisyou.model;

/**
 * Represents a Square on the game board. This interface provides various methods to
 * determine the characteristics and properties of a Square.
 */
public sealed interface Square permits Name, Operator, Property, Object {

  /**
   * Retrieves the x-coordinate of the Square.
   *
   * @return the x-coordinate.
   */
  int x();

  /**
   * Retrieves the y-coordinate of the Square.
   *
   * @return the y-coordinate.
   */
  int y();

  /**
   * Checks if the Square is of type Name.
   *
   * @return true if the Square is of type Name, false otherwise.
   */
  boolean isName();

  /**
   * Checks if the Square is of type Operator.
   *
   * @return true if the Square is of type Operator, false otherwise.
   */
  boolean isOperator();

  /**
   * Checks if the Square is of type Property.
   *
   * @return true if the Square is of type Property, false otherwise.
   */
  boolean isProperty();

  /**
   * Checks if the Square is of type Object.
   *
   * @return true if the Square is of type Object, false otherwise.
   */
  boolean isObject();

  /**
   * Checks if the Square is empty.
   *
   * @return true if the Square is empty, false otherwise.
   */
  boolean isEmpty();

  /**
   * Checks if the Square can be pushed.
   *
   * @param board the game board to check against.
   * @return true if the Square can be pushed, false otherwise.
   */
  boolean isPushable(GameBoard board);

  /**
   * Checks if the Square can be traversed.
   *
   * @param board the game board to check against.
   * @return true if the Square can be traversed, false otherwise.
   */
  boolean isTraversable(GameBoard board);

  /**
   * Retrieves the representation of the Square.
   *
   * @return a string representing the Square.
   */
  String representation();

  /**
   * Retrieves the name of the Square.
   *
   * @return the name of the Square.
   */
  String name();
}

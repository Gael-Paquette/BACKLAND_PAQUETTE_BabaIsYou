package fr.esiee.babaisyou.controler;

/**
 * Enumeration representing the different possible actions in the "Baba Is You" game controller.
 * <p>
 * The possible values are:
 * <ul>
 *   <li>{@link #EXIT} : Exit the game</li>
 *   <li>{@link #LEFT} : Move left</li>
 *   <li>{@link #RIGHT} : Move right</li>
 *   <li>{@link #UP} : Move up</li>
 *   <li>{@link #DOWN} : Move down</li>
 *   <li>{@link #AVOID} : Avoid an action or obstacle</li>
 * </ul>
 * </p>
 */
public enum Code {
  /**
   * Action to exit the game.
   */
  EXIT,

  /**
   * Action to move left.
   */
  LEFT,

  /**
   * Action to move right.
   */
  RIGHT,

  /**
   * Action to move up.
   */
  UP,

  /**
   * Action to move down.
   */
  DOWN,

  /**
   * Action to avoid an action or obstacle.
   */
  AVOID
}

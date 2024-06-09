package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;

import java.util.Objects;

/**
 * The {@code EventGame} class handles different types of events in the "Baba Is You" game.
 * It processes keyboard and pointer events to determine the appropriate action to take.
 */
public class EventGame {

  /**
   * Constructs an {@code EventGame} instance.
   */
  public EventGame() {}

  /**
   * Manages keyboard events and translates them into game actions.
   *
   * @param event the keyboard event to manage
   * @return the corresponding game action code
   * @throws NullPointerException if the event is null
   */
  private Code manageKeyboardEvent(KeyboardEvent event) {
    Objects.requireNonNull(event);
    return switch (event.action()) {
      case KEY_PRESSED -> switch (event.key()) {
        case KeyboardEvent.Key.Q -> Code.EXIT;
        case KeyboardEvent.Key.UP -> Code.UP;
        case KeyboardEvent.Key.DOWN -> Code.DOWN;
        case KeyboardEvent.Key.LEFT -> Code.LEFT;
        case KeyboardEvent.Key.RIGHT -> Code.RIGHT;
        default -> Code.AVOID;
      };
      case KEY_RELEASED -> Code.AVOID;
    };
  }

  /**
   * Manages pointer events and translates them into game actions.
   * Currently, all pointer events result in an {@code AVOID} action.
   *
   * @param event the pointer event to manage
   * @return the corresponding game action code
   * @throws NullPointerException if the event is null
   */
  private Code managePointerEvent(PointerEvent event) {
    Objects.requireNonNull(event);
    return Code.AVOID;
  }

  /**
   * Manages general events and delegates them to the appropriate handler based on the event type.
   *
   * @param event the event to manage
   * @return the corresponding game action code
   * @throws NullPointerException if the event is null
   */
  public Code manageEvent(Event event) {
    Objects.requireNonNull(event);
    return switch (event) {
      case KeyboardEvent keyboardEvent -> manageKeyboardEvent(keyboardEvent);
      case PointerEvent pointerEvent -> managePointerEvent(pointerEvent);
    };
  }
}

package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Event;
import com.github.forax.zen.KeyboardEvent;
import com.github.forax.zen.PointerEvent;
import fr.esiee.babaisyou.model.GameBoard;

import java.util.Objects;

public class EventGame {
  public EventGame() {}

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

  private Code managePointerEvent(PointerEvent event) {
    Objects.requireNonNull(event);
    return Code.AVOID;
  }

  public Code manageEvent(Event event) {
    Objects.requireNonNull(event);
    return switch (event) {
      case KeyboardEvent keyboardEvent -> manageKeyboardEvent(keyboardEvent);
      case PointerEvent pointerEvent -> managePointerEvent(pointerEvent);
    };
  }
}

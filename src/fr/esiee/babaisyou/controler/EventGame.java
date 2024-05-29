package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Event;
import fr.esiee.babaisyou.model.GameBoard;

import java.util.Objects;

public class EventGame {
  public EventGame() {}

  public void manageEvent(Event event, GameBoard board) {
    Objects.requireNonNull(event);
    Objects.requireNonNull(board);
  }
}

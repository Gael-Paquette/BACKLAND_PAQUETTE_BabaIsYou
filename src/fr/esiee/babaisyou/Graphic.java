package fr.esiee.babaisyou;

import java.awt.Color;

import com.github.forax.zen.Application;

public class Graphic {

  public static void createWindow() {
    Application.run(Color.BLACK, context -> {
      var screenInfo = context.getScreenInfo();
      var width = screenInfo.width();
      var height = screenInfo.height();
      // get the current event or wait 100 milliseconds
      var event = context.pollOrWaitEvent(100);
    });
  }
}

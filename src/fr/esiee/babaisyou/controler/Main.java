package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import java.awt.*;

/**
 * The {@code Main} class contains the entry point for running the "Baba Is You" game.
 */
public class Main {

    /**
     * The main method initializes the game components and starts the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var rule = Game.newRule();
        var imagesLoader = Game.newImagesLoader();
        var imagesEndLoader = Game.newImagesEndLoader();

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();

            Game.playGame(context, imagesEndLoader, imagesLoader, rule, width, height);
            System.exit(0);
        });
    }

}

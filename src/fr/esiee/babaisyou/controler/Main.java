package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import java.awt.*;

public class Main {
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

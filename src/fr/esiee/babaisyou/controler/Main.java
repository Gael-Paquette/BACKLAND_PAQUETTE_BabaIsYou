package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Name;
import fr.esiee.babaisyou.model.Operator;
import fr.esiee.babaisyou.model.Property;
import fr.esiee.babaisyou.view.Graphic;
import fr.esiee.babaisyou.view.ImagesLoader;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int enter;
        System.out.println("Bienvenue sur ce projet");
        Scanner sc = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard(10,10);
        gameBoard.displayBoard();
        System.out.println("rocks : " + gameBoard.countElementToPush(5, 3, "right"));
        gameBoard.setSquare(0, 0, new Name(0, 0, "BABA"));
        gameBoard.setSquare(0, 1, new Operator(0, 1, "IS"));
        gameBoard.setSquare(0, 2, new Property(0, 2, "YOU"));
        gameBoard.displayBoard();
        var imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );
        /*
        var view = new Graphic();
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            Graphic.draw(context, gameBoard, imagesLoader, view, width, height);
        });*/

        do {
            enter = Integer.parseInt(sc.nextLine());
            if(enter == 1)
                gameBoard.movePlayer("left");
            else if(enter == 2)
                gameBoard.movePlayer("right");
            else if(enter == 3)
                gameBoard.movePlayer("up");
            else if(enter == 4)
                gameBoard.movePlayer("down");
            gameBoard.displayBoard();
        } while(gameBoard.getSquareFlag() != null);

        System.out.println("Victory !");
    }

}
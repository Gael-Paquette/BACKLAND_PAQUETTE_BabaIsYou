package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Object;
import fr.esiee.babaisyou.model.Name;
import fr.esiee.babaisyou.model.Operator;
import fr.esiee.babaisyou.model.Property;
import fr.esiee.babaisyou.model.Rule;
import fr.esiee.babaisyou.view.Graphic;
import fr.esiee.babaisyou.view.ImagesLoader;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int direction;
        Scanner sc = new Scanner(System.in);
        Rule rule = new Rule();

        GameBoard gameBoard = new GameBoard(10,10);
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );

        // System.out.println(imagesLoader.getImagesObject());
        // System.out.println(imagesLoader.getImagesText());

        gameBoard.updateSquare(1, 1, new Name(1, 1, "BABA"));
        gameBoard.updateSquare(1, 2, new Operator(1, 2, "IS"));
        gameBoard.updateSquare(1, 3, new Property(1, 3, "YOU"));
        gameBoard.updateSquare(0,0, new Object(0, 0, "BABA"));
        gameBoard.updateSquare(0,9, new Object(0, 9, "FLAG"));
        gameBoard.updateSquare(5, 3, new Object(5, 3, "ROCK"));
        gameBoard.updateSquare(5, 4, new Object(5, 4, "ROCK"));
        gameBoard.updateSquare(5, 5, new Object(5, 5, "ROCK"));
        gameBoard.updateSquare(8, 3, new Name(8, 3, "ROCK"));
        gameBoard.updateSquare(8, 4, new Operator(8, 4, "IS"));
        gameBoard.updateSquare(8, 5, new Property(8, 5, "PUSH"));
        gameBoard.displayBoard();

        /*
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            Graphic.draw(context, gameBoard, imagesLoader, width, height);
        });
        */

        do {
            System.out.println("Enter the direction : ");
            System.out.println("1 : LEFT");
            System.out.println("2 : RIGHT");
            System.out.println("3 : UP");
            System.out.println("4 : DOWN");
            direction = Integer.parseInt(sc.nextLine());

            switch(direction) {
                case 1:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "LEFT"))
                        gameBoard.push("LEFT");
                    gameBoard.movePlayer("LEFT");
                    break;
                case 2:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "RIGHT"))
                        gameBoard.push("RIGHT");
                    gameBoard.movePlayer("RIGHT");
                    break;
                case 3:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "UP"))
                        gameBoard.push("UP");
                    gameBoard.movePlayer("UP");
                    break;
                case 4:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "DOWN"))
                        gameBoard.push("DOWN");
                    gameBoard.movePlayer("DOWN");
                    break;
            }
            gameBoard.displayBoard();
        } while(gameBoard.getSquareFlag() != null && gameBoard.playerIsPresent());

        if(!gameBoard.playerIsPresent())
            System.out.println("Defeat !");
        else
            System.out.println("Victory !");
    }

}

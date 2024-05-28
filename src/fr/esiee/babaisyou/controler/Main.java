package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Name;
import fr.esiee.babaisyou.model.Operator;
import fr.esiee.babaisyou.model.Property;
import fr.esiee.babaisyou.view.Graphic;
import fr.esiee.babaisyou.view.ImagesLoader;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int direction;
        Scanner sc = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard(Paths.get("levels/level0.txt"));
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );
        System.out.println(imagesLoader.getImagesObject());
        System.out.println(imagesLoader.getImagesText());
        gameBoard.displayBoard();
        gameBoard.updateSquare(1, 1, new Name(1, 1, "BABA"));
        gameBoard.updateSquare(1, 2, new Operator(1, 2, "IS"));
        gameBoard.updateSquare(1, 3, new Property(1, 3, "YOU"));
        gameBoard.displayBoard();


        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            Graphic.draw(context, gameBoard, imagesLoader, width, height);
        });

        do {
            System.out.println("Enter the direction : ");
            System.out.println("1 : left");
            System.out.println("2 : right");
            System.out.println("3 : up");
            System.out.println("4 : down");
            direction = Integer.parseInt(sc.nextLine());

            switch(direction) {
                case 1:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "left"))
                        gameBoard.push("left");
                    gameBoard.movePlayer("left");
                    break;
                case 2:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "right"))
                        gameBoard.push("right");
                    gameBoard.movePlayer("right");
                    break;
                case 3:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "up"))
                        gameBoard.push("up");
                    gameBoard.movePlayer("up");
                    break;
                case 4:
                    if(gameBoard.facingABlock(gameBoard.getSquarePlayer(), "down"))
                        gameBoard.push("down");
                    gameBoard.movePlayer("down");
                    break;
            }
            gameBoard.displayBoard();
        } while(gameBoard.getSquareFlag() != null);

        System.out.println("Victory !");
    }

}

package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Object;
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

        GameBoard board = new GameBoard(13,11);
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );

        // System.out.println(imagesLoader.getImagesObject());
        // System.out.println(imagesLoader.getImagesText());

        board.updateSquare(1, 1, new Name(1, 1, "BABA"));
        board.updateSquare(1, 2, new Operator(1, 2, "IS"));
        board.updateSquare(1, 3, new Property(1, 3, "YOU"));
        board.updateSquare(1, 7, new Name(1, 7, "FLAG"));
        board.updateSquare(1, 8, new Operator(1, 8, "IS"));
        board.updateSquare(1, 9, new Property(1, 9, "WIN"));
        board.updateSquare(7, 1, new Name(7, 1, "WALL"));
        board.updateSquare(7, 2, new Operator(7, 2, "IS"));
        board.updateSquare(7, 3, new Property(7, 3, "STOP"));
        board.updateSquare(7, 7, new Name(7, 7, "ROCK"));
        board.updateSquare(7, 8, new Operator(7, 8, "IS"));
        board.updateSquare(7, 9, new Property(7, 9, "PUSH"));
        board.updateSquare(5,1, new Object(5, 1, "BABA"));
        board.updateSquare(5,9, new Object(5, 9, "FLAG"));
        board.updateSquare(5, 5, new Object(5, 5, "ROCK"));
        board.updateSquare(6, 5, new Object(6, 5, "ROCK"));
        board.updateSquare(7, 5, new Object(7, 5, "ROCK"));
        board.updateSquare(3, 1, new Object(3, 1, "WALL"));
        board.updateSquare(3, 2, new Object(3, 2, "WALL"));
        board.updateSquare(3, 3, new Object(3, 3, "WALL"));
        board.updateSquare(3, 4, new Object(3, 4, "WALL"));
        board.updateSquare(3, 5, new Object(3, 5, "WALL"));
        board.updateSquare(3, 6, new Object(3, 6, "WALL"));
        board.updateSquare(3, 7, new Object(3, 7, "WALL"));
        board.updateSquare(3, 8, new Object(3, 8, "WALL"));
        board.updateSquare(3, 9, new Object(3, 9, "WALL"));
        board.updateSquare(9, 1, new Object(9, 1, "WALL"));
        board.updateSquare(9, 2, new Object(9, 2, "WALL"));
        board.updateSquare(9, 3, new Object(9, 3, "WALL"));
        board.updateSquare(9, 4, new Object(9, 4, "WALL"));
        board.updateSquare(9, 5, new Object(9, 5, "WALL"));
        board.updateSquare(9, 6, new Object(9, 6, "WALL"));
        board.updateSquare(9, 7, new Object(9, 7, "WALL"));
        board.updateSquare(9, 8, new Object(9, 8, "WALL"));
        board.updateSquare(9, 9, new Object(9, 9, "WALL"));

        board.displayBoard();


        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            Graphic.draw(context, gameBoard, imagesLoader, width, height);
        });

        do {
            System.out.println("Enter the direction : ");
            System.out.println("1 : LEFT");
            System.out.println("2 : RIGHT");
            System.out.println("3 : UP");
            System.out.println("4 : DOWN");
            direction = Integer.parseInt(sc.nextLine());

            switch(direction) {
                case 1:
                    if(board.facingABlock(board.getSquarePlayer(), "LEFT"))
                        board.push("LEFT");
                    board.movePlayer("LEFT");
                    break;
                case 2:
                    if(board.facingABlock(board.getSquarePlayer(), "RIGHT"))
                        board.push("RIGHT");
                    board.movePlayer("RIGHT");
                    break;
                case 3:
                    if(board.facingABlock(board.getSquarePlayer(), "UP"))
                        board.push("UP");
                    board.movePlayer("UP");
                    break;
                case 4:
                    if(board.facingABlock(board.getSquarePlayer(), "DOWN"))
                        board.push("DOWN");
                    board.movePlayer("DOWN");
                    break;
            }
            board.displayBoard();
        } while(!board.win() && board.playerIsPresent());

        if(!board.playerIsPresent())
            System.out.println("Defeat !");
        else
            System.out.println("Victory !");
    }

}

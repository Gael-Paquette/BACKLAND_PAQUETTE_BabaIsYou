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

        GameBoard board = new GameBoard(Paths.get("levels/level6.txt"));
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );
        board.displayBoard();

        /*
        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            Graphic.draw(context, gameBoard, imagesLoader, width, height);
        });*/

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

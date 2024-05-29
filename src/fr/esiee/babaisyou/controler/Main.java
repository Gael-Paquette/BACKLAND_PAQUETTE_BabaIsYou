package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.Direction;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.view.DrawGame;
import fr.esiee.babaisyou.view.ImagesLoader;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        int direction;
        Scanner sc = new Scanner(System.in);

        GameBoard board = new GameBoard(Paths.get("levels/level0.txt"));
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );
        board.displayBoard();


        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();

            var drawGame = new DrawGame(0, 0, width, height, board, imagesLoader);
            DrawGame.draw(context, board, drawGame);
            var event = context.pollOrWaitEvent(10000);

            System.exit(0);
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
                    if(board.facingABlock(board.getSquarePlayer(), Direction.LEFT))
                        board.push(Direction.LEFT);
                    board.movePlayer(Direction.LEFT);
                    break;
                case 2:
                    if(board.facingABlock(board.getSquarePlayer(), Direction.RIGHT))
                        board.push(Direction.RIGHT);
                    board.movePlayer(Direction.RIGHT);
                    break;
                case 3:
                    if(board.facingABlock(board.getSquarePlayer(), Direction.UP))
                        board.push(Direction.UP);
                    board.movePlayer(Direction.UP);
                    break;
                case 4:
                    if(board.facingABlock(board.getSquarePlayer(), Direction.DOWN))
                        board.push(Direction.DOWN);
                    board.movePlayer(Direction.DOWN);
                    break;
            }
            board.displayBoard();
        } while(!board.win() && board.playerIsPresent());

        if(!board.playerIsPresent()) {
            System.out.println("Defeat !");
            System.exit(0);
        } else {
            System.out.println("Victory !");
            System.exit(0);
        }

    }

}

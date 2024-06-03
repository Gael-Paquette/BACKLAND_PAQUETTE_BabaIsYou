package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.Direction;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Rule;
import fr.esiee.babaisyou.view.DrawGame;
import fr.esiee.babaisyou.view.ImagesLoader;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Rule rule = new Rule();
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

            do {
                var drawGame = new DrawGame(0, 0, width, height, board, imagesLoader);
                DrawGame.draw(context, board, drawGame);
                board.displayBoard();
                var event = context.pollOrWaitEvent(100);
                if (event != null) {
                    var eventGame = new EventGame();
                    var code = eventGame.manageEvent(event);
                    switch (code) {
                        case EXIT -> System.exit(0);
                        case UP -> {
                            if(board.facingABlock(board.getSquarePlayer(), Direction.UP))
                                board.push(Direction.UP);
                            board.movePlayer(Direction.UP);
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);

                        }
                        case DOWN -> {
                            if(board.facingABlock(board.getSquarePlayer(), Direction.DOWN))
                                board.push(Direction.DOWN);
                            board.movePlayer(Direction.DOWN);
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case LEFT -> {
                            if(board.facingABlock(board.getSquarePlayer(), Direction.LEFT))
                                board.push(Direction.LEFT);
                            board.movePlayer(Direction.LEFT);
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case RIGHT -> {
                            if(board.facingABlock(board.getSquarePlayer(), Direction.RIGHT))
                                board.push(Direction.RIGHT);
                            board.movePlayer(Direction.RIGHT);
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case AVOID -> {}
                    }
                }
            } while(rule.playerIsPresent(board) && !rule.playerIsWin(board));

            if(!rule.playerIsPresent(board)) {
                System.out.println("Defeat !");
                System.exit(0);
            } else {
                System.out.println("Victory !");
                System.exit(0);
            }
        });
    }

}

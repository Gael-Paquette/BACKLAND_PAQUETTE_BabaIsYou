package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.Direction;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Rule;
import fr.esiee.babaisyou.view.*;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Rule rule = new Rule();
        ImagesLoader imagesLoader = new ImagesLoader(
                List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER"),
                List.of("IS", "ON", "HAS", "AND"),
                List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK")
        );
        ImagesEndLoader imagesEndLoader = new ImagesEndLoader(
                List.of("CONGRATULATIONS", "DEFEAT")
        );

        Application.run(Color.BLACK, context -> {
            var screenInfo = context.getScreenInfo();
            var width = screenInfo.width();
            var height = screenInfo.height();
            var level = 0;

            do {
                try {
                    var clearWidow = new ClearWindow(0, 0, width, height);
                    var drawEnd = new DrawEnd(0, 0, width, height, imagesEndLoader);
                    ClearWindow.clearWindow(context, clearWidow);
                    GameBoard board = new GameBoard(Paths.get("levels/level" + level + ".txt"));
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

                    if (!rule.playerIsWin(board)) {
                        System.out.println("Defeat ! ");
                        DrawEnd.draw(context, drawEnd, "DEFEAT");
                        System.exit(0);
                    } else if (!rule.playerIsPresent(board)) {
                        System.out.println("Defeat !");
                        DrawEnd.draw(context, drawEnd, "DEFEAT");
                        System.exit(0);
                    } else {
                        System.out.println("Victory !");
                        DrawEnd.draw(context, drawEnd, "CONGRATULATIONS");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
              level++;
            } while (level < 7);
            System.exit(0);
        });
    }

}

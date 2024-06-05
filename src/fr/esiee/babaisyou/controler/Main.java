package fr.esiee.babaisyou.controler;

import com.github.forax.zen.Application;
import fr.esiee.babaisyou.model.Direction;
import fr.esiee.babaisyou.model.GameBoard;
import fr.esiee.babaisyou.model.Rule;
import fr.esiee.babaisyou.model.Square;
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
                List<Square> player = board.getSquaresPlayer();
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
                            for (Square square : player) {
                                if(board.facingABlock(square, Direction.UP))
                                    board.push(Direction.UP);
                                board.movePlayer(square, Direction.UP);
                            }
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case DOWN -> {
                            for (Square square : player) {
                                if(board.facingABlock(square, Direction.DOWN))
                                    board.push(Direction.DOWN);
                                board.movePlayer(square, Direction.DOWN);
                            }
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case LEFT -> {
                            for (Square square : player) {
                                if(board.facingABlock(square, Direction.LEFT))
                                    board.push(Direction.LEFT);
                                board.movePlayer(square, Direction.LEFT);
                            }
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case RIGHT -> {
                            for (Square square : player) {
                                if(board.facingABlock(square, Direction.RIGHT))
                                    board.push(Direction.RIGHT);
                                board.movePlayer(square, Direction.RIGHT);
                            }
                            if(rule.namesToTransform(board) != null)
                                board.transformSquare(rule.namesToTransform(board)[0], rule.namesToTransform(board)[1]);
                        }
                        case AVOID -> {}
                    }
                }
            } while(rule.playerIsPresent(board) && !rule.playerIsWin(board) && !rule.playerHasLost(board));

            if(!rule.playerIsPresent(board) || rule.playerHasLost(board)) {
                System.out.println("Defeat !");
                System.exit(0);
            } else {
                System.out.println("Victory !");
                System.exit(0);
            }
        });
    }

}

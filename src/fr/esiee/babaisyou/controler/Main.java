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
        int direction;
        Scanner sc = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard(10,10);
        gameBoard.displayBoard();

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

package fr.esiee.babaisyou;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int enter;
        System.out.println("Bienvenue sur ce projet");
        Scanner sc = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard(10,10);
        gameBoard.displayBoard();
        System.out.println("rocks : " + gameBoard.countElementToPush(5, 3, "right"));

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

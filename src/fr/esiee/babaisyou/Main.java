package fr.esiee.babaisyou;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int enter;
        System.out.println("Bienvenue sur ce projet");
        Scanner sc = new Scanner(System.in);

        GameBoard gameBoard = new GameBoard(10,10);
        gameBoard.setElement(0,0, new Player());
        gameBoard.setElement(0,9, new Flag());
        gameBoard.displayBoard();

        // TESTS
        String element1 = gameBoard.getElement(0,0).representation();
        System.out.println("Element (0,0) : " + element1);
        String element2 = gameBoard.getElement(0,5).representation();
        System.out.println("Element (0,5) : " + element2);
        String element3 = gameBoard.getElement(0,9).representation();
        System.out.println("Element (0,9) : " + element3);

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

        System.out.println("Victoire !");
    }

}

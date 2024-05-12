package fr.esiee.babaisyou;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenue sur ce projet");

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
    }

}

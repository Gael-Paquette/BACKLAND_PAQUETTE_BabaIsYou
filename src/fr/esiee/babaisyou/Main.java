package fr.esiee.babaisyou;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenue sur ce projet");

        GameBoard g1 = new GameBoard(6,6);
        g1.displayBoard();
        System.out.println();
        g1.swap(0,0,2,2);
        g1.displayBoard();
        System.out.println();
        g1.swap(0,0, 2,5);
        g1.displayBoard();
        System.out.println();
        g1.swap(2,5, 1,1);
        g1.displayBoard();

    }

}

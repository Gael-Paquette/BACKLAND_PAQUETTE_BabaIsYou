package fr.esiee.babaisyou;

public class GameBoard {

    private final int rows;
    private final int cols;
    private final Square [][] board;

    public GameBoard(int rows, int cols) {
        if(rows < 1 || cols < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.cols = cols;
        this.board = new Square[rows][cols];
        initializeBoard();
    }

    private void initializeBoard() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }

    public Square getSquare(int x, int y) {
        return this.board[x][y];
    }

    public void setSquare(int x, int y, Square square) {
        this.board[x][y] = square;
    }

    public void displayBoard() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                System.out.print("(" + board[i][j].x() + ", " + board[i][j].y() + ") ");
            }
            System.out.println();
        }
    }

    public void swap(int x1, int y1, int x2, int y2) {
        Square tmp = getSquare(x1,y1);
        setSquare(x1, y1, getSquare(x2,y2));
        setSquare(x2, y2, tmp);
    }

}

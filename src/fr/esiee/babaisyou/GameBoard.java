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

    public int rows() {
        return this.rows;
    }

    public int cols() {
        return this.cols;
    }

    private void initializeBoard() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                board[i][j] = new Square(i, j);
            }
        }
    }

    public void swapCells(Square s1, Square s2) {
        Square tmp = board[s1.x()][s1.y()];
        board[s1.x()][s1.y()] = board[s2.x()][s2.y()];
        board[s2.x()][s2.y()] = tmp;
    }

}

package fr.esiee.babaisyou;

import java.util.Objects;

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
                board[i][j] = new Object(i, j, "null");
            }
        }
        setSquare(0,0, new Object(0,0, "Baba"));
        setSquare(0,9, new Object(0, 9, "Flag"));
        setSquare(5, 3, new Object(5, 3, "Rock"));
        // setSquare(5, 4, new Element(5, 4, "Rock"));
        // (5, 5, new Element(5, 5, "Rock"));
        // setSquare(5, 6, new Element(5, 6, "Rock"));
    }

    public int getRows() { return this.rows; }

    public int getCols() { return this.cols; }

    public boolean inTheBoard(int x, int y) { return (x >= 0 && x < rows) && (y >= 0 && y < cols); }

    public Square getSquare(int x, int y) {
        if(inTheBoard(x, y))
            return this.board[x][y];
        return null;
    }

    public void setSquare(int x, int y, Square square) {
        Objects.requireNonNull(square);
        if(inTheBoard(x, y)) {
            this.board[x][y] = square;
        }
    }

    public Square getSquarePlayer() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                if(this.getSquare(i,j).representation().equals("X"))
                    return this.board[i][j];
            }
        }
        return null;
    }

    public Square getSquareFlag() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                if(this.getSquare(i, j).representation().equals("F"))
                    return this.board[i][j];
            }
        }
        return null;
    }

    public int countElementToPush(int x, int y, String direction) {
        int count = 0;
        if(inTheBoard(x, y)) {
            Square square = getSquare(x, y);
            Objects.requireNonNull(direction);
            if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down")) {
                throw new IllegalArgumentException("Invalid direction " + direction);
            }
            while (square != null && square.representation().equals("*")) {
                count++;
                square = getSquareDirection(direction, square.x(), square.y());
            }
        }
        return count;
    }

    private Square getSquareDirection(String direction, int x, int y) {
        Objects.requireNonNull(direction);
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down")) {
            throw new IllegalArgumentException("Invalid direction " + direction);
        }
        return switch (direction) {
            case "left" -> getSquare(x, y-1);
            case "right" -> getSquare(x, y+1);
            case "up" -> getSquare(x-1, y);
            case "down" -> getSquare(x+1, y);
            default -> null;
        };
    }

    public void movePlayer(String direction) {
        Square s;
        Objects.requireNonNull(direction);
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down")) {
            throw new IllegalArgumentException("Invalid direction " + direction);
        }
        Square player = getSquarePlayer();
        System.out.println(player.x() + "," + player.y());
        s = getSquareDirection(direction, player.x(), player.y());
        if(s != null && s.isElement()) {
            Square newPlayer = new Object(s.x(), s.y(), player.name());
            // Square newEmpty = new Element(player.x(), player.y(), s.name());
            Square newEmpty = new Object(player.x(), player.y(), "null");
            setSquare(s.x(), s.y(), newPlayer);
            setSquare(player.x(), player.y(), newEmpty);
        }
    }

    public void displayBoard() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                System.out.print("[" + getSquare(i,j).representation() + "]");
            }
            System.out.println();
        }
    }

}
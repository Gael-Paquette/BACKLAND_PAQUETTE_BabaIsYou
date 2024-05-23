package fr.esiee.babaisyou.model;

import java.awt.*;
import java.util.Objects;

public class GameBoard {

    private final int rows;
    private final int cols;
    private final Square[][] board;

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
                board[i][j] = new Object(i, j, "NULL");
            }
        }
    }

    public int getRows() { return this.rows; }

    public int getCols() { return this.cols; }

    public Square getSquare(int x, int y) {
        if (x < 0 || x > rows || y < 0 || y > cols) {
            throw new IllegalArgumentException("x or y out of bounds");
        }
        return this.board[x][y];
    }

    public void setSquare(int x, int y, Square square) {
        Objects.requireNonNull(square);
        if (x < 0 || x > rows || y < 0 || y > cols) {
            throw new IllegalArgumentException("x or y out of bounds");
        }
        this.board[x][y] = square;
    }

    public Square getSquarePlayer() {
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                if(this.getSquare(i, j).representation().equals("X"))
                    return this.board[i][j];
            }
        }
        return null;
    }

    public Square getSquareFlag() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.getSquare(i, j).representation().equals("F"))
                    return this.board[i][j];
            }
        }
        return null;
    }

    public int countElementToPush(int x, int y, String direction) {
        Objects.requireNonNull(direction);
        if (x < 0 || x > rows || y < 0 || y > cols) {
            throw new IllegalArgumentException("x or y out of bounds");
        }
        int count = 0;
        Square square = getSquare(x, y);
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down")) {
            throw new IllegalArgumentException("Invalid direction " + direction);
        }
        while (square != null && square.representation().equals("*")) {
            count++;
            square = getSquareDirection(direction, square.getX(), square.getY());
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
        Objects.requireNonNull(direction);
        Square s;
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down")) {
            throw new IllegalArgumentException("Invalid direction " + direction);
        }
        Square player = getSquarePlayer();
        System.out.println(player.getX() + "," + player.getY());
        s = getSquareDirection(direction, player.getX(), player.getY());
        if(s != null && s.isObject()) {
            Square newPlayer = new Object(s.getX(), s.getY(), player.name());
            Square newEmpty = new Object(player.getX(), player.getY(), "NULL");
            setSquare(s.getX(), s.getY(), newPlayer);
            setSquare(player.getX(), player.getY(), newEmpty);
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

    public void drawGameBoard(Graphics2D graphics2D, int widthWindow, int heightWindow) {
        Objects.requireNonNull(graphics2D);
        if (widthWindow < 0 || heightWindow < 0) {
            throw new IllegalArgumentException("width or height out of bounds");
        }
        var widthSquare = widthWindow / rows;
        var heightSquare = heightWindow / cols;
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < cols; j++) {
                var square = getSquare(i, j);
                //square.draw(graphics2D, (i * widthSquare) + (widthSquare / 2), (j * heightSquare) + (heightSquare / 2));
            }
        }
    }
}
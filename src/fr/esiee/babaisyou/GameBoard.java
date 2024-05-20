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
        setSquare(5, 4, new Object(5, 4, "Rock"));
        setSquare(5, 5, new Object(5, 5, "Rock"));
    }

    public int getRows() { return this.rows; }

    public int getCols() { return this.cols; }

    public Square getSquare(int x, int y) {
        if(inTheBoard(x, y))
            return this.board[x][y];
        return null;
    }

    public boolean inTheBoard(int x, int y) { return (x >= 0 && x < rows) && (y >= 0 && y < cols); }

    public boolean notInTheBoard(Square s) { return s == null; }

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

    public int countElementToPush(String direction) {
        Objects.requireNonNull(direction);
        int count = 0;
        Square player = getSquarePlayer();
        Square block;
        if (!notInTheBoard(player)) {
            block = getSquareDirection(player.x(), player.y(), direction);
            if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
                throw new IllegalArgumentException("Invalid direction " + direction);
            while (block != null && block.representation().equals("*")) {
                count++;
                block = getSquareDirection(block.x(), block.y(), direction);
            }
        }
        return count;
    }


    public boolean facingABlock(Square player, String direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        if(notInTheBoard(player)) return false;

        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
            throw new IllegalArgumentException("Invalid direction " + direction);

        if(!notInTheBoard(getSquareDirection(player.x(), player.y(), direction)))
            return getSquareDirection(player.x(), player.y(), direction).isObject() && getSquareDirection(player.x(), player.y(), direction).name().equals("Rock");

        return false;
    }

    public void push(String direction) {
        Objects.requireNonNull(direction);
        int countElementToPush = 0, i = 1;
        Square block;
        Square player = getSquarePlayer();
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
            throw new IllegalArgumentException("Invalid direction " + direction);
        if(notInTheBoard(player)) return;
        block = getSquareDirection(player.x(), player.y(), direction);
        countElementToPush = countElementToPush(direction);
        if(countElementToPush > 1) {
            while(i <= countElementToPush-1) {
                block = getSquareDirection(block.x(), block.y(), direction);
                i++;
            }
        }
        System.out.println("x :" + block.x() + ", y :" + block.y());
        if(notInTheBoard(block)) return;
        if(!notInTheBoard(getSquareDirection(block.x(), block.y(), direction))) {
            if(getSquareDirection(block.x(), block.y(), direction).isEmpty()) {
                setSquare(getSquareDirection(block.x(), block.y(), direction).x(), getSquareDirection(block.x(), block.y(), direction).y(), new Object(getSquareDirection(block.x(), block.y(), direction).x(), getSquareDirection(block.x(), block.y(), direction).y(), "Rock"));
                if(countElementToPush == 1)
                    setSquare(block.x(), block.y(), new Object(block.x(), block.y(), "null"));
                else
                    setSquare(getSquareDirection(player.x(), player.y(), direction).x(), getSquareDirection(player.x(), player.y(), direction).y(), new Object(getSquareDirection(player.x(), player.y(), direction).x(), getSquareDirection(player.x(), player.y(), direction).y(), "null"));
                }
        }
    }

    private Square getSquareDirection(int x, int y, String direction) {
        Objects.requireNonNull(direction);
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
            throw new IllegalArgumentException("Invalid direction " + direction);
        if(inTheBoard(x, y)) {
            return switch (direction) {
                case "left" -> getSquare(x, y - 1);
                case "right" -> getSquare(x, y + 1);
                case "up" -> getSquare(x - 1, y);
                case "down" -> getSquare(x + 1, y);
                default -> null;
            };
        }
        throw new IllegalArgumentException();
    }

    public void movePlayer(String direction) {
        Objects.requireNonNull(direction);
        Square player = getSquarePlayer();
        Square s  = getSquareDirection(player.x(), player.y(), direction);
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
            throw new IllegalArgumentException("Invalid direction " + direction);
        if(!notInTheBoard(s)) {
            if(s.isEmpty() || s.name().equals("Flag")) {
                setSquare(s.x(), s.y(), new Object(s.x(), s.y(), player.name()));
                setSquare(player.x(), player.y(), new Object(player.x(), player.y(), "null"));
            }
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
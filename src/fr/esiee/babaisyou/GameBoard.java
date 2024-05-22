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
        setSquare(8, 3, new Name(8, 3, "Rock"));
        setSquare(8, 4, new Operator(8, 4, "Is"));
        setSquare(8, 5, new Property(8, 5, "Push"));
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

    public boolean canMove(Square s, String direction) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square next = nextSquare(s.x(), s.y(), direction);
        return !notInTheBoard(next) && next.isEmpty();
    }

    public boolean isPushableRule() {
        Rule rule = new Rule();
        return rule.isValidRule(this, "Rock", "Is", "Push");
    }

    public void validDirection(String direction) {
        if (!direction.equals("left") && !direction.equals("right") && !direction.equals("up") && !direction.equals("down"))
            throw new IllegalArgumentException("Invalid direction " + direction);
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

    public int countElementToPush(String direction) {
        Objects.requireNonNull(direction);
        int count = 0;
        Square player = getSquarePlayer();
        Square block;
        if (!notInTheBoard(player)) {
            block = nextSquare(player.x(), player.y(), direction);
            validDirection(direction);
            while (block != null && ( (block.isObject() && !block.isEmpty()) || block.isName() || block.isOperator() || block.isProperty() )) {
                count++;
                block = nextSquare(block.x(), block.y(), direction);
            }
        }
        return count;
    }

    public boolean facingABlock(Square player, String direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        if (notInTheBoard(player)) return false;
        validDirection(direction);

        if (!notInTheBoard(nextSquare(player.x(), player.y(), direction)))
            return ( (nextSquare(player.x(), player.y(), direction).isObject() && !nextSquare(player.x(), player.y(), direction).name().equals("null")) || nextSquare(player.x(), player.y(), direction).isName() || nextSquare(player.x(), player.y(), direction).isOperator() || nextSquare(player.x(), player.y(), direction).isProperty());

        return false;
    }

    public void push(String direction) {
        Objects.requireNonNull(direction);
        int countElementToPush, i;
        Square block, current, currentNext, player = getSquarePlayer();
        validDirection(direction);

        if (notInTheBoard(player)) return;
        block = nextSquare(player.x(), player.y(), direction);

        countElementToPush = countElementToPush(direction);
        if (countElementToPush < 1) return;

        for (i = 1; i <= countElementToPush-1; i++) {
            block = nextSquare(block.x(), block.y(), direction);
            if (notInTheBoard(block)) return;
        }

        if(!canMove(block, direction)) return;
        boolean rule = isPushableRule();

        current = block;
        /* A CORRIGER - UN BLOC NE DOIT PAS ETRE POUSSE PAR UN OPERATEUR, UNE PROPRIETE OU UN NOM*/
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.x(), current.y(), direction);
            if(!rule && nextSquare(player.x(), player.y(), direction).representation().equals("*"))
                return;
            moveBlock(current, currentNext);
            current = nextSquareReverse(current.x(), current.y(), direction);
        }
    }

    public Square nextSquare(int x, int y, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
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

    public Square nextSquareReverse(int x, int y, String reverseDirection) {
        Objects.requireNonNull(reverseDirection);
        validDirection(reverseDirection);
        if(inTheBoard(x, y)) {
            return switch (reverseDirection) {
                case "left" -> getSquare(x, y + 1);
                case "right" -> getSquare(x, y - 1);
                case "up" -> getSquare(x + 1, y);
                case "down" -> getSquare(x - 1, y);
                default -> null;
            };
        }
        throw new IllegalArgumentException();
    }

    public void movePlayer(String direction) {
        Objects.requireNonNull(direction);
        Square player = getSquarePlayer();
        Square s  = nextSquare(player.x(), player.y(), direction);
        validDirection(direction);
        if(!notInTheBoard(s)) {
            if(s.isEmpty() || s.name().equals("Flag")) {
                setSquare(s.x(), s.y(), new Object(s.x(), s.y(), player.name()));
                setSquare(player.x(), player.y(), new Object(player.x(), player.y(), "null"));
            }
        }
    }

    public void moveBlock(Square from, Square to) {
        if (from.isObject())
            setSquare(to.x(), to.y(), new Object(to.x(), to.y(), from.name()));
        else if (from.isName())
            setSquare(to.x(), to.y(), new Name(to.x(), to.y(), from.name()));
        else if (from.isOperator())
            setSquare(to.x(), to.y(), new Operator(to.x(), to.y(), from.name()));
        else if(from.isProperty())
            setSquare(to.x(), to.y(), new Property(to.x(), to.y(), from.name()));

        setSquare(from.x(), from.y(), new Object(from.x(), from.y(), "null"));
    }

    public void displayBoard() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print("[" + getSquare(i, j).representation() + "]");
            }
            System.out.println();
        }
    }
}
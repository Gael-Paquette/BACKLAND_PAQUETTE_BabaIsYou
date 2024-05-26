package fr.esiee.babaisyou.model;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final Map<String, ArrayList<Square>> board;

    public GameBoard(int rows, int cols) {
        if(rows < 1 || cols < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.cols = cols;
        this.board = new LinkedHashMap<>();
        initializeBoard();
    }

    private void initializeBoard() {
        for(int row = 0 ; row < this.rows ; row++) {
            for(int col = 0 ; col < this.cols ; col++) {
                board.put(key(row, col), new ArrayList<>());
                board.get(key(row, col)).add(new Object(row, col, "NULL"));
            }
        }
    }

    private String key(int row, int col) {
        if(inTheBoard(row, col))
            return "x : " + row + ", y : " + col;
        throw new IllegalArgumentException("Coordinates out of bounds ! (x : " + row + ", y : " + col + ")");
    }

    public int getRows() { return this.rows; }

    public int getCols() { return this.cols; }

    public Square getSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col)).getFirst();
        return null;
    }

    public ArrayList<Square> getObjectsOfTheSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col));
        return null;
    }

    public void addSquare(int row, int col, Square square) {
        if(inTheBoard(row, col))
            board.get(key(row, col)).add(square);
    }

    public void updateSquare(int row, int col, Square square) {
        Objects.requireNonNull(square);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).add(square);
        }
    }

    public void updateSquareAll(int row, int col, List<Square> squares) {
        Objects.requireNonNull(squares);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).addAll(squares);
        }
    }

    public boolean inTheBoard(int row, int col) { return (row >= 0 && row < rows) && (col >= 0 && col < cols); }

    public boolean notInTheBoard(Square s) { return s == null; }

    public boolean canMove(Square s, String direction) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square next = nextSquare(s.getX(), s.getY(), direction);
        return !notInTheBoard(next) && next.isEmpty();
    }

    public void validDirection(String direction) {
        Objects.requireNonNull(direction);
        if (!direction.equals("LEFT") && !direction.equals("RIGHT") && !direction.equals("UP") && !direction.equals("DOWN"))
            throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public Square getSquarePlayer() {
        ArrayList<Square> squares;
        for(int row = 0 ; row < this.rows ; row++) {
            for(int col = 0 ; col < this.cols ; col++) {
                squares = board.get(key(row, col));
                for(Square s : squares) {
                    if(s.representation().equals("X"))
                        return s;
                }
            }
        }
        return null;
    }

    public Square getSquareFlag() {
        ArrayList<Square> squares;
        for(int row = 0 ; row < this.rows ; row++) {
            for(int col = 0 ; col < this.cols ; col++) {
                squares = board.get(key(row, col));
                for(Square s : squares) {
                    if(s.representation().equals("⚑"))
                        return s;
                }
            }
        }
        return null;
    }

    public int countElementToPush(String direction) {
        Objects.requireNonNull(direction);
        Square player = getSquarePlayer();
        Square block;
        int count = 0;
        if (!notInTheBoard(player)) {
            block = nextSquare(player.getX(), player.getY(), direction);
            validDirection(direction);
            while (block != null && block.isPushable(this)) {
                count++;
                block = nextSquare(block.getX(), block.getY(), direction);
            }
        }
        return count;
    }

    public boolean facingABlock(Square player, String direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        if (notInTheBoard(player)) return false;
        validDirection(direction);
        Square next = nextSquare(player.getX(), player.getY(), direction);
        if (!notInTheBoard(next)) return (next.isObject() && !next.name().equals("NULL")) || (next.isName()) || (next.isOperator()) || (next.isProperty());
        return false;
    }

    public boolean isRuleActive(String name, String operator, String property) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(property);
        Rule rule = new Rule();
        return rule.isValidRule(this, name, operator, property);
    }

    public boolean playerIsPresent() {
        return isRuleActive("BABA", "IS", "YOU");
    }

    public boolean win() {
        return isRuleActive("FLAG", "IS", "WIN")
                && getSquarePlayer().getX() == getSquareFlag().getX() && getSquarePlayer().getY() == getSquareFlag().getY();
    }

    public boolean canPushChain(int elements, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square block = nextSquare(getSquarePlayer().getX(), getSquarePlayer().getY(), direction);
        for(int i = 1 ; i <= elements ; i++) {
            if(!block.isPushable(this)) return false;
            block = nextSquare(block.getX(), block.getY(), direction);
        }
        return true;
    }

    public void push(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        int countElementToPush, i;
        Square block, current, currentNext, player = getSquarePlayer();
        if (notInTheBoard(player)) return;
        block = nextSquare(player.getX(), player.getY(), direction);
        countElementToPush = countElementToPush(direction);
        if (countElementToPush < 1) return;
        for (i = 1; i <= countElementToPush-1; i++) {
            block = nextSquare(block.getX(), block.getY(), direction);
            if (notInTheBoard(block)) return;
        }
        if(!canMove(block, direction)) return;
        if(!canPushChain(countElementToPush, direction)) return;
        current = block;
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.getX(), current.getY(), direction);
            moveBlock(current, currentNext);
            current = nextSquareReverse(current.getX(), current.getY(), direction);
        }
    }

    public Square nextSquare(int x, int y, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        if(inTheBoard(x, y)) {
            return switch (direction) {
                case "LEFT" -> getSquare(x, y - 1);
                case "RIGHT" -> getSquare(x, y + 1);
                case "UP" -> getSquare(x - 1, y);
                case "DOWN" -> getSquare(x + 1, y);
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
                case "LEFT" -> getSquare(x, y + 1);
                case "RIGHT" -> getSquare(x, y - 1);
                case "UP" -> getSquare(x + 1, y);
                case "DOWN" -> getSquare(x - 1, y);
                default -> null;
            };
        }
        throw new IllegalArgumentException();
    }

    /* THE CROSSING IS MANAGED HERE TEMPORARILY */
    public void movePlayer(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        List<Square> squares;
        Square player = getSquarePlayer();
        Square nextSquare = nextSquare(player.getX(), player.getY(), direction);
        squares = getObjectsOfTheSquare(player.getX(), player.getY());
        squares = squares.stream().filter(square -> !square.representation().equals("X")).collect(Collectors.toList());
        if (!notInTheBoard(nextSquare)) {
            if (nextSquare.isEmpty() || nextSquare.isTraversable(this)) {
                if (nextSquare.isEmpty())
                    updateSquare(nextSquare.getX(), nextSquare.getY(), new Object(nextSquare.getX(), nextSquare.getY(), player.name()));
                else
                    addSquare(nextSquare.getX(), nextSquare.getY(), new Object(nextSquare.getX(), nextSquare.getY(), player.name()));
                if (squares.isEmpty())
                    updateSquare(player.getX(), player.getY(), new Object(player.getX(), player.getY(), "NULL"));
                else
                    updateSquareAll(player.getX(), player.getY(), squares);
            }
        }
    }

    public void moveBlock(Square from, Square to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.isObject())
            updateSquare(to.getX(), to.getY(), new Object(to.getX(), to.getY(), from.name()));
        else if (from.isName())
            updateSquare(to.getX(), to.getY(), new Name(to.getX(), to.getY(), from.name()));
        else if (from.isOperator())
            updateSquare(to.getX(), to.getY(), new Operator(to.getX(), to.getY(), from.name()));
        else if(from.isProperty())
            updateSquare(to.getX(), to.getY(), new Property(to.getX(), to.getY(), from.name()));
        updateSquare(from.getX(), from.getY(), new Object(from.getX(), from.getY(), "NULL"));
    }

    public void displayBoard() {
        ArrayList<Square> squares;
        String representations;
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                squares = board.get(key(row, col));
                representations = squares.stream().map(Square::representation).collect(Collectors.joining());
                System.out.print("[" + representations + "]");
            }
            System.out.println();
        }
    }
}
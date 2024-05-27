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
        Square next = nextSquare(s.x(), s.y(), direction);
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
            block = nextSquare(player.x(), player.y(), direction);
            validDirection(direction);
            while (block != null && block.isPushable(this)) {
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
        Square next = nextSquare(player.x(), player.y(), direction);
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
                && getSquarePlayer().x() == getSquareFlag().x() && getSquarePlayer().y() == getSquareFlag().y();
    }

    public boolean canPushChain(int elements, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square block = nextSquare(getSquarePlayer().x(), getSquarePlayer().y(), direction);
        for(int i = 1 ; i <= elements ; i++) {
            if(!block.isPushable(this)) return false;
            block = nextSquare(block.x(), block.y(), direction);
        }
        return true;
    }

    public void push(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        int countElementToPush, i;
        Square block, current, currentNext, player = getSquarePlayer();
        if (notInTheBoard(player)) return;
        block = nextSquare(player.x(), player.y(), direction);
        countElementToPush = countElementToPush(direction);
        if (countElementToPush < 1) return;
        for (i = 1; i <= countElementToPush-1; i++) {
            block = nextSquare(block.x(), block.y(), direction);
            if (notInTheBoard(block)) return;
        }
        if(!canMove(block, direction)) return;
        if(!canPushChain(countElementToPush, direction)) return;
        current = block;
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.x(), current.y(), direction);
            moveBlock(current, currentNext);
            current = nextSquareReverse(current.x(), current.y(), direction);
        }
    }

    public Square nextSquare(int row, int col, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        if(inTheBoard(row, col)) {
            return switch (direction) {
                case "LEFT" -> getSquare(row, col - 1);
                case "RIGHT" -> getSquare(row, col + 1);
                case "UP" -> getSquare(row - 1, col);
                case "DOWN" -> getSquare(row + 1, col);
                default -> null;
            };
        }
        throw new IllegalArgumentException();
    }

    public Square nextSquareReverse(int row, int col, String reverseDirection) {
        Objects.requireNonNull(reverseDirection);
        validDirection(reverseDirection);
        if(inTheBoard(row, col)) {
            return switch (reverseDirection) {
                case "LEFT" -> getSquare(row, col + 1);
                case "RIGHT" -> getSquare(row, col - 1);
                case "UP" -> getSquare(row + 1, col);
                case "DOWN" -> getSquare(row - 1, col);
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
        Square nextSquare = nextSquare(player.x(), player.y(), direction);
        squares = getObjectsOfTheSquare(player.x(), player.y());
        squares = squares.stream().filter(square -> !square.representation().equals("X")).collect(Collectors.toList());
        if (!notInTheBoard(nextSquare)) {
            if (nextSquare.isEmpty() || nextSquare.isTraversable(this)) {
                if (nextSquare.isEmpty())
                    updateSquare(nextSquare.x(), nextSquare.y(), new Object(nextSquare.x(), nextSquare.y(), player.name()));
                else
                    addSquare(nextSquare.x(), nextSquare.y(), new Object(nextSquare.x(), nextSquare.y(), player.name()));
                if (squares.isEmpty())
                    updateSquare(player.x(), player.y(), new Object(player.x(), player.y(), "NULL"));
                else
                    updateSquareAll(player.x(), player.y(), squares);
            }
        }
    }

    public void moveBlock(Square from, Square to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.isObject())
            updateSquare(to.x(), to.y(), new Object(to.x(), to.y(), from.name()));
        else if (from.isName())
            updateSquare(to.x(), to.y(), new Name(to.x(), to.y(), from.name()));
        else if (from.isOperator())
            updateSquare(to.x(), to.y(), new Operator(to.x(), to.y(), from.name()));
        else if(from.isProperty())
            updateSquare(to.x(), to.y(), new Property(to.x(), to.y(), from.name()));
        updateSquare(from.x(), from.y(), new Object(from.x(), from.y(), "NULL"));
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
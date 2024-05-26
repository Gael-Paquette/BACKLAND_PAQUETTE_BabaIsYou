package fr.esiee.babaisyou.model;

import java.util.*;

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
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                board.put(key(i, j), new ArrayList<>());
                board.get(key(i, j)).add(new Object(i, j, "NULL"));
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

    public Square getSquare(int x, int y) {
        if(inTheBoard(x, y))
            return this.board.get(key(x, y)).getFirst();
        return null;
    }

    public void addSquare(int x, int y, Square square) {
        if(inTheBoard(x, y))
            board.get(key(x, y)).add(square);
    }

    public void updateSquare(int row, int col, Square square) {
        Objects.requireNonNull(square);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).add(square);
        }
    }

    public boolean inTheBoard(int x, int y) { return (x >= 0 && x < rows) && (y >= 0 && y < cols); }

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
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                squares = board.get(key(i, j));
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
        for(int i = 0 ; i < this.rows ; i++) {
            for(int j = 0 ; j < this.cols ; j++) {
                squares = board.get(key(i, j));
                for(Square s : squares) {
                    if(s.representation().equals("F"))
                        return s;
                }
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
            block = nextSquare(player.getX(), player.getY(), direction);
            validDirection(direction);
            while (block != null && ( (block.isObject() && !block.isEmpty()) || block.isName() || block.isOperator() || block.isProperty() )) {
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
        if (!notInTheBoard(next))
            return (next.isObject() && !next.name().equals("NULL")) || (next.isName()) || (next.isOperator()) || (next.isProperty());

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

    public boolean flagIsWin() {
        return isRuleActive("FLAG", "IS", "WIN");
    }

    public boolean canPushChain(int elements, String direction, String nameOfTheBlock) {
        Objects.requireNonNull(direction);
        Objects.requireNonNull(nameOfTheBlock);
        validDirection(direction);
        Square block = nextSquare(getSquarePlayer().getX(), getSquarePlayer().getY(), direction);
        for(int i = 1 ; i <= elements ; i++) {
            if(block.representation().equals(nameOfTheBlock)) {
                return false;
            }
            block = nextSquare(block.getX(), block.getY(), direction);
        }
        return true;
    }

    public void push(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        int countElementToPush, i;
        Square block, current, currentNext, playerNext, player = getSquarePlayer();

        if (notInTheBoard(player)) return;
        block = nextSquare(player.getX(), player.getY(), direction);

        countElementToPush = countElementToPush(direction);
        if (countElementToPush < 1) return;

        for (i = 1; i <= countElementToPush-1; i++) {
            block = nextSquare(block.getX(), block.getY(), direction);
            if (notInTheBoard(block)) return;
        }

        if(!canMove(block, direction)) return;

        current = block;
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.getX(), current.getY(), direction);
            playerNext = nextSquare(player.getX(), player.getY(), direction);
            if(playerNext.representation().equals("*") && (!isRuleActive("ROCK", "IS", "PUSH")))
                return;
            if( (playerNext.isName() || playerNext.isOperator() || playerNext.isProperty()) && (!isRuleActive("ROCK", "IS", "PUSH"))
                && !canPushChain(countElementToPush, direction, "*"))
                return;

            // AUTRES CONDTIONS A INDIQUER PAR LA SUITE (TROUVER UNE AUTRE SOLUTION POUR EVITER DE SURCHARGER LA METHODE)

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

    public void movePlayer(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square player = getSquarePlayer();
        Square s  = nextSquare(player.getX(), player.getY(), direction);
        if(!notInTheBoard(s)) {
            if(s.isEmpty() || s.name().equals("FLAG")) {
                updateSquare(s.getX(), s.getY(), new Object(s.getX(), s.getY(), player.name()));
                updateSquare(player.getX(), player.getY(), new Object(player.getX(), player.getY(), "NULL"));
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
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                System.out.print("[" + getSquare(i, j).representation() + "]");
            }
            System.out.println();
        }
    }

}
package fr.esiee.babaisyou.model;

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
        setSquare(0,0, new Object(0,0, "BABA"));
        setSquare(0,9, new Object(0, 9, "FLAG"));
        setSquare(5, 3, new Object(5, 3, "ROCK"));
        setSquare(5, 4, new Object(5, 4, "ROCK"));
        setSquare(5, 5, new Object(5, 5, "ROCK"));
        setSquare(8, 3, new Name(8, 3, "ROCK"));
        setSquare(8, 4, new Operator(8, 4, "IS"));
        setSquare(8, 5, new Property(8, 5, "PUSH"));
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
        Square next = nextSquare(s.getX(), s.getY(), direction);
        return !notInTheBoard(next) && next.isEmpty();
    }

    public void validDirection(String direction) {
        Objects.requireNonNull(direction);
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
        validDirection(direction);
        Square player = getSquarePlayer();
        Square s  = nextSquare(player.getX(), player.getY(), direction);
        if(!notInTheBoard(s)) {
            if(s.isEmpty() || s.name().equals("FLAG")) {
                setSquare(s.getX(), s.getY(), new Object(s.getX(), s.getY(), player.name()));
                setSquare(player.getX(), player.getY(), new Object(player.getX(), player.getY(), "NULL"));
            }
        }
    }

    public void moveBlock(Square from, Square to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.isObject())
            setSquare(to.getX(), to.getY(), new Object(to.getX(), to.getY(), from.name()));
        else if (from.isName())
            setSquare(to.getX(), to.getY(), new Name(to.getX(), to.getY(), from.name()));
        else if (from.isOperator())
            setSquare(to.getX(), to.getY(), new Operator(to.getX(), to.getY(), from.name()));
        else if(from.isProperty())
            setSquare(to.getX(), to.getY(), new Property(to.getX(), to.getY(), from.name()));

        setSquare(from.getX(), from.getY(), new Object(from.getX(), from.getY(), "NULL"));
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
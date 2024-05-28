package fr.esiee.babaisyou.model;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final Map<String, List<Square>> board;

    public GameBoard(int rows, int cols) throws IOException {
        if(rows < 1 || cols < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.cols = cols;
        this.board = new LinkedHashMap<>();
        initializeBoard();
        /*Objects.requireNonNull(path);
        this.board = null;
        try (var reader = Files.newBufferedReader(path)) {
            String text;
            String line;
            text = reader.readLine();
            var items = text.split(":");
            this.rows = Integer.parseInt(items[0]);
            this.cols = Integer.parseInt(items[1]);
            var col = 0;
            while ((line = reader.readLine()) != null) {
                loadGameBoard(line, col);
                col += 1;
            }
        }*/
    }
    /*
    private void loadGameBoard(String line, int col) {
        Objects.requireNonNull(line);
        if (col < 0 || col >= cols) {
            throw new IllegalArgumentException("Invalid column");
        }
        var items = line.split(":");
        for (int i = 0; i < items.length; i++) {
          if (items[i].length() == 1) {
            loadSquare(items[i], i, col);
          } else {
            loadRule(items[i], i, col);
          }
        }
    }

    private void loadSquare(String item, int row, int col) {
        Objects.requireNonNull(item);
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Invalid row");
        }
        this.board[row][col] = switch (item) {
          case "X" -> new Object(row, col, "BABA");
          case "F" -> new Object(row, col, "FLAG");
          case "■" -> new Object(row, col, "WALL");
          case "~" -> new Object(row, col, "WATER");
          case "¤" -> new Object(row, col, "SKULL");
          case "§" -> new Object(row, col, "LAVA");
          case "*" -> new Object(row, col, "ROCK");
          case " " -> new Object(row, col, "NULL");
          default -> new Object(row, col, "NULL");
        };
    }

    private void loadRule(String item, int row, int col) {
        Objects.requireNonNull(item);
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IllegalArgumentException("Invalid row");
        }
        if (item.equals("BABA") || item.equals("FLAG") || item.equals("WALL") || item.equals("WATER")
            || item.equals("SKULL") || item.equals("LAVA") || item.equals("ROCK")) {
            this.board[row][col] = new Name(row, col, item);
        } else if (item.equals("IS") || item.equals("ON") || item.equals("HAS") || item.equals("AND")) {
            this.board[row][col] = new Operator(row, col, item);
        } else {
            this.board[row][col] = new Property(row, col, item);
        }
    }*/

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
        throw new IllegalArgumentException("Coordinates out of bounds !");
    }

    public int getRows() { return this.rows; }

    public int getCols() { return this.cols; }

    public Square getSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col)).getFirst();
        return null;
    }

    public void addSquare(int row, int col, Square square) {
        Objects.requireNonNull(square);
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

    public void updateAllSquares(int row, int col, List<Square> squares) {
        Objects.requireNonNull(squares);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).addAll(squares);
        }
    }

    public List<Square> getObjectsOfTheSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col));
        return null;
    }

    public Square getFirstSquarePushable(int row, int col) {
        List <Square> squares = board.get(key(row,col));
        return squares.stream().filter(s -> s.isPushable(this)).findFirst().get();
    }

    public boolean haveABlockPushable(int row, int col) {
        List <Square> squares = board.get(key(row, col));
        return squares.stream().anyMatch(s -> s.isPushable(this));
    }

    public boolean haveOneBlockNull(List<Square> squares) {
        Objects.requireNonNull(squares);
        return squares.size() == 1 && squares.getFirst().isEmpty();
    }

    public boolean inTheBoard(int row, int col) { return (row >= 0 && row < rows) && (col >= 0 && col < cols); }

    public boolean notInTheBoard(Square s) { return s == null; }

    public boolean isPlayerOn(Square square) {
        Objects.requireNonNull(square);
        Square player = getSquarePlayer();
        return player != null && player.x() == square.x() && player.y() == square.y();
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

    public void validDirection(String direction) {
        Objects.requireNonNull(direction);
        if (!direction.equals("LEFT") && !direction.equals("RIGHT") && !direction.equals("UP") && !direction.equals("DOWN"))
            throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public boolean isRuleActive(String name, String operator, String property) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(operator);
        Objects.requireNonNull(property);
        Rule rule = new Rule();
        return rule.isValidRule(this, name, operator, property);
    }

    public Square getSquarePlayer() {
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.representation().equals("X")).findFirst().get();
    }

    public boolean playerIsPresent() {
        return isRuleActive("BABA", "IS", "YOU");
    }

    public Square getSquareFlag() {
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.representation().equals("⚑")).findFirst().get();
    }

    public List<Square> typeofSquare(String name) {
        Objects.requireNonNull(name);
        var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK");
        if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.name().equals(name)).collect(Collectors.toList());
    }

    public boolean win() {
        return ((isRuleActive("FLAG", "IS", "WIN")  && getSquarePlayer().x() == getSquareFlag().x() && getSquarePlayer().y() == getSquareFlag().y()))
                || ((isRuleActive("ROCK", "IS", "WIN") && typeofSquare("ROCK").stream().anyMatch(this::isPlayerOn)))
                || ((isRuleActive("WALL", "IS", "WIN") && typeofSquare("WALL").stream().anyMatch(this::isPlayerOn)));
    }

    public int countElementToPush(String direction) {
        Objects.requireNonNull(direction);
        Square player = getSquarePlayer();
        Square block = nextSquare(player.x(), player.y(), direction);;
        validDirection(direction);
        int count = 0;
        while (!notInTheBoard(block) && haveABlockPushable(block.x(), block.y())) {
            count++;
            block = nextSquare(block.x(), block.y(), direction);
        }
        return count;
    }

    public boolean canMove(Square s, String direction) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square next = nextSquare(s.x(), s.y(), direction);
        return !notInTheBoard(next) && next.isTraversable(this);
    }

    public boolean canPushChain(int elements, String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square block = nextSquare(getSquarePlayer().x(), getSquarePlayer().y(), direction);
        for(int i = 1 ; i <= elements ; i++) {
            if(!haveABlockPushable(block.x(), block.y()))
                return false;
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
        block = getSquarePlayer();
        countElementToPush = countElementToPush(direction);
        for (i = 0 ; i < countElementToPush ; i++) {
            block = nextSquare(block.x(), block.y(), direction);
            if (notInTheBoard(block)) return;
        }
        if(!canMove(block, direction)) return;
        if(!canPushChain(countElementToPush, direction)) return;
        current = block;
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.x(), current.y(), direction);
            if(!haveOneBlockNull(getObjectsOfTheSquare(current.x(), current.y())))
                current = getFirstSquarePushable(current.x(), current.y());
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

    /* LA TRAVERSEEE EST GEREE ICI (PEUT ETRE TEMPORAIREMENT) */
    public void movePlayer(String direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        List<Square> squares;
        Square player = getSquarePlayer();
        Square nextSquare = nextSquare(player.x(), player.y(), direction);
        squares = getObjectsOfTheSquare(player.x(), player.y());
        squares = squares.stream().filter(square -> !square.representation().equals("X")).collect(Collectors.toList());
        if(notInTheBoard(nextSquare)) return;
        if (nextSquare.isEmpty() || nextSquare.isTraversable(this)) {
            if (nextSquare.isEmpty())
                updateSquare(nextSquare.x(), nextSquare.y(), new Object(nextSquare.x(), nextSquare.y(), player.name()));
            else
                addSquare(nextSquare.x(), nextSquare.y(), new Object(nextSquare.x(), nextSquare.y(), player.name()));
            if (squares.isEmpty())
                updateSquare(player.x(), player.y(), new Object(player.x(), player.y(), "NULL"));
            else
                updateAllSquares(player.x(), player.y(), squares);
        }
    }

    /* A OPTIMISER */
    public void moveBlock(Square from, Square to) {
        List<Square> squares;
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        squares = getObjectsOfTheSquare(from.x(), from.y());
        squares = squares.stream().filter(square -> !square.equals(from)).collect(Collectors.toList());
        if (from.isObject()) {
            if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
                updateSquare(to.x(), to.y(), new Object(to.x(), to.y(), from.name()));
            else
                addSquare(to.x(), to.y(), new Object(to.x(), to.y(), from.name()));
        }
        else if (from.isName()) {
            if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
                updateSquare(to.x(), to.y(), new Name(to.x(), to.y(), from.name()));
            else
                addSquare(to.x(), to.y(), new Name(to.x(), to.y(), from.name()));
        }
        else if (from.isOperator()) {
            if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
                updateSquare(to.x(), to.y(), new Operator(to.x(), to.y(), from.name()));
            else
                addSquare(to.x(), to.y(), new Operator(to.x(), to.y(), from.name()));
        }
        else if(from.isProperty()) {
            if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
                updateSquare(to.x(), to.y(), new Property(to.x(), to.y(), from.name()));
            else
                addSquare(to.x(), to.y(), new Property(to.x(), to.y(), from.name()));
        }
        if (squares.isEmpty())
            updateSquare(from.x(), from.y(), new Object(from.x(), from.y(), "NULL"));
        else
            updateAllSquares(from.x(), from.y(), squares);
    }

    public void displayBoard() {
        List<Square> squares;
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
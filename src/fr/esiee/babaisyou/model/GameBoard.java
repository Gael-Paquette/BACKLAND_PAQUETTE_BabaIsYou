package fr.esiee.babaisyou.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;

public class GameBoard {
    private final int rows;
    private final int cols;
    private final Map<String, List<Square>> board;

    public GameBoard(Path path) throws IOException {
        Objects.requireNonNull(path);
        this.board = new LinkedHashMap<>();
        try (var reader = Files.newBufferedReader(path)) {
            String size;
            String line;
            size = reader.readLine();
            var sizes = size.split(":");
            this.rows = Integer.parseInt(sizes[0]);
            this.cols = Integer.parseInt(sizes[1]);
            initializeBoard();
            var col = 0;
            while ((line = reader.readLine()) != null) {
                loadGameBoard(line, col);
                col += 1;
            }
        }
    }

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
                loadWord(items[i], i, col);
            }
        }
    }

    private void loadSquare(String squareObject, int row, int col) {
        Objects.requireNonNull(squareObject);
        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
            throw new IllegalArgumentException("Invalid row & col");
        }

        this.board.put(key(row, col), new ArrayList<>());
        switch (squareObject) {
            case "X" -> updateSquare(row, col, new Object(row, col, "BABA"));
            case "⚑" -> updateSquare(row, col, new Object(row, col, "FLAG"));
            case "■" -> updateSquare(row, col, new Object(row, col, "WALL"));
            case "~" -> updateSquare(row, col, new Object(row, col, "WATER"));
            case "¤" -> updateSquare(row, col, new Object(row, col, "SKULL"));
            case "§" -> updateSquare(row, col, new Object(row, col, "LAVA"));
            case "*" -> updateSquare(row, col, new Object(row, col, "ROCK"));
            case "#" -> updateSquare(row, col, new Object(row, col, "FLOWER"));
            case " " -> updateSquare(row, col, new Object(row, col, "NULL"));
            default -> updateSquare(row, col, new Object(row, col, "NULL"));
        }
    }

    private void loadWord(String squareWord, int row, int col) {
        Objects.requireNonNull(squareWord);
        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
            throw new IllegalArgumentException("Invalid row & col");
        }

        var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
        var operators = List.of("IS", "ON", "HAS", "AND");
        var properties = List.of("YOU", "WIN", "STOP", "PUSH", "MELT", "HOT", "DEFEAT", "SINK");
        this.board.put(key(row, col), new ArrayList<>());
        if (names.contains(squareWord)) {
            updateSquare(row, col, new Name(row, col, squareWord));
        } else if (operators.contains(squareWord)) {
            updateSquare(row, col, new Operator(row, col, squareWord));
        } else if (properties.contains(squareWord)) {
            updateSquare(row, col, new Property(row, col, squareWord));
        } else {
            throw new IllegalArgumentException("squareWord is invalid");
        }
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
        List<Square> squares = new ArrayList<>();
        squares.add(square);
        squares.addAll(board.get(key(row, col)));
        board.put(key(row, col), squares);
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

    public void transformSquare(String name1, String name2) {
        Objects.requireNonNull(name1);
        Objects.requireNonNull(name2);
        List<Square> squares = typeofSquare(name1);
        for(Square square : squares) {
            updateSquare(square.x(), square.y(), new Object(square.x(), square.y(), name2));
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

    public String convertNameOfObjectToRepresentation(String name) {
        Objects.requireNonNull(name);
        String representation;
        switch(name) {
            case "BABA" -> representation = "X";
            case "FLAG" -> representation = "⚑";
            case "WALL" -> representation ="■";
            case "WATER" -> representation = "~";
            case "SKULL" -> representation = "¤";
            case "LAVA" -> representation = "§";
            case "ROCK" -> representation = "*";
            case "FLOWER" -> representation = "#";
            default -> representation = " ";
        }
        return representation;
    }

    public boolean inTheBoard(int row, int col) { return (row >= 0 && row < rows) && (col >= 0 && col < cols); }

    public boolean notInTheBoard(Square s) { return s == null; }

    public boolean isPlayerOn(Square square) {
        Objects.requireNonNull(square);
        List<Square> squares = getSquaresPlayer();
        for(Square player : squares) {
            if(player.x() == square.x() && player.y() == square.y())
                return true;
        }
        return false;
    }

    public boolean facingABlock(Square player, Direction direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        if (notInTheBoard(player)) return false;
        validDirection(direction);
        Square next = nextSquare(player.x(), player.y(), direction);
        if (!notInTheBoard(next)) return (next.isObject() && !next.name().equals("NULL")) || (next.isName()) || (next.isOperator()) || (next.isProperty());
        return false;
    }

    public void validDirection(Direction direction) {
        Objects.requireNonNull(direction);
        if (direction != Direction.LEFT && direction != Direction.RIGHT && direction != Direction.UP && direction != Direction.DOWN)
            throw new IllegalArgumentException("Invalid direction " + direction);
    }

    public Square getSquarePlayer() {
        Rule rule = new Rule();
        String nameOfThePlayer = rule.typeOfPlayerPresent(this);
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.representation().equals(convertNameOfObjectToRepresentation(nameOfThePlayer))).findFirst().get();
    }

    public List<Square> getSquaresPlayer() {
        Rule rule = new Rule();
        String nameOfThePlayer = rule.typeOfPlayerPresent(this);
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.representation().equals(convertNameOfObjectToRepresentation(nameOfThePlayer))).toList();
    }

    public List<Square> typeofSquare(String name) {
        Objects.requireNonNull(name);
        var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
        if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
        List<Square> squares = new ArrayList<>();
        for(String key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.isObject() && square.name().equals(name)).collect(Collectors.toList());
    }

    public int countElementToPush(Direction direction) {
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

    public boolean canMove(Square s, Direction direction) {
        Objects.requireNonNull(s);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square next = nextSquare(s.x(), s.y(), direction);
        return !notInTheBoard(next) && next.isTraversable(this);
    }

    public boolean canPushChain(int elements, Direction direction) {
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

    public void push(Direction direction) {
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

    public Square nextSquare(int row, int col, Direction direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        if(inTheBoard(row, col)) {
            return switch (direction) {
                case Direction.LEFT -> getSquare(row, col - 1);
                case Direction.RIGHT -> getSquare(row, col + 1);
                case Direction.UP -> getSquare(row - 1, col);
                case Direction.DOWN -> getSquare(row + 1, col);
            };
        }
        throw new IllegalArgumentException();
    }

    public Square nextSquareReverse(int row, int col, Direction reverseDirection) {
        Objects.requireNonNull(reverseDirection);
        validDirection(reverseDirection);
        if(inTheBoard(row, col)) {
            return switch (reverseDirection) {
                case Direction.LEFT -> getSquare(row, col + 1);
                case Direction.RIGHT -> getSquare(row, col - 1);
                case Direction.UP -> getSquare(row + 1, col);
                case Direction.DOWN -> getSquare(row - 1, col);
            };
        }
        throw new IllegalArgumentException();
    }

    public void movePlayer(Square player, Direction direction) {
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square nextSquare = nextSquare(player.x(), player.y(), direction);
        List<Square> squares = getObjectsOfTheSquare(player.x(), player.y());
        squares = squares.stream().filter(square -> !square.representation().equals(convertNameOfObjectToRepresentation(player.name()))).collect(Collectors.toList());
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

    public void moveBlock(Square from, Square to) {
        List<Square> squares;
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        squares = getObjectsOfTheSquare(from.x(), from.y());
        squares = squares.stream().filter(square -> !square.equals(from)).collect(Collectors.toList());

        if (from.isObject())
            moveBlockAux(to, new Object(to.x(), to.y(), from.name()));
        else if (from.isName())
            moveBlockAux(to, new Name(to.x(), to.y(), from.name()));
        else if (from.isOperator())
            moveBlockAux(to, new Operator(to.x(), to.y(), from.name()));
        else if(from.isProperty())
            moveBlockAux(to, new Property(to.x(), to.y(), from.name()));

        if (squares.isEmpty())
            updateSquare(from.x(), from.y(), new Object(from.x(), from.y(), "NULL"));
        else
            updateAllSquares(from.x(), from.y(), squares);
    }

    private void moveBlockAux(Square to, Square newSquare) {
        if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
            updateSquare(to.x(), to.y(), newSquare);
        else
            addSquare(to.x(), to.y(), newSquare);
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
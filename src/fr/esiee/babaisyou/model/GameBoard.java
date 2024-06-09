package fr.esiee.babaisyou.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the game board for the game "Baba Is You".
 * The game board contains rows and columns of squares, each of which can hold various objects, properties, names, and operators.
 */
public class GameBoard {
    private final int rows;
    private final int cols;
    private final Map<String, List<Square>> board;

    /**
     * Constructs a GameBoard by loading it from a file.
     *
     * @param path the path to the file containing the game board configuration.
     * @throws IOException if an I/O error occurs.
     * @throws NullPointerException if the path is null.
     */
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

    /**
     * Loads the game board from a given line of text.
     * This method splits the line into items and loads each item as either a square or a word.
     *
     * @param line the line of text to load.
     * @param col  the column index for the line.
     * @throws NullPointerException     if the line is null.
     * @throws IllegalArgumentException if the column index is invalid.
     */
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

    /**
     * Loads a square object into the game board.
     * This method maps single-character representations to specific game objects.
     *
     * @param squareObject the representation of the square object.
     * @param row          the row index of the square.
     * @param col          the column index of the square.
     * @throws NullPointerException     if the squareObject is null.
     * @throws IllegalArgumentException if the row or column index is invalid.
     */
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

    /**
     * Loads a word into the game board.
     * This method determines the type of the word (name, operator, or property) and updates the board accordingly.
     *
     * @param squareWord the word to load.
     * @param row        the row index of the word.
     * @param col        the column index of the word.
     * @throws NullPointerException     if the squareWord is null.
     * @throws IllegalArgumentException if the row or column index is invalid or if the word is invalid.
     */
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

    /**
     * Initializes the game board by setting all squares to a default empty state.
     */
    private void initializeBoard() {
        for(int row = 0 ; row < this.rows ; row++) {
            for(int col = 0 ; col < this.cols ; col++) {
                board.put(key(row, col), new ArrayList<>());
                board.get(key(row, col)).add(new Object(row, col, "NULL"));
            }
        }
    }

    /**
     * Generates a key for the map based on the row and column indices.
     *
     * @param row the row index.
     * @param col the column index.
     * @return a string key representing the position on the board.
     * @throws IllegalArgumentException if the coordinates are out of bounds.
     */
    private String key(int row, int col) {
        if(inTheBoard(row, col))
            return "x : " + row + ", y : " + col;
        throw new IllegalArgumentException("Coordinates out of bounds !");
    }

    /**
     * Gets the number of rows in the game board.
     *
     * @return the number of rows.
     */
    public int getRows() { return this.rows; }

    /**
     * Gets the number of cols in the game board.
     *
     * @return the number of columns.
     */
    public int getCols() { return this.cols; }

    /**
     * Retrieves the first square at the specified position.
     *
     * @param row the row position.
     * @param col the column position.
     * @return the first square at the specified position, or null if out of bounds.
     */
    public Square getSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col)).getFirst();
        return null;
    }

    /**
     * Adds a square to the specified position on the board.
     *
     * @param row    the row position.
     * @param col    the column position.
     * @param square the square to add.
     * @throws NullPointerException if the square is null.
     */
    public void addSquare(int row, int col, Square square) {
        Objects.requireNonNull(square);
        List<Square> squares = new ArrayList<>();
        squares.add(square);
        squares.addAll(board.get(key(row, col)));
        board.put(key(row, col), squares);
    }

    /**
     * Updates the square at the specified position on the board.
     *
     * @param row    the row position.
     * @param col    the column position.
     * @param square the square to update.
     * @throws NullPointerException if the square is null.
     */
    public void updateSquare(int row, int col, Square square) {
        Objects.requireNonNull(square);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).add(square);
        }
    }

    /**
     * Updates all squares at the specified position on the board.
     *
     * @param row     the row position.
     * @param col     the column position.
     * @param squares the list of squares to update.
     * @throws NullPointerException if the squares list is null.
     */
    public void updateAllSquares(int row, int col, List<Square> squares) {
        Objects.requireNonNull(squares);
        if (inTheBoard(row, col)) {
            this.board.get(key(row, col)).clear();
            this.board.get(key(row, col)).addAll(squares);
        }
    }

    /**
     * Transforms squares of one type to another.
     *
     * @param name1 the name of the squares to transform.
     * @param name2 the new name for the transformed squares.
     * @throws NullPointerException if either name is null.
     */
    public void transformSquare(String name1, String name2) {
        Objects.requireNonNull(name1);
        Objects.requireNonNull(name2);
        List<Square> squares = typeofSquare(name1);
        for(Square square : squares) {
            updateSquare(square.x(), square.y(), new Object(square.x(), square.y(), name2));
        }
    }

    /**
     * Retrieves the list of squares at the specified position on the board.
     *
     * @param row the row position.
     * @param col the column position.
     * @return the list of squares at the specified position, or null if out of bounds.
     */
    public List<Square> getObjectsOfTheSquare(int row, int col) {
        if(inTheBoard(row, col))
            return this.board.get(key(row, col));
        return null;
    }

    /**
     * Retrieves the first pushable square at the specified position.
     *
     * @param row the row position.
     * @param col the column position.
     * @return the first pushable square.
     */
    public Square getFirstSquarePushable(int row, int col) {
        List <Square> squares = board.get(key(row,col));
        return squares.stream().filter(s -> s.isPushable(this)).findFirst().get();
    }

    /**
     * Checks if there is a pushable block at the specified position.
     *
     * @param row the row position.
     * @param col the column position.
     * @return true if there is a pushable block, false otherwise.
     */
    public boolean haveABlockPushable(int row, int col) {
        List <Square> squares = board.get(key(row, col));
        return squares.stream().anyMatch(s -> s.isPushable(this));
    }

    /**
     * Checks if the list of squares contains only one block and if that block is null.
     *
     * @param squares the list of squares to check.
     * @return true if there is only one block and it is null, false otherwise.
     * @throws NullPointerException if the squares list is null.
     */
    public boolean haveOneBlockNull(List<Square> squares) {
        Objects.requireNonNull(squares);
        return squares.size() == 1 && squares.getFirst().isEmpty();
    }

    /**
     * Converts the name of an object to its corresponding representation.
     *
     * @param name the name of the object.
     * @return the representation of the object.
     * @throws NullPointerException if the name is null.
     */
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

    /**
     * Checks if the specified row and column are within the bounds of the board.
     *
     * @param row the row position.
     * @param col the column position.
     * @return true if within bounds, false otherwise.
     */
    public boolean inTheBoard(int row, int col) { return (row >= 0 && row < rows) && (col >= 0 && col < cols); }

    /**
     * Checks if the specified square is not on the board.
     *
     * @param s the square to check.
     * @return true if the square is not on the board, false otherwise.
     */
    public boolean notInTheBoard(Square s) { return s == null; }

    /**
     * Checks if a player is on the specified square.
     *
     * @param square the square to check.
     * @return true if a player is on the square, false otherwise.
     * @throws NullPointerException if the square is null.
     */
    public boolean isPlayerOn(Square square) {
        Objects.requireNonNull(square);
        List<Square> squares = getSquaresPlayer();
        for(Square player : squares) {
            if(player.x() == square.x() && player.y() == square.y())
                return true;
        }
        return false;
    }

    /**
     * Checks if there is a block facing the specified player in the given direction.
     *
     * @param player    the player square.
     * @param direction the direction to check.
     * @return true if there is a block facing the player, false otherwise.
     * @throws NullPointerException if the player or direction is null.
     */
    public boolean facingABlock(Square player, Direction direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        if (notInTheBoard(player))
            return false;
        validDirection(direction);
        Square next = nextSquare(player.x(), player.y(), direction);
        if (!notInTheBoard(next))
            return (next.isObject() && !next.name().equals("NULL")) || (next.isName()) || (next.isOperator()) || (next.isProperty());
        return false;
    }

    /**
     * Validates if the direction is one of the allowed directions.
     *
     * @param direction the direction to validate.
     * @throws NullPointerException     if the direction is null.
     * @throws IllegalArgumentException if the direction is invalid.
     */
    public void validDirection(Direction direction) {
        Objects.requireNonNull(direction);
        if (direction != Direction.LEFT && direction != Direction.RIGHT && direction != Direction.UP && direction != Direction.DOWN)
            throw new IllegalArgumentException("Invalid direction " + direction);
    }

    /**
     * Retrieves the list of squares representing the player on the board.
     *
     * @return the list of player squares.
     */
    public List<Square> getSquaresPlayer() {
        Rule rule = new Rule();
        String nameOfThePlayer = rule.typeOfPlayerPresent(this);
        List<Square> squares = new ArrayList<>();
        for(var key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.representation().equals(convertNameOfObjectToRepresentation(nameOfThePlayer))).toList();
    }

    /**
     * Retrieves the list of squares of a specified type.
     *
     * @param name the name of the square type to retrieve.
     * @return the list of squares of the specified type.
     * @throws NullPointerException     if the name is null.
     * @throws IllegalArgumentException if the name is invalid.
     */
    public List<Square> typeofSquare(String name) {
        Objects.requireNonNull(name);
        var names = List.of("BABA", "FLAG", "WALL", "WATER", "SKULL", "LAVA", "ROCK", "FLOWER");
        if(!names.contains(name)) throw new IllegalArgumentException("Invalid name : " + name);
        List<Square> squares = new ArrayList<>();
        for(var key : board.keySet()) {
            squares.addAll(board.get(key));
        }
        return squares.stream().filter(square -> square.isObject() && square.name().equals(name)).collect(Collectors.toList());
    }

    /**
     * Counts the number of elements to push from the specified player position in the given direction.
     *
     * @param player    the player square.
     * @param direction the direction to count elements.
     * @return the number of elements to push.
     * @throws NullPointerException if the player or direction is null.
     */
    public int countElementToPush(Square player, Direction direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        Square block = nextSquare(player.x(), player.y(), direction);;
        validDirection(direction);
        int count = 0;
        while (!notInTheBoard(block) && haveABlockPushable(block.x(), block.y())) {
            count++;
            block = nextSquare(block.x(), block.y(), direction);
        }
        return count;
    }

    /**
     * Checks if the specified square can move in the given direction.
     *
     * @param current   the current square.
     * @param direction the direction to check.
     * @return true if the square can move, false otherwise.
     * @throws NullPointerException if the current square or direction is null.
     */
    public boolean canMove(Square current, Direction direction) {
        Objects.requireNonNull(current);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square next = nextSquare(current.x(), current.y(), direction);
        return !notInTheBoard(next) && next.isTraversable(this);
    }

    /**
     * Checks if a chain of elements can be pushed from the specified player position in the given direction.
     *
     * @param player    the player square.
     * @param elements  the number of elements in the chain.
     * @param direction the direction to check.
     * @return true if the chain can be pushed, false otherwise.
     * @throws NullPointerException if the player or direction is null.
     */
    public boolean canPushChain(Square player, int elements, Direction direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        validDirection(direction);
        Square block = nextSquare(player.x(), player.y(), direction);
        for(int i = 1 ; i <= elements ; i++) {
            if(!haveABlockPushable(block.x(), block.y()))
                return false;
            block = nextSquare(block.x(), block.y(), direction);
        }
        return true;
    }

    /**
     * Pushes a chain of elements starting from the specified player position in the given direction.
     *
     * @param player    the player square.
     * @param direction the direction to push.
     * @throws NullPointerException if the player or direction is null.
     */
    public void push(Square player, Direction direction) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(direction);
        validDirection(direction);
        int countElementToPush, i;
        Square block, current, currentNext;
        if (notInTheBoard(player)) return;
        block = player;
        countElementToPush = countElementToPush(player, direction);
        for (i = 0 ; i < countElementToPush ; i++) {
            block = nextSquare(block.x(), block.y(), direction);
            if (notInTheBoard(block)) return;
        }
        if(!canMove(block, direction)) return;
        if(!canPushChain(player, countElementToPush, direction)) return;
        current = block;
        for (i = 1; i <= countElementToPush; i++) {
            currentNext = nextSquare(current.x(), current.y(), direction);
            if(!haveOneBlockNull(getObjectsOfTheSquare(current.x(), current.y())))
                current = getFirstSquarePushable(current.x(), current.y());
            moveBlock(current, currentNext);
            current = nextSquareReverse(current.x(), current.y(), direction);
        }
    }

    /**
     * Retrieves the next square in the specified direction.
     *
     * @param row       the row position.
     * @param col       the column position.
     * @param direction the direction to check.
     * @return the next square in the specified direction.
     * @throws NullPointerException     if the direction is null.
     * @throws IllegalArgumentException if the direction is invalid or out of bounds.
     */
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

    /**
     * Retrieves the next square in the reverse direction.
     *
     * @param row            the row position.
     * @param col            the column position.
     * @param reverseDirection the reverse direction to check.
     * @return the next square in the reverse direction.
     * @throws NullPointerException     if the reverse direction is null.
     * @throws IllegalArgumentException if the reverse direction is invalid or out of bounds.
     */
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

    /**
     * Moves the player square in the specified direction.
     *
     * @param player    the player square.
     * @param direction the direction to move.
     * @throws NullPointerException if the player or direction is null.
     */
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

    /**
     * Moves a block from one square to another.
     *
     * @param from the square to move from.
     * @param to   the square to move to.
     * @throws NullPointerException if either square is null.
     */
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
        Rule rule = new Rule();
        if(haveOneBlockNull(board.get(key(to.x(), to.y()))))
            updateSquare(to.x(), to.y(), newSquare);
        else {
            if(board.get(key(to.x(), to.y())).stream().anyMatch(s -> s.isObject() && rule.isSink(this, s.name())))
                updateSquare(to.x(), to.y(), new Object(to.x(), to.y(), "NULL"));
            else if(board.get(key(to.x(), to.y())).stream().anyMatch(s -> s.isObject() && rule.isHot(this, s.name())) && (newSquare.isObject() && rule.isMelt(this, newSquare.name()))) {
                // BROKEN BLOCK
            }
            else
                addSquare(to.x(), to.y(), newSquare);
        }
    }
}
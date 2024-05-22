package fr.esiee.babaisyou;

import java.util.Objects;

public record Object(int x, int y, String name) implements Square {
    public Object {
        Objects.requireNonNull(name);
        if(x < 0 || y < 0)
            throw new IllegalArgumentException("x and y are negative");
        if (!name.equals("null") && !name.equals("Baba") && !name.equals("Flag") && !name.equals("Wall") && !name.equals("Water") && !name.equals("Skull") && !name.equals("Lava") && !name.equals("Rock"))
            throw new IllegalArgumentException("Invalid name: " + name);
    }

    @Override
    public boolean isName() { return false; }

    @Override
    public boolean isOperator() { return false; }

    @Override
    public boolean isProperty() { return false; }

    @Override
    public boolean isObject() { return true; }

    @Override
    public boolean isPushable(GameBoard board, Square square) {
        Objects.requireNonNull(board);
        Objects.requireNonNull(square);
        Rule rule = new Rule();
        boolean valid = false;
        if(square.name().equals("null"))
            valid = true;
        else if(square.name().equals("Baba"))
            valid = rule.isValidRule(board, "Baba", "Is", "Push");
        else if(square.name().equals("Flag"))
            valid = rule.isValidRule(board, "Flag", "Is", "Push");
        else if(square.name().equals("Wall"))
            valid = rule.isValidRule(board, "Wall", "Is", "Push");
        else if(square.name().equals("Water"))
            valid = rule.isValidRule(board, "Water", "Is", "Push");
        else if(square.name().equals("Skull"))
            valid = rule.isValidRule(board, "Skull", "Is", "Push");
        else if(square.name().equals("Lava"))
            valid = rule.isValidRule(board, "Lava", "Is", "Push");
        else if(square.name().equals("Rock"))
            valid = rule.isValidRule(board, "Rock", "Is", "Push");
        return valid;
    }

    @Override
    public boolean isEmpty() { return this.name().equals("null"); }

    @Override
    public String representation() {
        return switch (name) {
            case "Baba" -> "X";
            case "Flag" -> "F";
            case "Wall" -> "■";
            case "Water" -> "~";
            case "Skull" -> "\\uD83D\\uDC80";
            case "Lava" -> "§S";
            case "Rock" -> "*";
            default -> " ";
        };
    }
}

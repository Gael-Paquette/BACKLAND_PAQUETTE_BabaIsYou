package fr.esiee.babaisyou;

public record Square(int x, int y) {
    public Square {
        if(x < 0 || y < 0)
            throw new IllegalArgumentException();
    }
}

package fr.esiee.babaisyou;

import java.util.Objects;

public record Square(int x, int y, Element element) {

    public Square {
        Objects.requireNonNull(element);
        if(x < 0 || y < 0)
            throw new IllegalArgumentException();
    }

}

package fr.esiee.babaisyou;

public sealed interface Element permits Player, Flag, Empty {

    String representation();

    boolean isEmpty();

}

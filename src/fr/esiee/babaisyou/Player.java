package fr.esiee.babaisyou;

public record Player() implements Element {

    @Override
    public String representation() {
        return "X";
    }

}

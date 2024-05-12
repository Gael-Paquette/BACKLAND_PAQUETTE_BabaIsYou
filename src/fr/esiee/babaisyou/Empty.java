package fr.esiee.babaisyou;

public record Empty() implements Element {

    @Override
    public String representation() {
        return " ";
    }

}

package fr.esiee.babaisyou;

public record Empty() implements Element {

    @Override
    public String representation() {
        return " ";
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

}

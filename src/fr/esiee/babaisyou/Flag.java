package fr.esiee.babaisyou;

public record Flag() implements Element {

    @Override
    public String representation() {
        return "F";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

}

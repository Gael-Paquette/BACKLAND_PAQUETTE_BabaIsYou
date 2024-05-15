package fr.esiee.babaisyou;

public record Player() implements Element {
    @Override
    public String representation() { return "X"; }

    @Override
    public boolean isName() { return false; }

    @Override
    public boolean isOperator() { return false; }

    @Override
    public boolean isProperty() { return false; }

    @Override
    public boolean isEmpty() {
        return false;
    }

}

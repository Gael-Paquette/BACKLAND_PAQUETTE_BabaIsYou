package fr.esiee.babaisyou;

public record Flag() implements Element {

    @Override
    public String representation() { return "F"; }

    @Override
    public boolean isName() { return false; }

    @Override
    public boolean isOperator() { return false; }

    @Override
    public boolean isProperty() { return false; }

}

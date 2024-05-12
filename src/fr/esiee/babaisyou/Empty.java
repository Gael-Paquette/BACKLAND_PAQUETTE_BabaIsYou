package fr.esiee.babaisyou;

public record Empty() implements Element {

    @Override
    public String representation() {
        return " ";
    }

    @Override
    public boolean isName() { return false; }

    @Override
    public boolean isOperator() { return false; }

    @Override
    public boolean isProperty() { return false; }


}

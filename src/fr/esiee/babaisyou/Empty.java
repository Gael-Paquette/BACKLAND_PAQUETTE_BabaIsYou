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

    @Override
    public boolean isEmpty() { return true; }

    @Override
    public boolean isWall() { return false; }

    @Override
    public boolean isPlayer() { return false; }

    @Override
    public boolean isWater() { return false; }

    @Override
    public boolean isSkull() { return false; }

    @Override
    public boolean isLava() { return false; }

    @Override
    public boolean isRock() { return false; }

    @Override
    public boolean isFlower() { return false; }

}

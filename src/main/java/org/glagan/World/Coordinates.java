package org.glagan.World;

import jakarta.validation.constraints.Min;

public class Coordinates {
    @Min(0)
    protected int x;

    @Min(0)
    protected int y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void addX(int x) {
        this.x += x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addY(int y) {
        this.y += y;
    }
}

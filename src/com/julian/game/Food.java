package com.julian.game;

public class Food {

    private int xPos, yPos;
    private int color;
    private int worth = 100;

    public Food(int x, int y, int color) {
        xPos = x;
        yPos = y;
        this.color = color;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getColor() {
        return color;
    }

    public int getWorth() {
        return worth;
    }

}

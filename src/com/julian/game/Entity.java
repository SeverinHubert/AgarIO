package com.julian.game;

public class Entity {

    public static float V0;
    public float xPosition, yPosition;
    public float lastDx, lastDy;
    public int size;
    public int color;


    public Entity(int StartX, int StartY, int color) {
        xPosition = StartX;
        yPosition = StartY;
        lastDx = 0;
        lastDy = 0;
        this.color = color;
        size = 1000;
        V0 = (float) (170.0 * Math.sqrt(size));
    }

    public void updatePosition(float dx, float dy, float dt, int xBound, int yBound) {
        xPosition += dx * V0 * dt / Math.sqrt(size);
        yPosition += dy * V0 * dt / Math.sqrt(size);

        lastDx = dx;
        lastDy = dy;

        if (xPosition < 0) {
            xPosition = 0;
        }
        if (yPosition < 0) {
            yPosition = 0;
        }
        if (xPosition > xBound) {
            xPosition = xBound;
        }
        if (yPosition > yBound) {
            yPosition = yBound;
        }
    }

    public void updatePlayerPosition(float MouseX, float MouseY, float dt) {
        if ((MouseX - xPosition) * (MouseX - xPosition) + (MouseY - yPosition) * (MouseY - yPosition) < V0 * dt / Math.sqrt(size)) {
            return;
        }

        float distance = (float) Math.sqrt((MouseX - xPosition) * (MouseX - xPosition) + (MouseY - yPosition) * (MouseY - yPosition));
        xPosition += V0 * dt / Math.sqrt(size) * (MouseX - xPosition) / distance;
        yPosition += V0 * dt / Math.sqrt(size) * (MouseY - yPosition) / distance;

    }

}

package com.julian.game;

import com.julian.engine.AbstractGame;
import com.julian.engine.GameContainer;
import com.julian.engine.Renderer;
import com.julian.engine.gfx.ImageTile;
import hubert.severin.ai.MovementS;

import java.util.Random;

public class GameManager extends AbstractGame {

    public Entity[] entities;
    public Food[] food;
    public int foodIndex = 0;
    float animationTrigger = 0;
    private ImageTile image;
    private Random rand;
    private boolean human = true;
    private int PLAYER_NUM = 6;
    private int MAX_FOOD = 100;
    private float foodTimer = (float) 2 / PLAYER_NUM;
    private int width = 1200, hight = 800;
    private float scale = 1f;
    private float huntingRatio = (float) 1.25;
    private float foodT = 0;
    private float sizeLoss = (float) 0.3 / 60;


    public GameManager() {

        rand = new Random();
        image = new ImageTile("/PlayerAnimation2.png", 21, 21);
        entities = new Entity[PLAYER_NUM];
        food = new Food[MAX_FOOD];


        entities[0] = new Entity(rand.nextInt(width), rand.nextInt(hight), 0xffff0000);

        for (int i = 1; i < PLAYER_NUM; i++) {
            entities[i] = new Entity(rand.nextInt(width), rand.nextInt(hight), rand.nextInt());
        }

    }

    public static void main(String[] args) {
        GameContainer gc = new GameContainer(new GameManager());
        gc.start();
    }

    public void update(GameContainer gc, float dt) {

        for (int i = 0; i < PLAYER_NUM; i++) {
            entities[i].size -= entities[i].size * sizeLoss * dt;
        }

        animationTrigger += 10 * dt;
        if (animationTrigger > 9) {
            animationTrigger -= 9;
        }

        // Update Positions
        if (human) {
            entities[0].updatePlayerPosition((float) gc.getInput().getMouseX(), (float) gc.getInput().getMouseY(), dt);
        } else {
            moveEntity(entities[0], dt);
        }


        for (int i = 1; i < PLAYER_NUM; i++) {
            moveEntity(entities[i], dt);
        }

        //Update Food
        foodT += dt;
        if (foodT > foodTimer && foodIndex < MAX_FOOD && foodIndex >= 0) {
            food[foodIndex] = new Food(rand.nextInt(width), rand.nextInt(hight), rand.nextInt());

            foodIndex++;
            foodT = 0;
        }

        // Test for Eating Food
        for (int i = 0; i < PLAYER_NUM; i++) {
            for (int j = 0; j < foodIndex; j++) {

                if ((food[j].getxPos() - entities[i].xPosition) * (food[j].getxPos() - entities[i].xPosition)
                        + (food[j].getyPos() - entities[i].yPosition) * (food[j].getyPos() - entities[i].yPosition) <
                        entities[i].size) {
                    entities[i].size += food[j].getWorth();
                    for (int k = j; k < foodIndex - 1; k++) {
                        food[k] = food[k + 1];
                    }
                    foodIndex--;
                }

            }
        }

        // Test for Hunting
        for (int i = 0; i < PLAYER_NUM; i++) {
            for (int j = 0; j < PLAYER_NUM; j++) {

                if ((entities[i].size > huntingRatio * entities[j].size) &&
                        ((entities[i].xPosition - entities[j].xPosition) * (entities[i].xPosition - entities[j].xPosition)
                                + (entities[i].yPosition - entities[j].yPosition) * (entities[i].yPosition - entities[j].yPosition))
                                < (entities[i].size)) {

                    entities[i].size += entities[j].size;
                    if (j == 0) {
                        human = false;
                    }
                    PLAYER_NUM--;
                    for (int k = j; k < PLAYER_NUM; k++) {
                        entities[k] = entities[k + 1];
                    }

                }

            }
        }

    }

    public void render(GameContainer gc, Renderer r) {

        //Render Food
        for (int i = 0; i < foodIndex; i++) {
            r.drawCircle(food[i].getxPos(), food[i].getyPos(), 7, food[i].getColor());
        }

        //Render Entities
        for (int i = PLAYER_NUM - 1; i > -1; i--) {
            r.drawEntity(entities[i]);
        }
        if (human) {
            r.drawImageTile(image, (int) entities[0].xPosition - 10, (int) entities[0].yPosition - 10, (int) animationTrigger, 0);
        }


        //Render Scoreboard
        for (int i = 0; i < PLAYER_NUM; i++) {

            int yoff = 1;
            for (int j = 0; j < PLAYER_NUM; j++) {
                if ((entities[i].size > entities[j].size) || ((entities[i].size == entities[j].size) && i < j)) {
                    yoff++;
                }
            }
            if (human && i == 0) {
                r.drawText("Me@ : " + entities[i].size, 6, 22 * (PLAYER_NUM - yoff) + 5, entities[i].color);
            } else {
                r.drawText("COM" + i + ": " + entities[i].size, 6, 22 * (PLAYER_NUM - yoff) + 5, entities[i].color);
            }
        }
        r.drawRectangle(0, 0, 135, 22 * PLAYER_NUM + 10, 3, 0xffd0d0d0);

        r.drawText("Food: " + foodIndex, width - 100, 2, 0);


        //System Output


    }

    public void moveEntity(Entity e, float dt) {
        MovementS.move(e, dt, entities, food, foodIndex, width, hight);
    }


    public int getWidth() {
        return width;
    }

    public int getHight() {
        return hight;
    }

    public float getScale() {
        return scale;
    }

}

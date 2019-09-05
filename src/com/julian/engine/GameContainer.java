package com.julian.engine;

import com.julian.game.GameManager;

public class GameContainer implements Runnable {

    private final double UPDATE_CAP = 1.0 / 60.0;
    private Thread thread;
    private Window window;
    private Renderer renderer;
    private Input input;
    private GameManager game;
    private boolean running = false;
    private int width, hight;
    private float scale;
    private String title = "MyEngine";

    public GameContainer(GameManager game) {
        this.game = game;
        this.width = game.getWidth();
        this.hight = game.getHight();
        this.scale = game.getScale();

    }

    public void start() {
        window = new Window(this);
        renderer = new Renderer(this);
        input = new Input(this);

        thread = new Thread(this);
        thread.run();
    }

    public void stop() {

    }

    public void run() {
        running = true;

        boolean render = false;
        double firstTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime = 0;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps = 0;

        while (running) {

            render = false;        //Cap fps  true -> uncaped

            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= UPDATE_CAP) {
                unprocessedTime -= UPDATE_CAP;
                render = true;

                game.update(this, (float) UPDATE_CAP);

                input.update();

                if (frameTime > 1.0) {
                    fps = frames;
                    frames = 0;
                    frameTime = 0;
                }
            }


            if (render) {
                renderer.clear();

                game.render(this, renderer);

                renderer.drawText("FPS: " + fps, 0, hight - 18, 0xff000000);

                frames++;
                window.update();

            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        dispose();
    }

    public void dispose() {

    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHight() {
        return hight;
    }

    public void setHight(int hight) {
        this.hight = hight;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public GameManager getGame() {
        return game;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }

    public Input getInput() {
        return input;
    }
}



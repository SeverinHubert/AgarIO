package com.julian.engine;

import com.julian.engine.gfx.Font;
import com.julian.engine.gfx.Image;
import com.julian.engine.gfx.ImageTile;
import com.julian.game.Entity;

import java.awt.image.DataBufferInt;
import java.util.Random;

public class Renderer {

    private int pW, pH;
    private int[] p;

    private Font font = Font.STANDARD;


    public Renderer(GameContainer gc) {
        pW = gc.getWidth();
        pH = gc.getHight();
        p = ((DataBufferInt) gc.getWindow().getImage().getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        for (int i = 0; i < p.length; i++) {
            p[i] = 0xffffffff;
        }
    }

    public void noise(int strength) {
        Random rn = new Random();
        for (int i = 0; i < p.length; i++) {
            int rnint = rn.nextInt(strength);
            p[i] += (256 * 256 * rnint + 256 * rnint + rnint);
        }
    }

    public void setPixel(int x, int y, int value) {

        if (x < 0 || x >= pW || y < 0 || y >= pH || value == 0xffff00ff) {
            return;
        }

        p[x + pW * y] = value;

    }

    public void drawImage(Image image, int offX, int offY) {

        for (int y = 0; y < image.getH(); y++) {
            for (int x = 0; x < image.getW(); x++) {

                setPixel(x + offX, y + offY, image.getP()[x + y * image.getW()]);

            }
        }

    }

    public void drawImageTile(ImageTile image, int offX, int offY, int tileX, int tileY) {

        for (int y = 0; y < image.getTileH(); y++) {
            for (int x = 0; x < image.getTileW(); x++) {

                setPixel(x + offX, y + offY, image.getP()[(x + tileX * image.getTileW()) + (y + tileY * image.getTileH()) * image.getW()]);

            }
        }

    }

    public void drawText(String text, int offX, int offY, int color) {
        text = text.toUpperCase();
        int offset = 0;

        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i) - 32;

            for (int y = 0; y < font.getFontImage().getH(); y++) {
                for (int x = 0; x < font.getWidths()[unicode]; x++) {
                    if (font.getFontImage().getP()[(x + font.getOffsets()[unicode]) + y * font.getFontImage().getW()] == 0xffffffff) {

                        setPixel(x + offX + offset, y + offY, color);

                    }
                }
            }
            offset += font.getWidths()[unicode];
        }
    }

    public void drawRectangle(int offX, int offY, int width, int hight, int thickness, int color) {

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < thickness; y++) {
                setPixel(x + offX, y + offY, color);
                setPixel(x + offX, y + offY + hight - thickness, color);
            }
        }

        for (int y = 0; y < hight; y++) {
            for (int x = 0; x < thickness; x++) {
                setPixel(x + offX, y + offY, color);
                setPixel(x + offX + width - thickness, y + offY, color);
            }
        }
    }

    public void drawCircle(int offX, int offY, int radius, int color) {

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y < radius; y++) {
                if (x * x + y * y < radius * radius) {
                    setPixel(x + offX, y + offY, color);
                }
            }
        }

    }

    public void drawHollowCircle(int offX, int offY, int radius, float thickness, int color) {

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y < radius; y++) {
                if (x * x + y * y < radius * radius && x * x + y * y >= (radius - thickness) * (radius - thickness)) {
                    setPixel(x + offX, y + offY, color);
                }
            }
        }

    }

    public void drawEntity(Entity e) {
        drawCircle((int) e.xPosition, (int) e.yPosition, (int) Math.sqrt(e.size), e.color);
    }


}

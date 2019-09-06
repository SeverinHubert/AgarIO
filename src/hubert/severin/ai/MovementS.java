package hubert.severin.ai;

import com.julian.game.Entity;
import com.julian.game.Food;

import java.util.Arrays;
import java.util.Random;

public class MovementS {
    public static void move(Entity entity, float pseudoDeltaTime, Entity[] entities, Food[] food, int foodIndex, int width, int height) {

        int numberOfRays = 8;
        int rayLength = 100;
        float[] phiArray = new float[numberOfRays];

        for (int i = 0; i < numberOfRays; i++) {
            phiArray[i] = (float) ((360 / numberOfRays) * (i + 1));
        }

        int[] distance = new int[numberOfRays];
        Arrays.fill(distance, Integer.MAX_VALUE);

        for (int i = 0; i < phiArray.length; i++) {

            float xPosition = entity.xPosition;
            float yPosition = entity.yPosition;
            for (int j = 0; j < rayLength; j++) {
                xPosition += Math.cos(phiArray[i]) * ((float) (170.0 * Math.sqrt(entity.size))) * pseudoDeltaTime / Math.sqrt(entity.size);
                yPosition += Math.sin(phiArray[i]) * ((float) (170.0 * Math.sqrt(entity.size))) * pseudoDeltaTime / Math.sqrt(entity.size);

                for (int k = 0; k < foodIndex; k++) {
                    if ((food[k].getxPos() - xPosition) * (food[k].getxPos() - xPosition)
                            + (food[k].getyPos() - yPosition) * (food[k].getyPos() - yPosition) <
                            entity.size) {
                        distance[i] = j + 1;
                        break;
                    }
                }
            }
        }

        int shortestDistanceIndex = 0;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < distance[shortestDistanceIndex])
                shortestDistanceIndex = i;
        }

        if (distance[shortestDistanceIndex] == Integer.MAX_VALUE) {
            Random rand = new Random();
            if ((rand.nextInt(200) == 0)) {
                float phi = phiArray[rand.nextInt(8)];
                entity.updatePosition((float) Math.cos(phi), (float) Math.sin(phi), pseudoDeltaTime, width, height);
            } else {
                float dx = entity.lastDx == -1 ? 1 : entity.lastDx;
                float dy = entity.lastDy == -1 ? 0 : entity.lastDy;
                if (entity.xPosition + entity.lastDx < 0 || entity.xPosition + entity.lastDx > width) {
                    dx = -entity.lastDx;
                }
                if (entity.yPosition + entity.lastDy < 0 || entity.yPosition + entity.lastDy > height) {
                    dy = -entity.lastDy;
                }
                entity.updatePosition(dx, dy, pseudoDeltaTime, width, height);
            }
        } else {
            float phi = phiArray[shortestDistanceIndex];
            entity.updatePosition((float) Math.cos(phi), (float) Math.sin(phi), pseudoDeltaTime, width, height);

        }
    }
}

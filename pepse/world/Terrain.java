package pepse.world;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Terrain class represents the terrain in the game world.
 */
public class Terrain {

    /**
     * The height of the ground at the starting point (x=0) of the terrain.
     */
    private float groundHeightAtX0;

    /**
     * The base color of the ground.
     */
    private static final Color BASE_GROUND_COLOR = new Color(212,123,74);

    /**
     * The depth of the terrain.
     */
    private static final int TERRAIN_DEPTH = 20;

    /**
     * The noise generator used for generating terrain noise.
     */
    private final NoiseGenerator noiseGenerator;

    /**
     * Constructs a new Terrain object with the specified window dimensions and seed for noise generation.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed for noise generation.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = windowDimensions.y() * ((float) 2 / 3);
        this.noiseGenerator = new NoiseGenerator(seed, (int) windowDimensions.y() / 3);

    }

    /**
     * Returns the height of the ground at the specified x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The height of the ground at the specified x-coordinate.
     */
    public float groundHeightAt(float x){
        // the x is coordinate in the game
        // we have to calculate a special height for the ground for each single x.
        float noise = (float) this.noiseGenerator.noise(x, Block.SIZE * 7);
        return this.groundHeightAtX0 + noise;
    }

    /**
     * Creates a list of blocks representing the terrain within the specified x-coordinate range.
     *
     * @param minX The minimum x-coordinate of the range.
     * @param maxX The maximum x-coordinate of the range.
     * @return A list of blocks representing the terrain.
     */
    public List<Block> createInRange(int minX, int maxX){
        List<Block> blockList = new ArrayList<>();
        int min = getMinRange(minX);
        int max =getMaxRange(maxX);
        for (int i = min; i <= max; i+=Block.SIZE) {
            int yVal = (int) Math.floor(groundHeightAt(i) / Block.SIZE) * Block.SIZE;
            for (int j = 0; j < TERRAIN_DEPTH; j++) {
                RectangleRenderable rend =new RectangleRenderable
                        (ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                Block block = new Block(new Vector2(i, yVal + j * Block.SIZE  ), rend);
                block.setTag("ground");
                blockList.add(block);
            }
        }
        return blockList;
    }

    /**
     * this function checks if the current min number divides the block size
     * if yes it returns the same number, else it finds the closest number to it that
     * divides the block size and because it's the min we search the new min number
     * which is found before the original one ( smaller than the original, so we can make sure that
     * all the required range is covered with ground).
     *
     * @param min the starting of the range.
     * @return min number that divides by block size.
     */
    private int getMinRange(int min){
        int new_min = min;
        if(min % Block.SIZE == 0){
            return min;
        }
        else {
            while(new_min % Block.SIZE != 0){
                new_min -= 1;
            }
            return new_min;
        }
    }


    /**
     * this function checks if the current max number divides the block size
     * if yes it returns the same number, else it finds the closest number to it that
     * divides the block size and because it's the max we search the new max number
     * which is found after the original one ( greater than the original, so we can make sure that
     * all the required range is covered with ground).
     *
     * @param max the starting of the range.
     * @return max number that divides by block size.
     */
    private int getMaxRange(int max){
        int new_max = max;
        if(max % Block.SIZE == 0){
            return max;
        }
        else {
            while(new_max % Block.SIZE != 0){
                new_max += 1;
            }
            return new_max;
        }
    }
}

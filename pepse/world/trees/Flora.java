package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Represents a collection of trees in the game world.
 */
public class Flora {

    private final int seed;
    private final Color TREE_TRUNK_COLOR = new Color(100,50,20);
    private final Color TREE_LEAVES_COLOR = new Color(50,200,30);
    private final int TREE_HEIGHT = 360;
    private final int RIGHT_X_RANGE_COORDINATE = 120;
    private final int LEFT_X_RANGE_COORDINATE = 90;
    private final int Y_RANGE_COORDINATES = 90;
    private final int APPLE_DIMENSIONS = 15;
    private final int DISTANCE_BETWEEN_TREES = 120;

    private final Vector2 windowDim;

    /**
     * Creates a new Flora instance.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed             The seed value for random generation.
     */
    public Flora(Vector2 windowDimensions, int seed){
        this.windowDim = windowDimensions;
        this.seed = seed;

    }

    /**
     * Creates a range of trees within the specified X coordinates.
     *
     * @param minX The minimum X coordinate.
     * @param maxX The maximum X coordinate.
     * @return A HashMap containing blocks, tree leaves, and tree fruits for each tree created.
     */
    public  HashMap<Block,  HashMap<TreeLeaves, TreeFruits>> createInRange(int minX, int maxX){
        HashMap<Block, HashMap<TreeLeaves, TreeFruits>> TreeList = new HashMap<>();
        int min = getMinRange(minX);
        int max =getMaxRange(maxX);
        Random random = new Random(1);
        RectangleRenderable rendTrunk = new RectangleRenderable
                (ColorSupplier.approximateColor(TREE_TRUNK_COLOR));
        RectangleRenderable rendLeaves = new RectangleRenderable
                (ColorSupplier.approximateColor(TREE_LEAVES_COLOR));
        Terrain ter = new Terrain(this.windowDim, seed);
        createAllTrees(min, max, random, ter, rendTrunk, rendLeaves, TreeList);
        return TreeList;
    }

    /**
     * Creates all trees within the specified range and adds them to the tree list.
     *  (full built trees with leaves and fruits.
     * @param min         The minimum X coordinate.
     * @param max         The maximum X coordinate.
     * @param random      The random number generator.
     * @param ter         The terrain object.
     * @param rendTrunk   The renderable for tree trunks.
     * @param rendLeaves  The renderable for tree leaves.
     * @param TreeList    The HashMap containing blocks, tree leaves, and tree fruits for each tree.
     */
    private void createAllTrees(int min, int max, Random random, Terrain ter, RectangleRenderable rendTrunk,
                                RectangleRenderable rendLeaves, HashMap<Block, HashMap<TreeLeaves,
            TreeFruits>> TreeList) {
        OvalRenderable appleShape = new OvalRenderable(Color.RED);
        for (int i = min; i <= max; i+=Block.SIZE) {
            float blockProb = random.nextFloat();
            if (blockProb < 0.1){
                int yVal = (int) Math.floor(ter.groundHeightAt(i)/ Block.SIZE) * Block.SIZE;
                int treeHeight = random.nextInt(TREE_HEIGHT/Block.SIZE);
                while(treeHeight < 5){
                    treeHeight = random.nextInt(TREE_HEIGHT/Block.SIZE);
                }
                int treeTop = yVal - treeHeight * Block.SIZE;
                int xRightRange = i + RIGHT_X_RANGE_COORDINATE;
                int xLeftRange = i - LEFT_X_RANGE_COORDINATE;
                int yUpRange = treeTop - Y_RANGE_COORDINATES;
                int yDownRange = treeTop + Y_RANGE_COORDINATES;
                float init = getAngle();

                Block trunkBlock = new Block(new Vector2(i,yVal-((float) (treeHeight * Block.SIZE) /2)
                        - ((float) Block.SIZE /2)),rendTrunk);


                trunkBlock.setDimensions(new Vector2(Block.SIZE,treeHeight*Block.SIZE));
                trunkBlock.setTag("trunk");
                HashMap<TreeLeaves, TreeFruits> treeLeaves = new HashMap<>();
                createLeavesAndApples(random, rendLeaves, treeHeight, xLeftRange, xRightRange,
                        yUpRange, yDownRange, treeLeaves, init, appleShape);
                TreeList.put(trunkBlock, treeLeaves);
                i += DISTANCE_BETWEEN_TREES;
            }
        }
    }

    /**
     * Creates leaves and apples for trees.
     *
     * @param random       The random number generator.
     * @param rendLeaves   The renderable for the leaves.
     * @param treeHeight   The height of the tree.
     * @param xLeftRange   The left range for x coordinates.
     * @param xRightRange  The right range for x coordinates.
     * @param yUpRange     The upper range for y coordinates.
     * @param yDownRange   The lower range for y coordinates.
     * @param treeLeaves   The mapping of tree leaves to fruits.
     * @param init         The initial value.
     * @param appleShape   The shape of the apple.
     */
    private void createLeavesAndApples(Random random, RectangleRenderable rendLeaves,
                                       int treeHeight, int xLeftRange, int xRightRange,
                                       int yUpRange, int yDownRange, HashMap<TreeLeaves,
            TreeFruits> treeLeaves, float init, OvalRenderable appleShape) {
        for (int j = 0; j < treeHeight; j++) {
            TreeLeaves.createLeaves(xLeftRange, xRightRange, yUpRange,
                    yDownRange, rendLeaves, treeLeaves, init);
        }
        for (TreeLeaves leaf : treeLeaves.keySet()){
            int appleRand = random.nextInt(10);
                TreeFruits app = new TreeFruits(leaf.getTopLeftCorner(),
                        new Vector2(APPLE_DIMENSIONS,APPLE_DIMENSIONS), appleShape);
                TreeFruits apple = app.createFruites(appleRand, leaf);
            treeLeaves.put(leaf, apple);
        }
    }


    /**
     * this function checks if the current min number divides the block size
     * if yes it returns the same number, else it finds the closest number to it that
     * divides the block size and because it's the min we search the new min number
     * which is found before the original one ( smaller than the original, so we can make sure that
     * all the required range is covered with ground).
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


    /**
     * Returns a random angle for tree leaves.
     *
     * @return A random angle for tree leaves.
     */
    private float getAngle(){
        Random rand = new Random();
        int randomInt = rand.nextInt(4);
        if(randomInt == 0){
            return 5f;
        }
        else if (randomInt == 1){
            return -5f;
        }
        else if (randomInt == 2){
            return 10f;
        }
        else{
            return -10f;
        }
    }
}


















//                for (int j = 0; j < treeHeight ; j++) {
//                    Block trunkBlock = new Block(new Vector2(i,(yVal - 30) - j*30), rendTrunk);
//                    trunkBlock.setTag("trunk");
//                    HashMap<TreeLeaves, TreeFruits> treeLeaves = new HashMap<>();
//                    TreeLeaves.createLeaves(xLeftRange, xRightRange, yUpRange,
//                    yDownRange, rendLeaves, treeLeaves, init);
//                    // Creating apples.
//                    for (TreeLeaves leaf : treeLeaves.keySet()){
//                        int appleRand = random.nextInt(10);
//                        TreeFruits app = new TreeFruits(leaf.getTopLeftCorner(),
//                        new Vector2(15,15),appleShape);
//                        TreeFruits apple = app.createFruites(appleRand, leaf);
//
//
//
//                        treeLeaves.put(leaf, apple);
//                    }
//
//                    TreeList.put(trunkBlock, treeLeaves);
//                }

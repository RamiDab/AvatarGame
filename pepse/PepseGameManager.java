package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import pepse.world.trees.Flora;
import pepse.world.trees.TreeFruits;
import pepse.world.trees.TreeLeaves;


/**
 * The main game manager class for the Pepse game.
 */
public class PepseGameManager extends GameManager {

    /**
     * The length of a cycle, used for day-night cycle calculations.
     */
    private static final float CYCLE_LENGTH = 30;

    /**
     * The initial x position of the avatar.
     */
    private static final int POS_X_AVATAR = 60;

    /**
     * The initial y position of the avatar.
     */
    private static final int POS_Y_AVATAR = 90;

    /**
     * The target frame rate for the game.
     */
    private static final int TARGET_FRAME = 40;

    /**
     * The entry point of the application.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }

    /**
     * Initializes the game by setting up the sky, sun, ground, night, trees, and avatar.
     *
     * @param imageReader      The image reader to read images for the game.
     * @param soundReader      The sound reader to read sounds for the game.
     * @param inputListener    The user input listener for the game.
     * @param windowController The window controller for the game.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowController.setTargetFramerate(TARGET_FRAME);
        createSky(imageReader, windowController);
        Random rand = new Random();
        int seed = rand.nextInt();
        createSun(windowController);
        Terrain terrain = createGround(windowController, seed);
        createNight(windowController);
        createAllTrees(windowController, seed);
        createAvatar(imageReader, inputListener, windowController, terrain);
    }

    /**
     * Creates the sky and adds it to the game world, along with the clouds.
     *
     * @param imageReader      The image reader to read images for clouds.
     * @param windowController The window controller to access window dimensions.
     */
    private void createSky(ImageReader imageReader, WindowController windowController) {
        gameObjects().addGameObject(Sky.create(windowController.getWindowDimensions()), Layer.BACKGROUND);

        new Clouds(imageReader, gameObjects());
    }

    /**
     * Creates the sun and its halo, adding them to the game world.
     *
     * @param windowController The window controller to access window dimensions.
     */
    private void createSun(WindowController windowController) {
        GameObject sun = Sun.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(SunHalo.create(sun), Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(sun,
                Layer.STATIC_OBJECTS);
    }

    /**
     * Creates the ground terrain and adds it to the game world.
     *
     * @param windowController The window controller to access window dimensions.
     * @param seed             The seed for generating terrain noise.
     * @return The created Terrain object representing the ground.
     */
    private Terrain createGround(WindowController windowController, int seed) {
        Terrain terrain = new Terrain(windowController.getWindowDimensions(), seed);
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block block : blocks) {
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        return terrain;
    }

    /**
     * Creates a night object and adds it to the game world.
     *
     * @param windowController The window controller to access window dimensions.
     */
    private void createNight(WindowController windowController) {
        GameObject night = Night.create(windowController.getWindowDimensions(), CYCLE_LENGTH);
        gameObjects().addGameObject(night);
    }


    /**
     * Creates all trees in the game world using the Flora class and adds them to the game.
     *
     * @param windowController The window controller to access window dimensions.
     * @param seed             The seed value for generating random trees.
     */
    private void createAllTrees(WindowController windowController, int seed) {
        Flora flora = new Flora(windowController.getWindowDimensions(), seed);
        HashMap<Block, HashMap<TreeLeaves, TreeFruits>> tree = flora.createInRange(0,
                (int) windowController.getWindowDimensions().x());

        for (Block tr : tree.keySet()) {
            gameObjects().addGameObject(tr, Layer.DEFAULT);
            for (TreeLeaves leaves : tree.get(tr).keySet()) {
                gameObjects().addGameObject(leaves, Layer.STATIC_OBJECTS);
                if (tree.get(tr).get(leaves) != null) {
                    gameObjects().addGameObject(tree.get(tr).get(leaves));
                }
            }
        }
    }

    /**
     * Creates an avatar game object and adds it to the game world.
     *
     * @param imageReader      The image reader used to load avatar images.
     * @param inputListener    The user input listener for controlling the avatar.
     * @param windowController The window controller to access window dimensions.
     * @param terrain          The terrain used to position the avatar at the correct height.
     */
    private void createAvatar(ImageReader imageReader, UserInputListener inputListener,
                              WindowController windowController, Terrain terrain) {
        float i = windowController.getWindowDimensions().x() - POS_X_AVATAR;
        Vector2 avatarPos = new Vector2(i, terrain.groundHeightAt(i) - POS_Y_AVATAR);
        GameObject avatar = new Avatar(avatarPos, inputListener, imageReader, gameObjects());
        avatar.setTag("avatar");
        gameObjects().addGameObject(avatar);
    }
}

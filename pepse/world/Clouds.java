package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The Clouds class is responsible for creating and managing cloud objects in the game world.
 */
public class Clouds{

    /**
     * The ImageReader instance to use for reading cloud images.
     */
    private ImageReader imageReader;

    /**
     * The GameObjectCollection instance to add the cloud objects to.
     */
    private GameObjectCollection gameObjects;

    /**
     * The size of the cloud objects.
     */
    private int CLOUD_SIZE = 800;

    /**
     * The coordinates of the first cloud object.
     */
    private final Vector2 CLOUD1_COORD = new Vector2(100, 100);

    /**
     * The coordinates of the second cloud object.
     */
    private final Vector2 CLOUD2_COORD = new Vector2(600, 150);

    /**
     * The coordinates of the third cloud object.
     */
    private final Vector2 CLOUD3_COORD = new Vector2(300, -200);

    /**
     * The coordinates of the forth cloud object.
     */
    private final Vector2 CLOUD4_COORD = new Vector2(1400, 50);

    /**
     * Creates a new Clouds instance.
     *
     * @param imageReader   The ImageReader instance to use for reading cloud images.
     * @param gameObjects   The GameObjectCollection instance to add the cloud objects to.
     */
    public Clouds(ImageReader imageReader, GameObjectCollection gameObjects){
        this.imageReader = imageReader;
        this.gameObjects = gameObjects;
        create();
    }

    /**
     * Creates the cloud objects and adds them to the game world.
     */
    private void create(){
        Renderable cloudImage1 = this.imageReader.readImage("cloud1.png", false);
        GameObject cloud1 = new GameObject(CLOUD1_COORD, new Vector2
                (this.CLOUD_SIZE,this.CLOUD_SIZE), cloudImage1);
        cloud1.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects.addGameObject(cloud1, Layer.BACKGROUND);

        Renderable cloudImage2 = this.imageReader.readImage("cloud2.png", false);
        GameObject cloud2 = new GameObject(CLOUD2_COORD, new Vector2
                (this.CLOUD_SIZE,this.CLOUD_SIZE), cloudImage2);
        cloud2.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects.addGameObject(cloud2, Layer.BACKGROUND);

        Renderable cloudImage3 = this.imageReader.readImage("cloud3.png", false);
        GameObject cloud3 = new GameObject(CLOUD3_COORD, new Vector2
                (this.CLOUD_SIZE,this.CLOUD_SIZE), cloudImage3);
        cloud3.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects.addGameObject(cloud3, Layer.BACKGROUND);

        GameObject cloud4 = new GameObject(CLOUD4_COORD, new Vector2
                (this.CLOUD_SIZE,this.CLOUD_SIZE), cloudImage1);
        cloud4.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects.addGameObject(cloud4, Layer.BACKGROUND);
    }
}

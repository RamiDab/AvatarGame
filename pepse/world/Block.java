package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.Random;


/**
 * Represents a block in the game world.
 */
public class Block extends GameObject {

    /**
     * The size of the block.
     */
    public static final int SIZE = 30;

    /**
     * tree trunk first color.
     */
    RectangleRenderable trColor1 = new RectangleRenderable(new Color(100,50,20));
    /**
     * tree trunk second color.
     */

    RectangleRenderable trColor2 = new RectangleRenderable(new Color(128, 70, 27));

    /**
     * tree trunk third color.
     */
    RectangleRenderable trColor3 = new RectangleRenderable(new Color(111, 78, 55));

    /**
     * The array of renderables representing different colors for the tree trunk.
     */
    private final Renderable[] trunkColors = new Renderable[]{trColor1, trColor2, trColor3};




    /**
     * Constructs a block object with the specified top-left corner and renderable.
     *
     * @param topLeftCorner The top-left corner of the block.
     * @param renderable    The renderable object representing the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     * Updates the block's behavior based on the game state.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (this.getTag().equals("trunk")){
            checkIfJump();
        }
    }

    /**
     * Checks if the avatar is jumping and changes the block's color accordingly.
     */
    private void checkIfJump() {
        if(Avatar.isJumping){
            Random random = new Random();
            int randColor = random.nextInt(3);
            this.renderer().setRenderable(trunkColors[randColor]);
        }

    }
}
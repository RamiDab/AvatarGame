package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Represents a fruit GameObject on a tree in the game world.
 */
public class TreeFruits extends GameObject {


    private final Vector2 appleSize = new Vector2(15,15);
    private OvalRenderable app1 = new OvalRenderable(Color.RED);
    private OvalRenderable app2 = new OvalRenderable(Color.PINK);
    private OvalRenderable app3 = new OvalRenderable(Color.ORANGE);
    private final int CYCLE_LENGTH = 30;

    private final Renderable[] appleShape = new Renderable[]{app1, app2, app3};
    private int colorsChange = 0;

    /**
     * Creates a new TreeFruits GameObject.
     *
     * @param topLeftCorner The top-left corner position of the fruit.
     * @param dimensions    The dimensions of the fruit.
     * @param renderable    The renderable component used to render the fruit.
     */
    public TreeFruits(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);

    }

    /**
     * Creates a new fruit GameObject on a tree leaf.
     *
     * @param appleRand The random value to determine if the fruit should be created.
     * @param leaf      The tree leaf GameObject on which the fruit is created.
     * @return The new TreeFruits GameObject if created, or null otherwise.
     */
    public TreeFruits createFruites(int appleRand, TreeLeaves leaf){
        if (appleRand > 7){

            TreeFruits apple = new TreeFruits(leaf.getTopLeftCorner(), appleSize,
                    appleShape[0]);
            apple.setTag("apple");
            return apple;
        }
        return null;
    }

    /**
     * Handles the event when a collision occurs with another GameObject.
     * If the other GameObject is the avatar, it increases the avatar's
     * energy value and initiates a transition
     * to make the fruit disappear temporarily.
     *
     * @param other     The other GameObject involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals("avatar")){
            Avatar.energyVal+=10;
            new Transition<>(this, (Float angle) -> this.setDimensions(new Vector2(0, 0)),
                    0f, 0f, Transition.LINEAR_INTERPOLATOR_FLOAT, CYCLE_LENGTH,
                    Transition.TransitionType.TRANSITION_ONCE, () -> {
                this.setDimensions(appleSize);
            });
        }
    }

    /**
     * Updates the fruit GameObject.
     * If the avatar is jumping, it changes the fruit's color.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (Avatar.isJumping){
            changeColors();
        }
    }

    /**
     * Changes the color of the fruit GameObject.
     * This method is called when the avatar is jumping, and it iterates through different colors
     * to simulate a color change effect.
     */
    private void changeColors() {
        colorsChange+=1;
        this.renderer().setRenderable(appleShape[colorsChange%3]);
    }
}

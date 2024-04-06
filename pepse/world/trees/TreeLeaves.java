package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;

import java.util.HashMap;
import java.util.Random;


/**
 * Represents a leaf GameObject on a tree in the game world.
 */
public class TreeLeaves extends GameObject {


    private static final float TRANSATION_TIME = 0.6f;
    private static final Vector2 leafSize = new Vector2(30,30);
    private static final float STARTING_DEG = 0f;
    private static final float FINAL_DEG = 90f;
    private static final float WAIT_TIME = 5.0f;
    private static final int WIND_ANGLE = 45;




    /**
     * Creates a new TreeLeaves GameObject.
     *
     * @param topLeftCorner The top-left corner position of the leaf.
     * @param dimensions    The dimensions of the leaf.
     * @param renderable    The renderable component used to render the leaf.
     */
    public TreeLeaves(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner,dimensions, renderable);
    }

    /**
     * Creates leaves on a tree within the specified ranges and adds them to the given treeLeaves HashMap.
     *
     * @param xLeftRange    The left range of X coordinates.
     * @param xRightRange   The right range of X coordinates.
     * @param yUpRange      The up range of Y coordinates.
     * @param yDownRange    The down range of Y coordinates.
     * @param rendLeaves    The renderable for the leaves.
     * @param treeLeaves    The HashMap to store the created leaves.
     * @param init          The initial angle for the leaves.
     */
    public static void createLeaves(int xLeftRange, int xRightRange, int yUpRange, int yDownRange,
                                    RectangleRenderable rendLeaves,
                                    HashMap<TreeLeaves, TreeFruits> treeLeaves, float init) {
        Random random = new Random();
        for (int k = xLeftRange; k < xRightRange; k+= Block.SIZE) {
            for (int l = yUpRange; l < yDownRange; l+=Block.SIZE) {
                if(random.nextInt(10) > 8){
                    TreeLeaves oneLeaf = new TreeLeaves(new Vector2(k ,l),new Vector2
                            (Block.SIZE-1,Block.SIZE-1), rendLeaves);
                    Transition<Float> angleTrans = new Transition<>(oneLeaf,
                            oneLeaf.renderer()::setRenderableAngle,
                            init, -init, Transition.LINEAR_INTERPOLATOR_FLOAT,
                            TRANSATION_TIME, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
                    new ScheduledTask(oneLeaf, WAIT_TIME, true, () -> {
                        if(oneLeaf.renderer().getRenderableAngle() == 0){
                            oneLeaf.renderer().setRenderableAngle(WIND_ANGLE);
                            oneLeaf.setDimensions(leafSize);
                        }
                        else {
                            oneLeaf.renderer().setRenderableAngle(0);
                            oneLeaf.setDimensions(new Vector2(Block.SIZE, Block.SIZE));
                        }
                    } );
                    oneLeaf.addComponent(angleTrans);
                    treeLeaves.put(oneLeaf, null);
                }
            }
        }
    }

    /**
     * Updates the leaf GameObject.
     * If the avatar is jumping, it initiates a transition to change the angle of the leaf (90 deg).
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Avatar.isJumping){
            new Transition<>(this,
                    this.renderer()::setRenderableAngle,
                    STARTING_DEG, FINAL_DEG, Transition.LINEAR_INTERPOLATOR_FLOAT,
                    TRANSATION_TIME, Transition.TransitionType.TRANSITION_ONCE, null);
        }
    }
}

package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.*;
import danogl.util.Vector2;



import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The Avatar class represents the player's character in the game.
 * It controls the avatar's movement, animation, and energy level.
 */
public class Avatar extends GameObject {
    /**
     * The horizontal velocity of the avatar.
     */
    private static final float VELOCITY_X = 400;

    /**
     * The vertical velocity of the avatar when jumping.
     */
    private static final float VELOCITY_Y = -650;

    /**
     * The gravitational acceleration applied to the avatar.
     */
    private static final float GRAVITY = 600;

    /**
     *  The energy level of the avatar.
     */
    public static float energyVal = 100;

    /**
     * all the game objects, used it to update the energy show.
     */
    private final GameObjectCollection collection;

    /**
     * The user input listener for controlling the avatar.
     */
    private final UserInputListener inputListener;

    /**
     * the game object that contains the number of the current energy.
     */
    private GameObject energyShow;

    /**
     * a boolean flag that show if the avatar is jumping ot not.
     */
    public static boolean isJumping = false;

    /**
     * An array containing all the animations for the avatar.
     * Index 0: Idle animation
     * Index 1: Running animation
     * Index 2: Jumping animation
     */
    private final AnimationRenderable[] allAnimations = new AnimationRenderable[3];

    /**
     * this is the size of the avatar
     */
    private final float MAX_ENERGY = 100;
    private final float MIN_ENERGY = 0;



    /**
     * Constructs a new Avatar object.
     *
     * @param pos           The initial position of the avatar.
     * @param inputListener The user input listener to control the avatar.
     * @param imageReader   The image reader for loading avatar images.
     * @param collection    The game object collection to manage game objects.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, ImageReader imageReader,
                  GameObjectCollection collection) {
        super(pos, Vector2.ONES.mult(50), imageReader.readImage("assets/idle_0.png",
                        false));

        this.collection = collection;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.allAnimations[0] = idleAnimation(imageReader);
        this.allAnimations[1] = runAnimation(imageReader);
        this.allAnimations[2] = jumpAnimation(imageReader);
    }

    /**
     * Creates the running animation for the avatar.
     *
     * @param imageReader The image reader for loading animation frames.
     * @return The animation renderable for the running animation.
     */
    private AnimationRenderable runAnimation(ImageReader imageReader){
        return new AnimationRenderable(new Renderable[]{
                imageReader.readImage("assets/run_0.png", false),
                imageReader.readImage("assets/run_1.png", false),
                imageReader.readImage("assets/run_2.png", false)
                , imageReader.readImage("assets/run_3.png", false),
                imageReader.readImage("assets/run_4.png", false),
                imageReader.readImage("assets/run_5.png", false)},
                0.1);
    }


    /**
     * Creates the jumping animation for the avatar.
     *
     * @param imageReader The image reader for loading animation frames.
     * @return The animation renderable for the jumping animation.
     */
    private AnimationRenderable jumpAnimation(ImageReader imageReader){

        return new AnimationRenderable(new Renderable[]{
                imageReader.readImage("assets/jump_0.png", false),
                imageReader.readImage("assets/jump_1.png", false),
                imageReader.readImage("assets/jump_2.png", false)
                , imageReader.readImage("assets/jump_3.png", false)},
                0.1);
    }

    /**
     * Creates the idle animation for the avatar.
     *
     * @param imageReader The image reader for loading animation frames.
     * @return The animation renderable for the idle animation.
     */
    private AnimationRenderable idleAnimation(ImageReader imageReader){

        return new AnimationRenderable(new Renderable[]{
                imageReader.readImage("assets/idle_0.png", false),
                imageReader.readImage("assets/idle_1.png", false),
                imageReader.readImage("assets/idle_2.png", false)
                , imageReader.readImage("assets/idle_3.png", false)},
                0.1);
    }

    /**
     * Updates the avatar's position, animation, and energy level based on user input and game state.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        checkKeysPressed(xVel);
        showLifeEnergy(energyVal);
        checkAnimation();

    }

    /**
     * Checks the user input keys and updates the avatar's velocity and energy level accordingly.
     *
     * @param xVel The horizontal velocity of the avatar.
     */
    private void checkKeysPressed(float xVel) {
        isJumping = false;
        if(!inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && !inputListener.isKeyPressed(KeyEvent.VK_LEFT)
                && getVelocity().y() == 0){
            if(energyVal < MAX_ENERGY){
                energyVal += 1;

            }
        }
        xVel = checkIFLeftRightPressed(xVel);
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0){
            if(energyVal >= 10){
                transform().setVelocityY(VELOCITY_Y);
                energyVal -= 10;
                isJumping = true;
            }
        }
        updateEnergyShow();
    }

    /**
     * this function checks if the energy is in the available range and updates it.
     */
    private void updateEnergyShow() {
        if (this.energyShow != null){
            this.collection.removeGameObject(this.energyShow);
        }
        if(energyVal>MAX_ENERGY){
            energyVal=MAX_ENERGY;
        }
        if(energyVal<MIN_ENERGY){
            energyVal=MAX_ENERGY;
        }
    }

    /**
     * this function checks if the left or right arrows are pressed and applies the needed
     * functionality.
     * @param xVel the current Horizontal velocity.
     * @return the new velocity.
     */
    private float checkIFLeftRightPressed(float xVel) {
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            if(energyVal!=MIN_ENERGY){
                xVel -= VELOCITY_X;
                energyVal-=0.5f;
            }
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            if(energyVal!=MIN_ENERGY){
                xVel += VELOCITY_X;
                energyVal -= 0.5f;
            }
        }
        return xVel;
    }

    /**
     * Checks the current user input and updates the avatar's animation state accordingly.
     */
    private void checkAnimation() {
        if (getVelocity().y() == 0 && !inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                !inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            renderer().setRenderable(this.allAnimations[0]);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)){
            renderer().setIsFlippedHorizontally(false);
            renderer().setRenderable(this.allAnimations[1]);
        }
        else if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)){
            renderer().setIsFlippedHorizontally(true);
            renderer().setRenderable(this.allAnimations[1]);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() != 0){
            renderer().setRenderable(this.allAnimations[2]);
        }
    }

    /**
     * Displays the energy level of the avatar as a text renderable.
     *
     * @param energy The energy level of the avatar.
     */
    private void showLifeEnergy(float energy){
        TextRenderable energyShow = new TextRenderable("Energy: "+ energy);
        energyShow.setColor(Color.BLACK);
        this.energyShow = new GameObject(new Vector2(50,50), new Vector2(30,30), energyShow);
        this.collection.addGameObject(this.energyShow);
    }
}

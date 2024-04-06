package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * this is the class that is responsible for making Night time in the game.
 */
public class Night {

    /**
     * the nighttime in the game is basically a black screen with a specific brightness
     * so this field is the max darkness of the night.
     */
    private static final float MIDNIGHT_OPACITY = 0.5f;

    /**
     * this is the default constructor.
     */
    public Night(){

    }

    /**\
     *
     * @param windowDimensions the dimensions of the screen
     * @param cycleLength this is the day-night cycle which is 30 seconds,
     *                    so every 30 seconds 1-day passes in the game.
     * @return  a gameObject that is a big black square on the whole game screen
     * with the appropriate transition to apply the cycle.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        RectangleRenderable blackrend = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,blackrend);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag("night");
        new Transition<>(night, night.renderer()::setOpaqueness, 0f,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}

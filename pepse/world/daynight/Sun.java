package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun in a day-night cycle simulation.
 */
public class Sun {

    private static final int SUN_DIMENSIONS = 100;
    private static final float SUN_DURATION_DEGREES = 360;

    /**
     * default constructor
     */
     public Sun(){

     }
    /**
     * Creates a new sun GameObject.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param cycleLength      The length of the day-night cycle in seconds.
     * @return The sun GameObject.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){

        OvalRenderable ov = new OvalRenderable(Color.yellow);
        GameObject sun = new GameObject(new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2),
                new Vector2(SUN_DIMENSIONS,SUN_DIMENSIONS), ov);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() * ((float) 2 / 3));
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2);
        new Transition<Float>(sun, (Float angle) ->
                sun.setCenter(initialSunCenter.subtract(cycleCenter).rotated(angle).add(cycleCenter))
        , 0f, SUN_DURATION_DEGREES, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }

}

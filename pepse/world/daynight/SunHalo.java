package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a halo effect around the sun.
 */
public class SunHalo {


    private final static int SUN_HALO_DIMENSION = 100 ;
    private final static Color SUN_HALO_COLOR = new Color(255,255,0,20);
    /**
     * default constructor
     */
    public SunHalo(){

    }


    /**
     * Creates a new sun halo GameObject.
     *
     * @param sun The sun GameObject around which the halo is created.
     * @return The sun halo GameObject.
     */
    public static GameObject create(GameObject sun){
        Vector2 haloDim = new Vector2(sun.getDimensions().x() + SUN_HALO_DIMENSION ,
                sun.getDimensions().y() + SUN_HALO_DIMENSION);
        OvalRenderable rend = new OvalRenderable(SUN_HALO_COLOR);
        GameObject sunHalo = new GameObject(sun.getTopLeftCorner(),haloDim, rend);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sunHalo");
        sunHalo.addComponent(  deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}

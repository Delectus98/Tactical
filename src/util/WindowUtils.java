package util;

import System.*;
import Graphics.Vector2f;

public final class WindowUtils {
    private WindowUtils(){}

    /**
     * When camera and viewport are displaced then mouse coordinates for collision are not in same space coordinates
     * @param coord
     * @param viewport
     * @param cam
     * @return
     */
    public static Vector2f mapCoordToPixel(Vector2f coord, Viewport viewport, Camera2D cam) {
        Vector2f offset = viewport.getTopLeftCorner().sum(cam.getCenter().sum(cam.getDimension().mul(-0.5f)));
        return offset.add(coord);
    }

    public static Vector2f mapPixelToCoord(Vector2f pixel, Viewport viewport, Camera2D cam) {
        Vector2f offset = viewport.getTopLeftCorner().sum(cam.getCenter().sum(cam.getDimension().mul(-0.5f)));
        return pixel.sum(offset.negate());
    }

}

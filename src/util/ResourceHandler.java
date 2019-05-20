package util;


import Graphics.ConstTexture;
import Graphics.Texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {
    private ResourceHandler(){}

    private static Map<String, Texture> textures = new HashMap<>();

    public static ConstTexture loadTexture(String file) throws IOException {
        return textures.put(file, new Texture(file));
    }

/*
    public static ConstSound loadSound(String sound) throws IOException {}
    public static ConstSound getTexture(String name){
        return sounds.get(name);
    }
*/
}

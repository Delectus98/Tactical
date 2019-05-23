package util;


import Graphics.ConstShader;
import Graphics.ConstTexture;
import Graphics.Shader;
import Graphics.Texture;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {
    private ResourceHandler(){}

    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Shader> shaders = new HashMap<>();

    public static ConstTexture loadTexture(String file, String name) throws IOException {
        ConstTexture t = textures.get(file);
        if (t == null) {
            Texture tmp = new Texture(file);
            t = tmp;
            textures.put(name, tmp);
        }
        return t;
    }

    public static ConstTexture getTexture(String name){
        return textures.get(name);
    }


    public static ConstShader loadShader(String vertex, String fragment, String name) throws IOException {
        ConstShader s = shaders.get(name);
        if (s == null) {
            Shader tmp = new Shader(vertex, fragment);
            s = tmp;
            shaders.put(name, tmp);
        }
        return s;
    }

    public static ConstShader getShader(String name) {
        return shaders.get(name);
    }
/*
    public static ConstSound loadSound(String sound) throws IOException {}
*/
}

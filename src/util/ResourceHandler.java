package util;


import Graphics.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ResourceHandler {
    private ResourceHandler(){}

    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, FontFamily> fonts = new HashMap<>();
    private static Map<String, Shader> shaders = new HashMap<>();
    //private static Map<String, Sound> sounds = new HashMap<>();

    public void release() {
        textures.forEach((s,t) -> t.free());
        //fonts.forEach((s,f) -> f.free());
        shaders.forEach((s,sh) -> sh.free());
    }

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


    public static FontFamily loadFont(String file, int pixel, String name) throws IOException {
        FontFamily s = fonts.get(name);
        if (s == null) {
            FontFamily tmp = new FontFamily(file, pixel);
            s = tmp;
            fonts.put(name, tmp);
        }
        return s;
    }

    public static FontFamily getFont(String name) {
        return fonts.get(name);
    }
/*
    public static ConstSound loadSound(String sound) throws IOException {}

    public static ConstSound getSound(String name) {}
*/
}

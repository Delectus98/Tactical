package util;


import Graphics.*;
import app.sounds.Sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ResourceHandler {
    private ResourceHandler(){}

    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, FontFamily> fonts = new HashMap<>();
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Sound> sounds = new HashMap<>();

    /**
     * Releases all assets from GRAM and RAM.
     */
    public static void free() {
        textures.forEach((s,t) -> t.free());
        fonts.forEach((s,f) -> f.free());
        shaders.forEach((s,sh) -> sh.free());
    }

    /**
     * Loads Texture according to filepath
     * @param file filepath to asset
     * @param name asset name
     * @return loaded texture or already loaded texture
     * @throws IOException when attempting to load texture
     */
    public static ConstTexture loadTexture(String file, String name) throws IOException {
        ConstTexture t = textures.get(file);
        if (t == null) {
            Texture tmp = new Texture(file);
            t = tmp;
            textures.put(name, tmp);
        }
        return t;
    }

    /**
     * Gives texture according to a pre-defined name
     * @param name specified name
     * @return a texture according to a pre-defined name
     */
    public static ConstTexture getTexture(String name){
        ConstTexture tmp = textures.get(name);
        if (tmp == null) tmp = Texture.DefaultTexture();
        return tmp;
    }

    /**
     * Loads Shader according to filepath
     * @param vertex filepath to vertex shader
     * @param fragment filepath to fragment shader
     * @param name asset name
     * @return loaded shader or already loaded texture
     * @throws IOException when attempting to load shader
     */
    public static ConstShader loadShader(String vertex, String fragment, String name) throws IOException {
        ConstShader s = shaders.get(name);
        if (s == null) {
            Shader tmp = new Shader(vertex, fragment);
            s = tmp;
            shaders.put(name, tmp);
        }
        return s;
    }

    /**
     * Gives shader according to a pre-defined name
     * @param name specified name
     * @return a shader according to a pre-defined name
     */
    public static ConstShader getShader(String name) {
        ConstShader tmp = shaders.get(name);
        if (tmp == null) tmp = Shader.getDefaultShader();
        return tmp;
    }

    /**
     * Loads Font according to filepath
     * @param file filepath to asset
     * @param pixel font pixels
     * @param name asset name
     * @return loaded texture or already loaded font
     * @throws IOException when attempting to load font
     */
    public static FontFamily loadFont(String file, int pixel, String name) throws IOException {
        FontFamily s = fonts.get(name);
        if (s == null) {
            FontFamily tmp = new FontFamily(file, pixel);
            s = tmp;
            fonts.put(name, tmp);
        }
        return s;
    }

    /**
     * Gives font according to a pre-defined name
     * @param name specified name
     * @return a font according to a pre-defined name
     */
    public static FontFamily getFont(String name) {
        return fonts.get(name);
    }

    public static Sound loadSound(String file, String name) throws IOException {
        Sound s = new Sound(file);
        sounds.put(name, s);
        return s;
    }

    public static Sound getSound(String name) {
        return sounds.get(name);
    }

}

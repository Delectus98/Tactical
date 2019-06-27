package util;

import System.Event;

public class TextInput {
    public TextInput(){}

    private static char c;
    private static boolean updated = false;

    public static char getChar(){
        return c;
    }

    public static boolean hasBeenUpdated() {
        return updated;
    }

    public static void reset(){
        updated = false;
    }

    public static void handle(Event event) {
        if (event.type == Event.Type.TEXTENTERED) {
            c = (char) event.textEntered;
            updated = true;
        }
    }
}

package util;

import System.*;
import System.IO.AZERTYLayout;

public class TextInput {
    public TextInput(){}

    private static char c;
    private static boolean updated = false;
    private static boolean backspace = false;
    private static boolean returnCarriage = false;
    private static Clock internalClk = new Clock();
    private static Time noPressure = Time.zero();

    private static ConstTime noPressureLimit = Time.milliseconds(40);

    public static char getChar(){
        return c;
    }

    public static boolean isTextEntered() {
        return updated;
    }

    public static boolean isReturnCarriageEntered() {
        return returnCarriage;
    }
    public static boolean isBackspaceEntered() {
        return backspace;
    }

    public static void reset(){
        updated = false;

        returnCarriage = false;
        backspace = false;

        noPressure.add(internalClk.restart());
    }

    public static void handle(Event event) {

        if (event.type == Event.Type.TEXTENTERED) {
            c = (char) event.textEntered;
            updated = true;
        } else if (event.type == Event.Type.KEYREPEAT) {

            if (noPressure.compareTo(noPressureLimit) >= 0) {
                noPressure = Time.zero();

                if (event.keyRepeated == AZERTYLayout.RETURN.getKeyID()) {
                    returnCarriage = true;
                } else if (event.keyRepeated == AZERTYLayout.BACKSPACE.getKeyID()) {
                    backspace = true;
                }
            }
        } else if (event.type == Event.Type.KEYPRESSED) {
            if (event.keyPressed == AZERTYLayout.RETURN.getKeyID()) {
                returnCarriage = true;
            } else if (event.keyPressed == AZERTYLayout.BACKSPACE.getKeyID()) {
                backspace = true;
            }
        }
    }
}

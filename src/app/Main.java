package app;

import Graphics.Color;
import System.*;

public class Main
{
    static boolean baddraw = false;

    public static void main(String[] args)
    {
        if (!baddraw)
        {
            GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
            Game current = null;
            Clock clock = new Clock();
            while (window.isOpen())
            {
                Event event;
                while ((event = window.pollEvents()) != null)
                {
                    if (event.type == Event.Type.CLOSE)
                    {
                        window.close();
                    }
                }
                window.clear(Color.Cyan);
                //Menu principal
                //....
                //
                if (!current.isFinished())
                {
                    current.update(clock.restart());
                    current.draw(window);
                }
                window.display();
            }
        } else
        {
            GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
            window.hide();
            while (window.isOpen())
            {
                Event event;
                while ((event = window.pollEvents()) != null)
                {
                    if (event.type == Event.Type.CLOSE)
                    {
                        window.close();
                    }
                }
            }
        }
    }
    //Menu principale

    //Creer partie

    //Initialiser partie

    // ( start game If(!Finished))
    // car initialisation
    //Afficher map pour que p1 place ses joueurs

    //idem pour j2

    //commencer partie pour j1

    //plus initialisation
}

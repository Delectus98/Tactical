package app;

import Graphics.Color;
import System.*;
import app.play.LocalhostGame;

import java.io.IOException;

public class Main
{

    public static void main(String[] args) throws IOException {
        GLFWWindow window = new GLFWWindow(VideoMode.getDesktopMode(), "Tactical", WindowStyle.DEFAULT);
        //Game current = new LocalhostGame(window);
        Clock clock = new Clock();

        while (window.isOpen())
        {
            Event event;
            while ((event = window.pollEvents()) != null)
            {
                //current.handle(event);

                if (event.type == Event.Type.CLOSE)
                {
                    window.close();
                }
            }
            window.clear(Color.Cyan);
            //Menu principal
            //....
            //
            /*if (!current.isFinished())
            {*/
                //current.update(clock.restart());
                //current.draw(window);
            //}
            window.display();
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

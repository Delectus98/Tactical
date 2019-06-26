package app.hud;

import Graphics.Color;
import Graphics.RectangleShape;
import Graphics.Text;
import System.Event;
import System.IO.AZERTYLayout;
import System.RenderTarget;
import util.GameInput;
import util.ResourceHandler;

public class HudGameMenu {
    private RectangleShape escapeRect;
    private Text escapeText;
    private boolean displayed = false;
    private boolean attempt = false;

    public HudGameMenu(){
        escapeText = new Text(ResourceHandler.getFont("default"), "ESCAPE");
        escapeRect = new RectangleShape(escapeText.getBounds().w, escapeText.getBounds().h);
        escapeRect.setFillColor(new Color(0.5f,0.5f,0.5f));

    }

    public void update(GameInput input){
        if (escapeRect.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePosition().y)) {
            if (input.isLeftPressed())
                escapeText.setFillColor(Color.Green);
            else
                escapeText.setFillColor(Color.Yellow);
        } else escapeText.setFillColor(Color.White);

        if ((displayed && input.isLeftReleased() && escapeRect.getBounds().contains(input.getMousePositionOnHUD().x, input.getMousePosition().y)))
            attempt = true;
    }

    public boolean isAttemptingToEscape() {
        return attempt;
    }

    public void handle(Event event) {
        if (event.type == Event.Type.KEYRELEASED && event.keyReleased == AZERTYLayout.ESCAPE.getKeyID())
            displayed = !displayed;
    }

    public void draw(RenderTarget target) {
        if (!displayed) return;
        target.draw(escapeRect);
        target.draw(escapeText);
    }
}

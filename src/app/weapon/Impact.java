package app.weapon;

import Graphics.Vector2i;
import app.Unite;
import javafx.util.Pair;

import java.util.List;

public final class Impact {
    private List<Pair<Vector2i, Integer>> impact;

    public Impact(List<Pair<Vector2i, Integer>> impact){
        this.impact = impact;
    }

    public final void apply(List<Unite> all){
        for (Unite unite : all) {
            impact.forEach(i -> {
                if (i.getKey().equals(unite.getMapPosition())) {
                    unite.takeDamages(i.getValue());
                }
            });
        }
    }
}

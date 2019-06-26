package app.animations;

import Graphics.Sprite;
import Graphics.Vector2i;
import System.ConstTime;
import System.RenderTarget;

import java.util.ArrayList;


public class TileToTileAnimation {


    private double overallDistance;
    //these coordinates are map coordinates, 0 0 being the top left
    private double currentX;
    private double currentY;
    private boolean actif;
    private double tilesPerMili;
    private double distanceTravelled;
    private double endDistance;
    //alpha is the angle between the x axis and our trajectory
    private Angle alpha;
    //These are the coeffisiants that go in front of a distance traveled to return the corresponding x and y distances.
    private double xCoef;
    private double yCoef;
    private Sprite image;

    //list des anim
    public static ArrayList<TileToTileAnimation> allAnims = new ArrayList<TileToTileAnimation>();


    public TileToTileAnimation(Vector2i startOnMap, Vector2i endOnMap, double tilesPerMili, Sprite image){
        this.tilesPerMili = tilesPerMili;
        alpha = new Angle();
        setAlpha(startOnMap, endOnMap);
        setCoefs();
        actif = true;
        this.image = image;
        currentX = (double)(startOnMap.x);
        currentY = (double)(endOnMap.y);
        this.distanceTravelled = 0.0;
        endDistance = calculateDistance(startOnMap, endOnMap);
        allAnims.add(this);

    }


    public void draw(RenderTarget target){
        image.setPosition((float)(currentX*64.0), (float)(currentY*64.0));
        target.draw(image);

    }

    public void advanceCoords(ConstTime time){
        double cycleDuration = time.asMilliseconds();
        double distance = cycleDuration*tilesPerMili;
        distanceTravelled += distance;
        if(distanceTravelled>=endDistance){
            terminateAnimation();
        }
        else {
            double xChange = xCoef * distance;
            double yChange = yCoef * distance;
            currentX += xChange;
            currentY += yChange;
        }

    }
    private void terminateAnimation(){
        actif = false;
        allAnims.remove(this);
    }

    //Tested!
    //req alpha to be set at correct value.
    private void setCoefs(){
        Angle angle = new Angle(alpha.gSize());
        if(this.alpha.gSize()>=90){
            if(this.alpha.gSize()<180){
                angle.setSize(angle.gSize()-90);
                angle.convertTo(Angle.Unit.RAD);
                xCoef = -1 * Math.sin(angle.gSize());
                yCoef = -1 * Math.cos(angle.gSize());
            }else{
                if(this.alpha.gSize()<270){
                    angle.setSize(angle.gSize()-180);
                    angle.convertTo(Angle.Unit.RAD);
                    xCoef = -1 * Math.cos(angle.gSize());
                    yCoef = Math.sin(angle.gSize());
                }else{
                    angle.setSize(angle.gSize()-270);
                    angle.convertTo(Angle.Unit.RAD);
                    xCoef = Math.sin(angle.gSize());
                    yCoef = Math.cos(angle.gSize());
                }
            }
        }else{
            angle.convertTo(Angle.Unit.RAD);
            xCoef = Math.cos(angle.gSize());
            yCoef = -1 * Math.sin(angle.gSize());
        }

    }

    //Tested!
    //Sets alpha in degrees
    private void setAlpha(Vector2i startOnMap, Vector2i endOnMap){
        double xDiff = (double)(endOnMap.x - startOnMap.x);
        double yDiff = (double)(endOnMap.y - startOnMap.y);
        if(xDiff==0){
            if(yDiff>=0){
                alpha.setSize(270);
            }
            else{
                alpha.setSize(90);
            }
        }else{
            if(yDiff==0){
                if(xDiff>=0){
                    alpha.setSize(0);
                }
                else{
                    alpha.setSize(180);
                }
            }else{
                if( (xDiff<0&&yDiff<0)||(xDiff>0&&yDiff>0) ){
                    Angle beta = new Angle(Angle.Unit.RAD, Math.atan(xDiff/yDiff));
                    beta.convertTo(Angle.Unit.DEG);
                    if(xDiff<0){
                        alpha.setSize(90+beta.gSize());
                    }else{
                        alpha.setSize(270+beta.gSize());
                    }
                }
                else{
                    Angle beta = new Angle(Angle.Unit.RAD, Math.atan((Math.abs(yDiff))/(Math.abs(xDiff))));
                    beta.convertTo(Angle.Unit.DEG);
                    if(xDiff<0){
                        alpha.setSize(180+beta.gSize());
                    }else{
                        alpha.setSize(0+beta.gSize());
                    }
                }
            }
        }

    }
    private double calculateDistance(Vector2i first, Vector2i second){
        return Math.sqrt(Math.abs(second.x-first.x)*Math.abs(second.x-first.x)+Math.abs(second.y-first.y)*Math.abs(second.y-first.y));
    }

}

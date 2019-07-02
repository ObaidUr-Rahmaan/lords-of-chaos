package com.lordsofchaos.gamelogic.entities.gameworlditems;

import com.lordsofchaos.gamelogic.entities.GameEntity;
import com.lordsofchaos.gamelogic.entities.player.Player;
import com.lordsofchaos.gamelogic.transmission.Sprite;
import com.lordsofchaos.gamelogic.transmission.SpriteType;
import org.omg.CORBA.MARSHAL;

import java.awt.*;
import java.util.ArrayList;

public class Rocket extends Bullet{

    int heading;
    int speed;
    ArrayList<GameEntity> objects;

    public Rocket(int owner, int damage, Point position, Point velocity, int bulletSize, ArrayList<GameEntity> objects, int speed){
        super(owner, damage, position, velocity, bulletSize);
        this.heading = this.getBearing();
        this.speed = speed;
        this.objects = objects;
        this.setTimeLeft(150);
    }


    @Override
    public void updateObject(){
        this.trackingDirection();
        this.fixVelocity();
        super.updateObject();
    }

    private void trackingDirection() {
        int bestDistance = 10000000;
        int headingChange = 0;
        double bearing = findBearing2();
        for(GameEntity e : objects){
            if(e instanceof Player){
                int xComponent =  (e.getLocation().x + 10) - location.x;
                int yComponent =  (e.getLocation().y + 10) - location.y;
                int dotProduct = (xComponent*velocity.x) + (yComponent*velocity.y);

                if(dotProduct > 0){
                    double playerBearing = -Math.atan2((double)xComponent, (double)yComponent);
                    int distance = (xComponent*xComponent + yComponent*yComponent);
                    if(distance< bestDistance){
                        bestDistance = distance;

                        if((playerBearing-bearing > 0 ||   ((bearing> Math.PI/2) && (playerBearing< (bearing-(3*Math.PI/2)))))  ){
                            headingChange = 5;
                        }
                        else{
                            headingChange = -5;
                        }
                    }

                }
                /*if(playerBearing-bearing < Math.PI/2 || ((playerBearing < -(Math.PI/2)) && (playerBearing+(Math.PI*2)-bearing < Math.PI/2))){
                    int potentialScore = 10000000- ((xComponent*xComponent) + (yComponent*yComponent));
                    if(potentialScore > bestHeadingScore){
                        clockwise = true;
                        bestHeadingScore = potentialScore;
                    }
                }
                else if(bearing-playerBearing < Math.PI/2 || ((bearing < -(Math.PI/2)) && (bearing+(Math.PI*2)-playerBearing < Math.PI/2))){
                    int potentialScore = 10000000- ((xComponent*xComponent) + (yComponent*yComponent));
                    if(potentialScore > bestHeadingScore){
                        clockwise = false;
                        bestHeadingScore = potentialScore;
                    }
                }*/
            }
        }
        heading += headingChange;
    }

    private void fixVelocity(){
        double fixedHeading = findBearing();
        //Use the bearing to find the x and y components of the bullets motion.
        this.setVelocity(new Point((int)(speed*Math.cos(fixedHeading)), (int)(speed*Math.sin(fixedHeading))));
    }

    private  double findBearing(){
        int rightZero = heading -90;
        if(rightZero >= 180){
            rightZero -= 360;
        }
        return (double) rightZero*Math.PI/180.0;
    }

    private  double findBearing2(){
        //Returns in the same format as the player bearings
        return (double) (heading-180)*Math.PI/180.0;
    }

    public Sprite getSprite() {
        return new Sprite(this.location, SpriteType.ROCKET, this.id, getBearing(), this.owner);
    }
}

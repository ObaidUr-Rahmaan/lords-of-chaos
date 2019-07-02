package com.lordsofchaos.graphics;

import com.lordsofchaos.gamelogic.transmission.PowerUpState;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.io.Serializable;

public abstract class GraphicObject implements Serializable {

    protected Node view;

    /*
    @param primary True is tank object is for the local player
     */
    public GraphicObject(Node view) {
        this.view = view;
    }


    public void update(double time) {
    }

    public double getTX() {
        return view.getTranslateX();
    }

    public double getTY() {
        return view.getTranslateY();
    }

    public void setLocation(Point location) {
        //view.sceneToLocal(location.getX(),location.getY());
        //view.localToScene(location.getX(),location.getY());
        //Point2D newCoords = view.localToScreen(location.getX(),location.getY());
        //Point2D newCoords = view.localToScreen(location);
        //System.out.println(newCoords);
        //System.out.println(view.screenToLocal(location.getX(),location.getY()));
        //System.out.println(view.localToScene(location.getX(),location.getY()));
        //view.setTranslateX(newCoords.getX());
        //view.setTranslateY(newCoords.getY());
        view.setTranslateX(location.getX());
        view.setTranslateY(location.getY());
        //view.setLayoutX(location.getX());
        //view.setLayoutY(location.getY());
    }

    public double getRotateView() {
        return view.getRotate();
    }

    public void rotateRight() {
        view.setRotate(view.getRotate() + 90);
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 90);
    }

    public Node getView() {
        return view;
    }

    public void setRotation(double rotation, boolean wholeGroup) {
        if (view instanceof Group && !wholeGroup){
            ((Group) view).getChildren().get(0).setRotate(rotation);
        } else {
            view.setRotate(rotation);
        }
    }

    public void setBarrelRotation(double r){
        if (view instanceof Group){
            ((Group) view).getChildren().get(1).setRotate(r);
        }
    }

    public Bounds getBarrelLocation(){
        if (view instanceof Group){
            Bounds boundsInLocal = ((Group) view).getChildren().get(1).getBoundsInLocal();
            return (((Group) view).getChildren().get(1)).localToScreen(boundsInLocal);
        }
        return null;
    }

    public void removeIndic(){
        if (view instanceof Group){
            if( ((Group) view).getChildren().get(0) instanceof Group){
                ((Group) ((Group) view).getChildren().get(0)).getChildren().remove(1);
            }
        }
    }

    public void setPowerUp(PowerUpState state) {

    }

    public void endPowerUp(){

    }

    public void setView(String s) {
        javafx.scene.image.Image powerImg = new Image(s, 74, 74, true, true);
        view = new ImageView(powerImg);
        view.setCache(true);
    }
}

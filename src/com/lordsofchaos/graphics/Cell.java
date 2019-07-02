package com.lordsofchaos.graphics;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Maze Cell
 *
 * @author Ben Sassoon
 */
public class Cell extends Rectangle implements Serializable {
    private Background bg;
    public int x, y;
    public boolean inMaze = false;


    public Cell(int x, int y, boolean inMaze, Background bg) {
        super(x, y, 30, 30);
        this.x = x;
        this.y = y;
        this.inMaze = inMaze;
        // setFill(Color.WHITE);
        //setStroke(Color.WHITE);
        this.bg = bg;
        if (!inMaze) {
            background();
        }
    }

    public void makeMaze(Background bg) {
        inMaze = true;
        //setFill(Color.BLACK);
        //setStroke(Color.BLACK);
        Image img;
        if (bg == Background.GRASSNOT) {
            img = new Image("file:./resources/graphics/img/tile.png");
        } else if (bg == Background.GRASS) {
            img = new Image("file:./resources/graphics/img/stonetile.png");
        } else {
            img = new Image("file:./resources/graphics/img/stonetile.png");
        }
        this.setFill(new ImagePattern(img));
    }

    private void background() {
        //System.out.println("This bg name: " + this.bg.name());
        String filename = "file:.//resources//graphics//img//" + this.bg.name() + ".png";
        //System.out.println(filename);
        Image img;
        try {
            img = new Image(filename);
        }catch (Exception e){
            filename = "file:.//resources//graphics//img//GRASSNOT.png";
            img = new Image(filename);
        }
//        Image img = new Image("./resources/graphics/img/" +  this.bg.name()  + ".png");
        //System.out.println("file:./resources/graphics/img/" +  bg  + ".png");
       // System.out.println("Setting fill");
        this.setFill(new ImagePattern(img));
        //System.out.println("Fill set");
    }

    public void setBackground(Background mapBackground) {
        this.bg = mapBackground;
        //System.out.println("Running background");
        background();
        //System.out.println("Background run");
    }

}

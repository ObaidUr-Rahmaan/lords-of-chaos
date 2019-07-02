package com.lordsofchaos.graphics;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Main GamePanel Class for Game
 *
 * @author Ben Sassoon
 */
public class Main extends Application {
    long lastNanoTime = System.nanoTime();

    private Maze maze;
    private Tank tank1;
    protected ArrayList<Cell> allCells;
    protected KeyCode latestKey;
    private ArrayList<Bullet> bullets;
    private ArrayList<Tank> tanks;
    private BorderPane root = new BorderPane();

    public Main(Maze maze) {
        this.maze = maze;

    }

    public void refreshObjects(ArrayList<Bullet> bullets, ArrayList<Tank> tanks) {
        this.bullets = bullets;
        this.tanks = tanks;
    }

    private Parent createContent() {

        root.setPrefSize(600, 500);

        //root.getChildren().add(maze);
        //root.getChildren().add(tank1.getView());

        return root;
    }

    @SuppressWarnings("Duplicates")
    public void start(Stage primaryStage) throws Exception {

        /*
        Creates the window
         */
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(createContent(),screenSize.getWidth(),screenSize.getHeight());
        primaryStage.setTitle("Lords of Chaos");
        primaryStage.setScene(scene);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void update(double elapsedTime) {


        for (Bullet b : bullets) {
            b.update(elapsedTime);
        }

        for (Tank t : tanks) {
            t.update(elapsedTime);
        }

        tank1.update(elapsedTime);
    }
}

package com.lordsofchaos.graphics;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Main GamePanel Class for Game
 * Generates a maze and fills cells with images
 *
 * @author Ben Sassoon
 */
public class Maze extends Parent implements Serializable {

    public VBox rows = new VBox();

    private int width = 30;
    private int height = 60;

    public ArrayList<Cell> allCells = new ArrayList<>();

    public Maze(Background bg) {
        genMaze(bg);

    }

    /*
    Generate required amount of cells, and fill with image
     */
    public void genMaze(Background bg){
        getChildren().clear();
        for (int y = 0; y < width; y++) {
            HBox row = new HBox();
            for (int x = 0; x < height; x++) {
                //System.out.println("X: " + x + " Y: " + y);
                Cell c;
                try {
                    c = new Cell(x, y, false, bg);
                }catch (Exception e){
                     c = new Cell(x, y, false, Background.GRASS);
                }
                allCells.add(c);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);

    }

    /*
    Checks neighbours of cell to see if in maze
    Prevents lots of maze blocks clunked together
    Still needs improvement
     */
    public boolean checkNeighbours(Cell c, Direction d) {
        int x = c.x;
        int y = c.y;
        try {
            Cell c1 = getCell(x + 1, y);
            Cell c2 = getCell(x, y + 1);
            Cell c3 = getCell(x - 1, y);
            Cell c4 = getCell(x, y - 1);
            if (d == Direction.HORIZONTAL) {
                return (c1.inMaze || c3.inMaze);
            } else {
                return (c4.inMaze || c2.inMaze);
            }
        } catch (IndexOutOfBoundsException e) {
            return true;
        }
    }


    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    /*
    Actually creating the maze.
    Adjust max and min variables to control how populated map is
     */

    public void importMaze(boolean[][] serversMaze, Background mapBackground) {
        System.out.println("importing now");
        for (int y = 0; y < serversMaze.length; y++) {
            for (int x = 0; x < serversMaze[y].length; x++) {
                if (serversMaze[y][x]) {
                    this.getCell(x, y).makeMaze(mapBackground);
                    //System.out.println("Adding");
                } else{
                    this.getCell(x,y).setBackground(mapBackground);
                }
            }
        }
    }
}

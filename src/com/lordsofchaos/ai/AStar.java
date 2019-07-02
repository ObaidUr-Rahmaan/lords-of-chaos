package com.lordsofchaos.ai;

import com.lordsofchaos.gamelogic.GameWorld;

import java.awt.*;
import java.io.Serializable;
import java.util.*;

/**
 * A* route from start position to goal state
 *
 * @author Zornitsa Tonozlieva
 */

public class AStar implements Serializable {

    private  float[][] heuristicMap;
    private  float[][] movementCostMap;
    private  float[][] totalCostMap;

    private PriorityQueue<Point> openSet;
    private HashSet<Point> closeSet;
    private HashSet<Point> closeSetDefault;
    private HashMap<Point, Point> previousNodeMap;

    private final int D = 1; // cost of moving from one space to adjacent space
    private int width;
    private int height;

    private boolean[][] mapWalls;

    /**
     * Constructor of A*
     * @param mapWalls map of points with walls and free space
     * @param height height of the map
     * @param width width of the map
     */

    public AStar(boolean[][] mapWalls, int height, int width) {
        this.width =   width;
        this.height = height;
        this.mapWalls = mapWalls;
        heuristicMap = new float[width][height]; // h
        movementCostMap = new float[width][height]; // g
        totalCostMap = new float[width][height]; // f

        previousNodeMap = new HashMap<>();
        openSet = new PriorityQueue<>((l, r) -> Float.compare(totalCostMap[l.x][l.y], totalCostMap[r.x][r.y]));
        closeSet = new HashSet<>();
        closeSetDefault = new HashSet<>();

        for (int x = 0; x < width-1; x++) {
            for (int y = 0; y < height-1; y++) {
                if (isWall(new Point(y,x))){
                    closeSetDefault.add(new Point(x,y));
                }
            }
        }

    }


    /**
     * finds the path to the goal
     * @param goalLocation the goal location
     * @param startLocation the start location
     * @return an ArrayList of nodes which is the route from start to goal
     */
    public ArrayList<Point> findRoute(Point goalLocation, Point startLocation) {

        goalLocation = new Point(goalLocation.x /30, goalLocation.y/30);
        startLocation = new Point(startLocation.x /30, startLocation.y/30);


        //reset
        for (int x = 0; x < width-1; x++) {
            for (int y = 0; y < height-1; y++) {
                movementCostMap[x][y] = Integer.MAX_VALUE;
                totalCostMap[x][y] = Integer.MAX_VALUE;
            }
        }

        //reset sets
        openSet.clear();
        closeSet.clear();
        previousNodeMap.clear();
        closeSet.addAll(closeSetDefault);


        //Calculate route
        openSet.add(startLocation);
        Point currentNode;

        ArrayList<Point> path = null;
        movementCostMap[startLocation.x][startLocation.y] = 0;

        while (!openSet.isEmpty()){
            //best current node
            currentNode = openSet.remove(); //poll returns exception is set it empty
            if (closeSet.contains(currentNode))
                continue;

            closeSet.add(currentNode);
            if (currentNode.equals(goalLocation)){
                path = constructPath(currentNode);
                break;
            }

            //get neighbours
            for (int x = currentNode.x - 1;x <= currentNode.x + 1; x++) {
                for (int y = currentNode.y - 1; y <= currentNode.y + 1; y++) {
                    Point neighbour = new Point(x,y);
                    int diagonal = Math.abs(currentNode.x - neighbour.x) + Math.abs(currentNode.y - neighbour.y);
                    if (x < width-1 && x > 0 && y > 0 && y < height-1 && diagonal != 2) {
                        if (!isWall(new Point(neighbour.y, neighbour.x)) &&
                                !isWall(new Point(neighbour.y, neighbour.x+1))
                            && !isWall(new Point(neighbour.y +1, neighbour.x)))
                        {
                            if (closeSet.contains(neighbour))
                                continue;

                            float GCost = movementCostMap[currentNode.x][currentNode.y] + 1;
                            //calculate heuristic
                            heuristicMap[x][y] = heuristic(goalLocation, x, y);
                            openSet.add(neighbour);
                            if (GCost >= movementCostMap[neighbour.x][neighbour.y])
                                continue;
                            previousNodeMap.put(neighbour, currentNode);
                            movementCostMap[neighbour.x][neighbour.y] = GCost;
                            totalCostMap[neighbour.x][neighbour.y] = GCost + heuristicMap[neighbour.x][neighbour.y];
                        }
                    }
                }
            }
        }

        if (path == null){
            path = new ArrayList<>();
        }
        return path;
    }

    /**
     * constructs the path in an array list
     * @param currentNode adds the current node to the path
     * @return route
     */
    private ArrayList<Point> constructPath(Point currentNode) {
        ArrayList<Point> route = new ArrayList<>();
        route.add(currentNode);
        while (previousNodeMap.keySet().contains(currentNode)) {
            currentNode = previousNodeMap.get(currentNode);
            route.add(0, currentNode);
        }
        return route;
    }

    /**
     * Calculates the heuristic with Manhattan distance not allowing diagonal movement
     * @param goal the goal position
     * @param x the x coordinate of the point we want to check the heuristic
     * @param y the y coordinate of the point we want to check the heuristic
     * @return the heuristic of the point x and y
     */
    private float heuristic(Point goal, int x, int y) {
        float dx = Math.abs(x - goal.x);
        float dy = Math.abs(y - goal.y);
        return D * (dx + dy);
    }

    /**
     * Helper method to check if there is a wall on a certain point of the map
     * @param point point to be check
     * @return whether there is a wall on not
     */

    private boolean isWall(Point point) {
        return this.mapWalls[point.x][point.y];
    }
}
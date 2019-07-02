//package com.lordsofchaos.ai;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//import static org.hamcrest.CoreMatchers.*;
//
//import java.awt.*;
//import java.util.ArrayList;
//
//import static junit.framework.TestCase.assertEquals;
//
//public class AStarJUnit {
//
//    @Test
//    public void testAStar()
//    {
//        int heigth = 30;
//        int width = 30;
//        boolean[][] map = new boolean[width][heigth];
//        AStar astar = new AStar(map, heigth, width);
//        Point enemy = new Point(30,30);
//        Point ai = new Point(150,30);
//
//        ArrayList<Point> test1 = astar.findRoute(enemy, ai);
//        ArrayList<Point> expected1 = new ArrayList<Point>();
//        expected1.add(new Point(5,1));
//        expected1.add(new Point(4,1));
//        expected1.add(new Point(3,1));
//        expected1.add(new Point(2,1));
//        expected1.add(new Point(1,1));
//
//        assertEquals(expected1.toString(),test1.toString());
//
//        map[1][4] = true; // make a wall on point 4,1
//        AStar astar2 = new AStar(map, heigth, width);
//        ArrayList<Point> test2 = astar2.findRoute(enemy, ai);
//        assertThat(expected1.toString(), not(test2.toString()));
//    }
//
//}

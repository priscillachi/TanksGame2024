package Tanks;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONTokener;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class Tree {

    private App app;
    private Level level;
    private float[] movingAverages;
    private float xCoordinate;
    private float yCoordinate;
    private int size;
    private PImage treeImage;

    /**
     * Constructor of Tree object.
     * 
     * @param app is the App object that we will pass through to allow for drawing + more implementations of the PApplet library.
     * @param level is the Level object which the Tree object belongs to.
     * @param xCoordinate is the x-coordinate of the tree.
     * @param yCoordinate is the y-coordinate of the tree.
     */
    public Tree(App app, Level level, float xCoordinate, float yCoordinate) {
        this.level = level;
        this.app = app;
        this.treeImage = level.getTreeImage();
        this.size = 32;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    /**
     * Grounds the tree on the terrain. Trees will fall when terrain falls and stay grounded on the terrain.
     */
    public void groundTree() { // ensure trees stay on the terrain and don't hang in the air
        this.movingAverages = this.level.getBackgroundTerrain().getMovingAveragePoints();
        this.yCoordinate = this.movingAverages[(int)this.xCoordinate+(this.size/2)]-this.size; // ground tree
    }

    /**
     * Draws the trees.
     */
    public void drawTree() {
        app.image(this.treeImage, this.xCoordinate, this.yCoordinate, this.size, this.size);
    }
    
}

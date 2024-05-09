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

    public Tree(App app, Level level, float xCoordinate, float yCoordinate) {
        this.level = level;
        this.app = app;
        this.treeImage = level.getTreeImage();
        this.size = 32;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void groundTree() {
        this.movingAverages = this.level.getBackgroundTerrain().getMovingAveragePoints();
        this.yCoordinate = this.movingAverages[(int)this.xCoordinate+(this.size/2)]-this.size;
    }

    public void drawTree() {
        app.image(this.treeImage, this.xCoordinate, this.yCoordinate, this.size, this.size);
    }
    
    public void moveTree() {
        ;
    }
}

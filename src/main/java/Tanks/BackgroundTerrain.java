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


public class BackgroundTerrain {

    private String backgroundImage;
    private PImage bg;
    private App app;
    private int level;
    private String txtFileName;
    private String[] layoutData;
    private String[][] terrainMatrix = new String[20][28];
    private int[] foregroundColour = new int[3];
    private ArrayList<Integer> terrainHeightsText = new ArrayList<Integer>();
    private int[] terrainHeightsFrame = new int[896];
    private float[] movingAveragePoints = new float[896];
    private Level levelObj;

    /**
     * Constructor of BackgroundTerrain.
     * 
     * @param app is the App object that we will pass through to allow for drawing + more implementations of the PApplet library.
     * @param level is the level number which a terrain belongs to.
     * @param levelObj is the Level object which a terrain belongs to.
     */
    public BackgroundTerrain(App app, int level, Level levelObj) {
        this.app = app;
        this.level = level;
        this.levelObj = levelObj;
    }

    // no need for Javadoc comments for setters and getters
    public int getLevel() {
        return this.level;
    }

    public ArrayList<Integer> getTerrainHeightsText() {
        return this.terrainHeightsText;
    }

    public int[] getTerrainHeightsFrame() {
        return this.terrainHeightsFrame;
    }

    public String[][] getTerrainMatrix() {
        return this.terrainMatrix;
    }

    public float[] getMovingAveragePoints() {
        return this.movingAveragePoints;
    }


    /**
     * Read JSON file and add background.
     * 
     * @return true if executed, false if otherwise
     */
    public boolean setBackground() {

        if (!app.levelsData.getJSONObject(level-1).isNull("background")) {
            this.backgroundImage = app.levelsData.getJSONObject(level-1).getString("background");
            this.bg = app.loadImage(app.getClass().getResource(backgroundImage).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            app.background(bg);
            return true;            
        }

        return false;
    }

    /**
     * Read JSON file and set foreground colour.
     * 
     * @return true if executed, false if otherwise
     */
    public boolean setForegroundColour() {
        if (!app.levelsData.getJSONObject(level-1).isNull("foreground-colour")) {
            String foreground = app.levelsData.getJSONObject(level-1).getString("foreground-colour");
            String[] foregroundList = foreground.split(",");

            for (int i=0; i<foregroundList.length; i++) {
                this.foregroundColour[i] = Integer.parseInt(foregroundList[i]);
            }

            return true;
        }

        return false;
    }

    /**
     * Initialise terrain at the beginning - call at setup.
     * 
     * @return true if executed, false if otherwise
     */
    public boolean setTerrainMatrix() {
        
        if (!app.levelsData.getJSONObject(level-1).isNull("layout")) {
            this.txtFileName = app.levelsData.getJSONObject(level-1).getString("layout");
            this.layoutData = app.loadStrings(txtFileName);

            // read txt file as a matrix
            for (int i=0; i<this.layoutData.length; i++) {
                if (i == 20) break;
                for (int j=0; j<this.layoutData[i].length(); j++) {
                    if (j==28) break;
                    this.terrainMatrix[i][j]=this.layoutData[i].substring(j,j+1);
                }
            }

            // get rid of null and newlines and tabs
            for (int i=0; i<28; i++) {
                for (int j=0; j<20; j++) {
                    if (this.terrainMatrix[j][i] == null || this.terrainMatrix[j][i].equals("\n") || this.terrainMatrix[j][i].equals("\t")) {
                        this.terrainMatrix[j][i] = " ";
                    }
                }
            }


            for (int i=0; i<this.terrainMatrix.length; i++) {
                for (int j=0; j<this.terrainMatrix[i].length; j++) {
                }
            }

            return true;
        }

        return false;
    }


    /**
     * Calculate the moving average from the list of heights generated in setTerrainMatrix().
     * 
     * @return true if executed, false if otherwise
     */
    public boolean calculateMovingAverage() { // what the method says

        // add heights
        for (int i=0; i<28; i++) {
            for (int j=0; j<20; j++) {
                if (this.terrainMatrix[j][i].equals("X")) {
                    this.terrainHeightsText.add(j);
                }
            }
        }

        // turn heights in txt file into pixels
        int index=0;
        while (index < this.terrainHeightsText.size()) {
            for (int i=0; i<this.terrainHeightsFrame.length; i++) {
                this.terrainHeightsFrame[i]=(this.terrainHeightsText.get(index)) * 32;

                if ((i+1)%32==0) {
                    index += 1;
                }
            }
        }


        // moving average once
        for (int i=0; i<this.terrainHeightsFrame.length; i++) {
            int sum = 0;

            int j=i;
            int increase=0;
            while (j<i+32) {
                if (j==896) break;
                sum += this.terrainHeightsFrame[j];
                j+=1;
                increase+=1;
            }

            float averagePoint = sum/increase;
            this.movingAveragePoints[i] = averagePoint;
        }

        // moving average twice
        for (int i=0; i<this.movingAveragePoints.length; i++) {
            int sum = 0;

            int j=i;
            int increase=0;
            while (j<i+32) {
                if (j==896) break;
                sum += this.movingAveragePoints[j];
                j+=1;
                increase+=1;
            }

            float averagePoint = sum/increase;
            this.movingAveragePoints[i] = averagePoint;
        }

        if (this.movingAveragePoints != null) {
            return true;
        } else {
            return false;
        }
    }
        
    /**
     * Draw terrain. Call this in the draw() function of the App class.
     */
    public void setTerrain() {
        // draw lines from height of moving point average to bottom of screen
        for (int i=0; i<this.movingAveragePoints.length; i++) {
            app.stroke(this.foregroundColour[0], this.foregroundColour[1], this.foregroundColour[2]);
            app.rect(i,this.movingAveragePoints[i],1,640-this.movingAveragePoints[i]);
        }
    }

    /**
     * Update terrain after a projectile explodes.
     * 
     * @param x is the x-coordinate where we want to update the terrain at.
     * @param newYCoordinate is the new y-coordinate of the terrain that will correspond to the x-coordinate.
     */
    public void updateTerrain(int x, float newYCoordinate) { // call when there is a projectile explosion
        this.movingAveragePoints[x] = newYCoordinate;
    }

    /**
     * Returns true when the terrain height at a certain x value is inside the explosion circle.
     * 
     * @param x is the x-coordinate that corresponds to the terrain height that we want to check.
     * @param explosionCentreX is the x-coordinate of the point where the projectile explodes.
     * @param explosionCentreY is the y-coordinate of the point where the projectile explodes.
     * @return true if terrain height is inside the circle, false if otherwise.
     */
    public boolean insideExplosion(int x, float explosionCentreX, float explosionCentreY) { // check to see if terrain points are inside explosion
        return ((((float)x-explosionCentreX)*((float)x-explosionCentreX))+
        ((this.movingAveragePoints[x]-explosionCentreY)*(this.movingAveragePoints[x]-explosionCentreY)) <= 30 * 30);
    }

    /**
     * Returns true if terrain height at a certain x value is above the explosion circle. Use this to see when to collapse the terrain.
     * 
     * @param x is the x-coordinate that corresponds to the terrain height that we want to check.
     * @param explosionCentreX is the x-coordinate of the point where the projectile explodes.
     * @param explosionCentreY is the y-coordinate of the point where the projectile explodes.
     * @return true if terrain height is above the circle, false if otherwise.
     */
    public boolean aboveExplosion(int x, float explosionCentreX, float explosionCentreY) { // check to see if terrain is above explosion - if true, call calculateNewY2 for when terrain falls down
        return (this.movingAveragePoints[x] < explosionCentreY-(float)Math.sqrt((30*30)-((explosionCentreX-(float)x)*(explosionCentreX-(float)x))));
    }

    /**
     * Calculates new y-coordinate for the terrain height at a certain x-coordinate for when the original terrain height is inside the explosion circle.
     * 
     * @param x is the x-coordinate that corresponds to the terrain height that we want to generate.
     * @param explosionCentreX is the x-coordinate of the point where the projectile explodes.
     * @param explosionCentreY is the y-coordinate of the point where the projectile explodes.
     * @return new y-coordinate for when the terrain does not fall down, and just creates a crater.
     */
    public float calculateNewY1(int x, float explosionCentreX, float explosionCentreY) { // use function of circle to update new y coordinate when terrain doesn't fall
        float circleY = (float)Math.sqrt((30*30)-((explosionCentreX-(float)x)*(explosionCentreX-(float)x)));
        float newY = this.movingAveragePoints[x]+(circleY-(this.movingAveragePoints[x]-explosionCentreY));
        return newY;
    }

    /**
     * Calculates new y-coordinate for the terrain height at a certain x-coordinate for when the original terrain height is above the explosion circle.
     * 
     * @param x is the x-coordinate that corresponds to the terrain height that we want to generate.
     * @param explosionCentreX is the x-coordinate of the point where the projectile explodes.
     * @param explosionCentreY is the y-coordinate of the point where the projectile explodes.
     * @return new y-coordinate for when the terrain falls down.
     */
    public float calculateNewY2(int x, float explosionCentreX, float explosionCentreY) { // use when there is terrain on top of explosion
        float circleY = (float)Math.sqrt((30*30)-((explosionCentreX-(float)x)*(explosionCentreX-(float)x)));
        float newY = this.movingAveragePoints[x]+(2*circleY);
        return newY;
    }
}

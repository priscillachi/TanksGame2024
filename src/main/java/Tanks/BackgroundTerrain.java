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

    public BackgroundTerrain(App app, int level, Level levelObj) {
        this.app = app;
        this.level = level;
        this.levelObj = levelObj;
    }

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


    public void setBackground() { // read JSON file and add background, foreground

        if (!app.levelsData.getJSONObject(level-1).isNull("background")) {
            this.backgroundImage = app.levelsData.getJSONObject(level-1).getString("background");
            this.bg = app.loadImage(app.getClass().getResource(backgroundImage).getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
            app.background(bg);            
        }
    }

    public void setForegroundColour() {
        if (!app.levelsData.getJSONObject(level-1).isNull("foreground-colour")) {
            String foreground = app.levelsData.getJSONObject(level-1).getString("foreground-colour");
            String[] foregroundList = foreground.split(",");

            for (int i=0; i<foregroundList.length; i++) {
                this.foregroundColour[i] = Integer.parseInt(foregroundList[i]);
            }
        }
    }

    public void setTerrainMatrix() { // initialise terrain at the beginning - only call at setup
        
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
        }
    }


    public void calculateMovingAverage() { // this can be called multiple times

        // add heights
        for (int i=0; i<28; i++) {
            for (int j=0; j<20; j++) {
                if (this.terrainMatrix[j][i].equals("X")) {
                    this.terrainHeightsText.add(j);
                }
            }
        }

        // turn heights into pixels
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
    }

        
    public void setTerrain() {
        // draw lines from height of moving point average to bottom of screen
        app.beginShape();
        for (int i=0; i<this.movingAveragePoints.length; i++) {
            app.stroke(this.foregroundColour[0], this.foregroundColour[1], this.foregroundColour[2]);
            app.rect(i,this.movingAveragePoints[i],1,640-this.movingAveragePoints[i]);
        }
        app.endShape();
    }

    public void updateTerrain() {
        // change moving average points here;
        //set terrain again;
    }
}

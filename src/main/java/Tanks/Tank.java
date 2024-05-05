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


public class Tank {
    private App app;
    private float xCoordinate;
    private float yCoordinate;
    private int[] colourScheme;
    private Level levelObj;
    private int tankWidth;
    private int tankHeight;
    private int turretWidth;
    private float[] movingAverages;
    private Player player;
    private PShape tank;
    //private PVector turretV1;
    //private PVector turretV2;
    //private PShape turret;
    private float angle;
    private int speed;
    private boolean rotateTurret=false;

    public Tank(float xCoordinate, float yCoordinate, int[] colourScheme, App app, Level levelObj, Player player) {
        this.colourScheme = colourScheme;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.app = app;
        this.levelObj = levelObj;
        this.tankWidth = 20;
        this.tankHeight = 16;
        this.turretWidth = 4;
        this.player = player;
        this.angle=0;
        this.speed = 2;
    }

    public int getTankWidth() {
        return this.tankWidth;
    }

    public int getTankHeight() {
        return this.tankHeight;
    }

    public int getTurretWidth() {
        return this.turretWidth;
    }
    
    public void groundTank() { // handling the logic for where to ground the tanks
        this.movingAverages = this.levelObj.getBackgroundTerrain().getMovingAveragePoints();
        this.yCoordinate = this.movingAverages[(int)this.xCoordinate+(this.tankWidth/2)]-(this.tankHeight);
    }


    public void drawTank() { // draw tank
        this.groundTank();
        app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
        app.strokeWeight(4);
        app.stroke(0);
        app.rotate(0);
        this.tank = app.createShape(app.RECT, this.xCoordinate, this.yCoordinate, this.tankWidth, this.tankHeight);
        app.shape(this.tank);
    }

    public void setTurret() {
        //this.turret = app.createShape(app.RECT, this.xCoordinate+(tankWidth/2)-(turretWidth/2), this.yCoordinate-this.tankHeight, this.turretWidth, this.tankHeight);
    }

    public void drawTurret() { // draw turret
        app.fill(0);
        app.stroke(0);
        app.strokeWeight(1);
        //app.rotate(this.angle);
        //this.turret = app.createShape(app.RECT, this.xCoordinate+(tankWidth/2)-(turretWidth/2), this.yCoordinate-this.tankHeight, this.turretWidth, this.tankHeight);
        //app.shape(this.turret);
        //this.turret.rotate(this.angle);
        app.pushMatrix();
        app.translate(this.xCoordinate+(this.tankWidth/2)-(this.turretWidth/2), this.yCoordinate-this.tankHeight);
        app.rotate(this.angle);
        app.rect(0, 0, this.turretWidth, this.tankHeight);
        app.popMatrix();

    }

    public void rotateTurretLeft() { // up arrow
        if (this.angle <= (float)(-Math.PI/2)) {
            ;
        } else {
            this.angle -= (float)0.1;
        }
        
        //this.turret.rotate(this.angle);
    }

    public void rotateTurretRight() { // down arrow
        if (this.angle >= (float)Math.PI/2) {
            ;
        } else {
            this.angle += (float)0.1;
        }
        
        //this.turret.rotate(this.angle);
    }

    public void moveTankLeft() { // left arrow
        if (this.xCoordinate == 0) {
            ;
        } else {
            this.xCoordinate -= this.speed;
        }
    }

    public void moveTankRight() { // right arrow
        if (this.xCoordinate == 864-20) {
            ;
        } else {
            this.xCoordinate += this.speed;
        }
    }

    public void destroyTank() {
        ;
    }


    public PShape getTank() {
        return this.tank;
    }

    //public PShape getTurret() {
        //return this.turret;
    //}

    public float getAngle() {
        return this.angle;
    }

    public boolean getRotateTurret() {
        return this.rotateTurret;
    }

    public void setRotateTurret(boolean value) {
        this.rotateTurret = value;
    }
}

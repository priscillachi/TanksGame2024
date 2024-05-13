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
    private float turretXCoordinate;
    private float turretYCoordinate;
    private int[] colourScheme;
    private Level levelObj;
    private int tankWidth;
    private int tankHeight;
    private int turretWidth;
    private float[] movingAverages;
    private Player player;
    private PShape tank;
    private float angle;
    private int speed;
    private boolean rotateTurret=false;
    private Projectile projectile;
    private float tankCentreX;
    private float tankCentreY;
    private int arrowCount = 0;
    private boolean arrowOn = false;

    public Tank(float xCoordinate, float yCoordinate, int[] colourScheme, App app, Level levelObj, Player player) {
        this.colourScheme = colourScheme;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.app = app;
        this.levelObj = levelObj;
        this.tankWidth = 30;
        this.tankHeight = 16;
        this.turretWidth = 4;
        this.player = player;
        this.angle=0;
        this.speed = 4;
        this.tankCentreX = this.xCoordinate + (this.tankWidth/2);
        this.tankCentreY = this.yCoordinate + (this.tankHeight/2);
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
        this.tankCentreY = this.yCoordinate + (this.tankHeight/2);
    }


    public void drawTank() { // draw tank
        if (this.player.getPlayerAlive() == true) {
            this.groundTank();
            app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
            app.strokeWeight(4);
            app.stroke(0);
            app.rotate(0);
            this.tank = app.createShape(app.RECT, this.xCoordinate, this.yCoordinate, this.tankWidth, this.tankHeight);
            app.shape(this.tank);
        }
    }

    public void drawTurret() { // draw turret
        if (this.player.getPlayerAlive() == true) {
            app.fill(0);
            app.stroke(0);
            app.strokeWeight(1);
            app.pushMatrix();
            this.turretXCoordinate = this.xCoordinate+(this.tankWidth/2);
            this.turretYCoordinate = this.yCoordinate;
            app.translate(this.turretXCoordinate, this.turretYCoordinate);
            app.rotate(this.angle);
            app.rectMode(app.CENTER);
            app.rect(0, 0, this.turretWidth, this.tankHeight*2);
            app.popMatrix();
            app.rectMode(app.CORNER);
        }
    }

    public void drawArrow() {
        if (this.arrowOn == true) {
            app.fill(0);
            app.stroke(0);
            app.strokeWeight(1);
            app.rectMode(app.CENTER);
            app.rect(this.turretXCoordinate, this.turretYCoordinate-(this.tankHeight*6), this.turretWidth, this.tankHeight*4);
            app.rectMode(app.CORNER);
            app.strokeWeight(this.turretWidth);
            app.line(this.turretXCoordinate-this.tankHeight, this.turretYCoordinate-(this.tankHeight*5), this.turretXCoordinate, this.turretYCoordinate-(this.tankHeight*4));
            app.line(this.turretXCoordinate+this.tankHeight, this.turretYCoordinate-(this.tankHeight*5), this.turretXCoordinate, this.turretYCoordinate-(this.tankHeight*4));
            this.arrowCount += 1;
        }

        if (this.arrowCount == 60) { // remove after two seconds
            this.arrowOn = false;
        }
    }

    public void setArrowOn(boolean value) {
        this.arrowOn = value;
    }

    public void setArrowCount(int num) {
        this.arrowCount = num;
    }

    public void rotateTurretLeft() { // up arrow
        if (this.angle-(float)0.2>=(float)(-Math.PI/2)) {
            this.angle -= (float)0.2;
        }
    }

    public void rotateTurretRight() { // down arrow
        if (this.angle<=(float)Math.PI/2-(float)0.2) {
            this.angle += (float)0.2;
        }
    }

    public void moveTankLeft() { // left arrow
        if (this.xCoordinate-this.speed >= 0) {
            this.xCoordinate -= this.speed;
            this.tankCentreX = this.xCoordinate + (this.tankWidth/2);
        }
    }

    public void moveTankRight() { // right arrow
        if (this.xCoordinate <= 864-32-this.speed) {
            this.xCoordinate += this.speed;
            this.tankCentreX = this.xCoordinate + (this.tankWidth/2);
        }
    }


    public PShape getTank() {
        return this.tank;
    }


    public float getAngle() {
        return this.angle;
    }

    public Player getPlayer() {
        return this.player;
    }

    public float getTurretXCoordinate() {
        return this.turretXCoordinate;
    }

    public float getTurretYCoordinate() {
        return this.turretYCoordinate;
    }

    public void setProjectile() {
        float projectileAngle = 0;
        float projectileXCoordinate = 0;
        float projectileYCoordinate = 0;
        float power = this.player.getPower();
        float diameter = this.turretWidth * 2;

        if (this.angle < 0) {
            projectileAngle = -this.angle - (float)Math.PI/2;
        } else {
            projectileAngle = (float)Math.PI/2 - this.angle;
        }

        if (projectileAngle < 0) {
            projectileXCoordinate = this.turretXCoordinate - (this.tankHeight * (float)Math.cos(Math.abs(projectileAngle)));
        } else if (projectileAngle > 0){
            projectileXCoordinate = this.turretXCoordinate + (this.tankHeight * (float)Math.cos(Math.abs(projectileAngle)));
        } else {
            if (this.angle < 0) {
                projectileXCoordinate = this.turretXCoordinate - this.tankHeight;
            } else if (this.angle > 0) {
                projectileXCoordinate = this.turretXCoordinate + this.tankHeight;
            }
        }

        projectileYCoordinate = this.turretYCoordinate - (this.tankHeight * (float)Math.sin(Math.abs(projectileAngle)));

        projectile = new Projectile(this.app, this, projectileXCoordinate, projectileYCoordinate, projectileAngle, power, diameter, this.colourScheme);
    }

    public Projectile getProjectile() {
        return this.projectile;
    }

    public void clearProjectile() {
        this.projectile = null;
    }

    public float getTankCentreX() {
        return this.tankCentreX;
    }

    public float getTankCentreY() {
        return this.tankCentreY;
    }

    public boolean insideExplosion(float tankCentreX, float tankCentreY, float explosionCentreX, float explosionCentreY) { // check if another tank is inside explosion
        return (((tankCentreX-explosionCentreX)*(tankCentreX-explosionCentreX))+((tankCentreY-explosionCentreY)*(tankCentreY-explosionCentreY)) <= 30 * 30);
    }

    public float damage(float tankCentreX, float tankCentreY, float explosionCentreX, float explosionCentreY) { // calculate distance from explosion
        return ((30-(float)Math.sqrt(((tankCentreX-explosionCentreX)*(tankCentreX-explosionCentreX)) + ((tankCentreY-explosionCentreY)*(tankCentreY-explosionCentreY))))/30) * 60;
        // 30-distance from centre of explosion divided by 30, then multipled by 60; i.e. percentage of damage * 60
    }
}

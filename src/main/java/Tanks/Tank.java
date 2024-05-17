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
    private float explosionRadius = 0;
    private boolean explosionOut = false;
    private boolean insideExplosion = false;

    /**
     * Constructor for Tank object.
     * 
     * @param xCoordinate is the x-coordinate of a tank, which updates when the LEFT and RIGHT keys are pressed.
     * @param yCoordinate is the y-coordinate of a tank, which is grounded to the terrain, unless the tank is falling after the terrain below it falls.
     * @param colourScheme is the colour scheme of the Player object which the Tank object belongs to.
     * @param app is the App object that we will pass through to allow for drawing + more implementations of the PApplet library.
     * @param levelObj is the Level object which the Tank object belongs to.
     * @param player is the Player object which the Tank object belongs to.
     */
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

    // no Javadoc comments required for setters and getters
    public int getTankWidth() {
        return this.tankWidth;
    }

    public int getTankHeight() {
        return this.tankHeight;
    }

    public int getTurretWidth() {
        return this.turretWidth;
    }

    public void setXCoordinate(float num) {
        this.xCoordinate = num;
    }

    public void setYCoordinate(float num) {
        this.yCoordinate = num;
    }

    public float getXCoordinate() {
        return this.xCoordinate;
    }

    public float getYCoordinate() {
        return this.yCoordinate;
    }

    public void setArrowOn(boolean value) {
        this.arrowOn = value;
    }

    public void setArrowCount(int num) {
        this.arrowCount = num;
    }


    public PShape getTank() {
        return this.tank;
    }

    public void setAngle(float angle) {
        this.angle = angle;
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

    public void setExplosionOut(boolean value) {
        this.explosionOut = value;
    }

    public boolean getExplosionOut() {
        return this.explosionOut;
    }

    public float getExplosionRadius() {
        return this.explosionRadius;
    }

    public void setExplosionRadius(float value) {
        this.explosionRadius = 0;
    }

    /**
     * Increases the y-coordinate when the tank is descending, either on a parachute or without a parachute.
     * 
     * @param increase is the number of pixels we want our y-coordinate to increase by.
     */
    public void increaseYCoordinate(float increase) { // use for descending
        if (this.player.getParachuteOn() == true) {
            this.yCoordinate += increase;
        }
    }
    
    /**
     * Keeps the tank on the terrain.
     */
    public void groundTank() { // handling the logic for where to ground the tanks
        this.movingAverages = this.levelObj.getBackgroundTerrain().getMovingAveragePoints();
        this.yCoordinate = this.movingAverages[(int)this.xCoordinate+(this.tankWidth/2)]-(this.tankHeight);
        this.tankCentreY = this.yCoordinate + (this.tankHeight/2);
    }

    /**
     * Draws the tank. Will draw the shield if it is being hit and has shields left to use.
     */
    public void drawTank() { // draw tank
        if (this.player.getPlayerAlive() == true) {
            app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
            app.strokeWeight(4);
            app.stroke(0);
            app.rotate(0);
            this.tank = app.createShape(app.RECT, this.xCoordinate, this.yCoordinate, this.tankWidth, this.tankHeight);
            app.shape(this.tank);
            this.player.drawShield();
        }
    }

    /**
     * Draws the turret and handles its rotation when the UP and DOWN keys are pressed.
     */
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

    /**
     * Draws the arrow indicating the player's turn. Disappears after 2 seconds.
     */
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

    /**
     * Rotates the turret left when the UP arrow is pressed. Call in the keyPressed() function of the App class.
     */
    public void rotateTurretLeft() { // up arrow
        if (this.angle-(float)0.2>=(float)(-Math.PI/2)) {
            this.angle -= (float)0.2;
        }
    }

    /**
     * Rotates the turret right when the DOWN arrow is pressed. Call in the keyPressed() function of the App class.
     */
    public void rotateTurretRight() { // down arrow
        if (this.angle<=(float)Math.PI/2-(float)0.2) {
            this.angle += (float)0.2;
        }
    }

    /**
     * Moves the tank left when the LEFT arrow is pressed by updating the x-coordinate and fuel. Call in the keyPressed() function of the App class.
     */
    public void moveTankLeft() { // left arrow
        if (this.xCoordinate-this.speed >= 0 && this.player.getFuel()>=this.speed) {
            this.xCoordinate -= this.speed;
            this.tankCentreX = this.xCoordinate + (this.tankWidth/2);

            int currentFuel = this.player.getFuel();
            this.player.setFuel(currentFuel-this.speed);
        }
    }

    /**
     * Moves the tank right when the RIGHT arrow is pressed by updating the x-coordinate and fuel. Call in the keyPressed() function of the App class.
     */
    public void moveTankRight() { // right arrow
        if (this.xCoordinate <= 864-30-this.speed && this.player.getFuel()>=this.speed) {
            this.xCoordinate += this.speed;
            this.tankCentreX = this.xCoordinate + (this.tankWidth/2);

            int currentFuel = this.player.getFuel();
            this.player.setFuel(currentFuel-this.speed);
        }
    }

    /**
     * Creates a new Projectile object each time the spacebar is pressed.
     */
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

        this.projectile = new Projectile(this.app, this, projectileXCoordinate, projectileYCoordinate, projectileAngle, power, diameter, this.colourScheme);
    }

    /**
     * Returns true if another tank is inside the projectile's explosion.
     * 
     * @param tankCentreX is the x-coordinate of another tank's centre.
     * @param tankCentreY is the y-coordinate of another tank's centre.
     * @param explosionCentreX is the x-coordinate of the projectile's explosion centre.
     * @param explosionCentreY is the y-coordinate of the projectile's explosion centre.
     * @return true if another tank is inside the explosion circle, false if otherwise.
     */
    public boolean insideExplosion(float tankCentreX, float tankCentreY, float explosionCentreX, float explosionCentreY) { // check if another tank is inside explosion
        return (((tankCentreX-explosionCentreX)*(tankCentreX-explosionCentreX))+((tankCentreY-explosionCentreY)*(tankCentreY-explosionCentreY)) <= 30 * 30);
    }

    /**
     * Calculates the damange from the explosion. This is proportional to the distance from the explosion.
     * 
     * @param tankCentreX is the x-coordinate of another tank's centre.
     * @param tankCentreY is the y-coordinate of another tank's centre.
     * @param explosionCentreX is the x-coordinate of the projectile's explosion centre.
     * @param explosionCentreY is the y-coordinate of the projectile's explosion centre.
     * @return 30-distance from centre of explosion divided by 30, then multipled by 60; i.e. fraction of damage * 60
     */
    public float damage(float tankCentreX, float tankCentreY, float explosionCentreX, float explosionCentreY) { // calculate damage from explosion proportional to distance from explosion
        return ((30-(float)Math.sqrt(((tankCentreX-explosionCentreX)*(tankCentreX-explosionCentreX)) + ((tankCentreY-explosionCentreY)*(tankCentreY-explosionCentreY))))/30) * 60;
        // 30-distance from centre of explosion divided by 30, then multipled by 60; i.e. fraction of damage * 60
    }

    /**
     * Draws explosion of the tank when it dies.
     * 
     * @return true if executed, false if otherwise.
     */
    public boolean drawExplosion() { // when health is 0
        if (this.explosionOut == true) {
            app.stroke(255, 0, 0);
            app.fill (255, 0, 0);
            app.ellipse(this.tankCentreX, this.tankCentreY, this.explosionRadius*2, this.explosionRadius*2);
            app.stroke(255, 102, 0);
            app.fill(255, 102, 0);
            app.ellipse(this.tankCentreX, this.tankCentreY, this.explosionRadius, this.explosionRadius);
            app.stroke(255, 255, 0);
            app.fill(255, 255, 0);
            app.ellipse(this.tankCentreX, this.tankCentreY, this.explosionRadius*2/5, this.explosionRadius*2/5);
            this.expandExplosion();

            return true;
        }

        return false;
    }

    /**
     * Expands the explosion of the tank as it dies.
     */
    public void expandExplosion() {
        this.explosionRadius += 5; 
    }
}

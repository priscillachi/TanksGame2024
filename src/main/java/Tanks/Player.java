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

public class Player { // handles players and logic
    private App app;
    private int xCoordinate;
    private int yCoordinate;
    private int[] colourScheme;
    private Tank tank;
    private Level levelObj;
    private HealthPower healthPower;
    private String player;
    private int score;
    private int parachute;
    private int fuel;
    private boolean playerAlive=true;
    private boolean gainScore=false;
    private boolean parachuteOn=false;
    private int shield; // extension
    private boolean shieldOn = false; // extension
    private float shieldCount = 0; // extension
    private boolean belowMap = false;


    /**
     * Constructor for Player class. Keeps track of everything a player has (e.g. score, parachutes, fuel, shield, etc.) and contains a Tank object.
     * 
     * @param app is the App object that we will pass through to allow for drawing + more implementations of the PApplet library.
     * @param levelObj is the Level object which a Player object belongs to.
     * @param xCoordinate is the x-coordinate of a player on the screen.
     * @param yCoordinate is the y-coordinate of a player on the screen.
     * @param colourScheme is the RGB colour scheme of a player.
     * @param player is the String for the player's name, i.e. "A" for Player A, "B" for Player B, etc.
     */
    public Player(App app, Level levelObj, int xCoordinate, int yCoordinate, int[] colourScheme, String player) {
        this.app = app;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.levelObj = levelObj;
        this.colourScheme = colourScheme;
        this.player = player;
        this.score = 0;
        this.parachute = 3;
        this.fuel = 250;
        this.shield=0; // extension
    }

    // no need for Javadoc comments for getters and setters.
    public int[] getColourScheme() {
        return this.colourScheme;
    }

    public String getPlayerString() {
        return this.player;
    }

    public boolean getPlayerAlive() {
        return this.playerAlive;
    }

    public void setPlayerAlive(boolean value) {
        this.playerAlive = value;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Tank getTank() {
        return this.tank;
    }

    public void setHealthPower() {
        this.healthPower = new HealthPower(this.app, this.levelObj, this);
    }

    public HealthPower getHealthPower() {
        return this.healthPower;
    }

    public int getHealth() {
        return this.healthPower.getHealth();
    }

    public float getPower() {
        return this.healthPower.getPower();
    }

    public void setShield(int num) { // extension
        this.shield = num;
    }

    public int getShield() { // extension
        return this.shield;
    }

    public int getScore() {
        return this.score;
    }

    public int getFuel() {
        return this.fuel;
    }

    public int getParachute() {
        return this.parachute;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    /**
     * While the parachute feature is not an extension, I have implemented an extension feature where the number of parachutes increases when the key 'p' is pressed.
     * 
     * @param parachute is the number of parachutes that we want our parachute number to set or update to.
     */
    public void setParachute(int parachute) {
        this.parachute = parachute;
    }

    public void setParachuteOn(boolean value) { // if true: call when drawing parachute
        this.parachuteOn = value;
    }

    public boolean getParachuteOn() {
        return this.parachuteOn;
    }

    public boolean getBelowMap() {
        return this.belowMap;
    }

    public void setBelowMap(boolean value) {
        this.belowMap = value;
    }

    public void setShieldOn(boolean value) { // call when tanks are being hit and they still have shields left
        this.shieldOn = value;
    }

    public void setGainScore(boolean value) { // set when score is to be gained
        this.gainScore = value;
    }

    public void updateHealth(int health) {
        this.healthPower.updateHealth(health);
    }

    public void updatePower(int power) {
        this.healthPower.updatePower(power);
    }

    /**
     * Creates a Tank object at the positions of the player.
     */
    public void setTank() { // create Tank object
        this.tank = new Tank((float)this.xCoordinate, (float)this.yCoordinate, this.colourScheme, this.app, this.levelObj, this); 
    }

    /**
     * Displays the text which states which player the turn belongs to.
     */
    public void displayPlayerText() { // what the method says
        int xCoordinate = 15;
        int yCoordinate = 30;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String printOut = String.format("Player %s's turn", this.player);
        app.text(printOut, xCoordinate, yCoordinate);
    }

    /**
     * Increases the player's score. Call when the player has hit another tank.
     * 
     * @param increase is the number we want our score to increase by.
     */
    public void increaseScore(int increase) {
        int newScore = increase + this.score;
        if (this.gainScore == true) {
            this.score = newScore;

            if (this.score == newScore) {
                this.gainScore = false;
            }
        }

        if (this.score == newScore) {
            this.gainScore = false;
        }
    }

    /**
     * Displays the amount of fuel left in the top left corner.
     */
    public void displayFuel() { // display fuel amount in top left corner
        int xCoordinate = 210;
        int yCoordinate = 30;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String fuelNum = String.format("%d", this.fuel);
        app.text(fuelNum, xCoordinate, yCoordinate);

    }

    /**
     * Displays the number of parachutes left in the top left corner.
     */
    public void displayParachute() { // display parachute number in top left corner
        int xCoordinate = 210;
        int yCoordinate = 58;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String parachuteNum = String.format("%d", this.parachute);
        app.text(parachuteNum, xCoordinate, yCoordinate);
    }

    /**
     * Displays the number of shields left in the top left corner. This is an extension feature of my program.
     */
    public void displayShield() { // display shield number in top left corner - extension
        int xCoordinate = 210;
        int yCoordinate = 86;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String parachuteNum = String.format("%d", this.shield);
        app.text(parachuteNum, xCoordinate, yCoordinate);
    }

    /**
     * Draws the shield, which disappears after 25 frames. This is an extension feature of my program.
     */
    public void drawShield() { // draw shield on tank - extension; disappears after 25 frames (use counter to keep track)
        if (this.shieldOn == true) {
            app.stroke(0);
            app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2], 50);
            app.ellipse(this.tank.getTankCentreX(), this.tank.getTankCentreY(), 45, 45);
            this.shieldCount += 1;
        }

        if (this.shieldCount == 25) {
            this.shieldOn = false;
            this.shield -= 1;
            this.shieldCount = 0;
        }
    }

    /**
     * Draws the parachute on the tank. While the parachute feature is not an extension, I have implemented an extension feature where the number of parachutes increases when the key 'p' is pressed.
     */
    public void drawParachute() { //draw parachute on tank
        if (this.parachuteOn==true) {
            PImage parachute = this.levelObj.getParachutImage();
            app.image(parachute, this.tank.getXCoordinate()-10, this.tank.getYCoordinate()-(3*this.tank.getTankHeight()), this.tank.getTankWidth()+20, 3*this.tank.getTankHeight());
        }
    }
}

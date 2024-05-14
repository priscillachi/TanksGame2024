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
    }

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


    public void setTank() { // create Tank object
        this.tank = new Tank((float)this.xCoordinate, (float)this.yCoordinate, this.colourScheme, this.app, this.levelObj, this); 
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

    public void updateHealth(int health) {
        this.healthPower.updateHealth(health);
    }

    public void updatePower(int power) {
        this.healthPower.updatePower(power);
    }

    public void setAIPlayers() {
        ;
    }

    public void displayPlayerText() { // what the method says
        int xCoordinate = 15;
        int yCoordinate = 30;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String printOut = String.format("Player %s's turn", this.player);
        app.text(printOut, xCoordinate, yCoordinate);
    }

    public void setScore(int score) {
        this.score = score;
    }

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

    public int getScore() {
        return this.score;
    }

    public int getFuel() {
        return this.fuel;
    }

    public int getParachute() {
        return this.parachute;
    }

    public void updateFuel(int fuel) {
        this.fuel = fuel;
    }

    public void setParachute(int parachute) {
        this.parachute = parachute;
    }

    public void displayFuel() { // display parachute symbol and amount in top left corner
        int xCoordinate = 210;
        int yCoordinate = 30;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String fuelNum = String.format("%d", this.fuel);
        app.text(fuelNum, xCoordinate, yCoordinate);

    }

    public void displayParachute() { // display parachute symbol and number in top left corner
        int xCoordinate = 210;
        int yCoordinate = 57;

        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        String parachuteNum = String.format("%d", this.parachute);
        app.text(parachuteNum, xCoordinate, yCoordinate);
    }

    public void setGainScore(boolean value) {
        this.gainScore = value;
    }

    public void drawParachute() { //draw parachute on tank
        if (this.parachuteOn==true) {
            PImage parachute = this.levelObj.getParachutImage();
            app.image(parachute, this.tank.getXCoordinate()-10, this.tank.getYCoordinate()-(3*this.tank.getTankHeight()), this.tank.getTankWidth()+20, 3*this.tank.getTankHeight());
        }
    }

    public void setParachuteOn(boolean value) {
        this.parachuteOn = value;
    }

    public boolean getParachuteOn() {
        return this.parachuteOn;
    }
}

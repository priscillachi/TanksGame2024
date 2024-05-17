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

public class HealthPower {
    
    private Player player;
    private Level level;
    private App app;
    private int health;
    private float power;
    private int barWidth;
    private int barHeight;
    private int[] colourScheme;
    private float healthLength;
    private float powerLength;
    private boolean loseHealth=false;

    /**
     * Constructor for HealthPower class. Keeps track of each player's health and power and the GUI for them.
     * 
     * @param app is the App object that we will pass through to allow for drawing + more implementations of the PApplet library.
     * @param level is the Level object which a HealthPower object belongs to.
     * @param player is the Player object which a HealthPower object belongs to.
     */
    public HealthPower(App app, Level level, Player player) {
        this.app = app;
        this.level = level;
        this.player = player;
        this.health = 100;
        this.power = 50;
        this.barWidth = 175;
        this.barHeight = 25;
        this.colourScheme = player.getColourScheme();
        this.healthLength = ((float)this.health/(float)100) * (float)this.barWidth;
        this.powerLength = ((float)this.power/(float)100) * (float)this.barWidth;
    }

    // no Javadoc comments required for setters and getters
    public int getHealth() {
        return this.health;
    }

    public float getPower() {
        return this.power;
    }

    public void setLoseHealth(boolean value) {
        this.loseHealth = value;
    }

    public boolean getLoseHealth() {
        return this.loseHealth;
    }

    /**
     * Draw health bar at the top of the screen.
     * 
     * @return true if executed, false if otherwise.
     */
    public boolean drawHealthBar() { // what the method says
        try{
            int xCoordinate = ((864*3)/5) - (this.barWidth/2);
            int yCoordinate = 10;
    
            // fill in
            app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
            app.rect(xCoordinate, yCoordinate, this.healthLength, this.barHeight);
    
            app.strokeWeight(5);
            app.stroke(0); // black box
            app.line(xCoordinate + this.powerLength, yCoordinate, xCoordinate + this.barWidth, yCoordinate);
            app.line(xCoordinate + this.powerLength, yCoordinate + this.barHeight, xCoordinate + this.barWidth, yCoordinate + this.barHeight);
            app.line(xCoordinate + this.powerLength, yCoordinate, xCoordinate + this.powerLength, yCoordinate + this.barHeight);
            app.line(xCoordinate + this.barWidth, yCoordinate, xCoordinate + this.barWidth, yCoordinate + this.barHeight);
    
            app.strokeWeight(5);
            app.stroke(87); // grey box
            app.line(xCoordinate, yCoordinate, xCoordinate + this.powerLength, yCoordinate);
            app.line(xCoordinate, yCoordinate + this.barHeight, xCoordinate + this.powerLength, yCoordinate + this.barHeight);
            app.line(xCoordinate, yCoordinate, xCoordinate, yCoordinate + this.barHeight);
            app.line(xCoordinate + this.powerLength, yCoordinate, xCoordinate + this.powerLength, yCoordinate + this.barHeight);

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Display how much health and power a player has.
     * 
     * @return true if executed, false if otherwise.
     */
    public boolean displayHealthPowerText() { // what the method says
        try {
            int xCoordinate = ((864*3)/5) - (this.barWidth/2);
            int yCoordinate = 10;
            
            app.strokeWeight(1);
            app.textSize(18);
            app.fill(0);
            app.text("Health:", xCoordinate-(this.barWidth/2)+14, yCoordinate+this.barHeight-5); // word health next to bar
            app.text("Power:", xCoordinate-(this.barWidth/2)+14, yCoordinate+this.barHeight+22); // word power next to bar
            app.text((int)this.power, xCoordinate, yCoordinate+this.barHeight+22); // power number next to bar
            app.text(this.health, xCoordinate + this.barWidth + 10, yCoordinate+this.barHeight-5);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Update the health amount. If the health falls below the current power amount, update the power such that it is equal to the health, as the power is at most equal to the health.
     * 
     * @param health is the health amount that we want to update our health value to.
     * @return true if executed, false if otherwise.
     */
    public boolean updateHealth(int health) { 
        try {
            this.health = health;
            this.healthLength = ((float)this.health/(float)100) * (float)this.barWidth;
    
            if (this.health <= this.power) {
                this.power = this.health;
                this.powerLength = ((float)this.power/(float)100) * (float)this.barWidth;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Decreases the health amount. Call when a player has been hit by a projectile.
     * 
     * @param decrease is the amount we want our health to decrease by.
     * @return true if executed, false if otherwise.
     */
    public boolean decreaseHealth(int decrease) {
            int newHealth = this.health - (int)decrease;
            if (this.loseHealth==true) {
                this.updateHealth(newHealth);
    
                if (this.health==newHealth) {
                    this.loseHealth = false;
                }

                return true;
            }

            return false;
    }

    /**
     * Update the power amount. It cannot exceed the health amount.
     * 
     * @param power is the amount of power we want to update our power to.
     * @return true if excecuted, false if otherwise.
     */
    public boolean updatePower(float power) {
        if (power <= this.health) {
            this.power = power;
            this.powerLength = ((float)this.power/(float)100) * (float)this.barWidth;

            return true;
        }

        return false;
    }

    /**
     * Increase the power by 1.2 when we press the 'w' key.
     * 
     * @return true if excecuted, false if otherwise.
     */
    public boolean powerIncrease() { // for keyPressed
        if (this.power <= this.health) {
            this.updatePower(this.power+(float)1.2);

            return true;
        }

        return false;
    }

    /**
     * Decrease the power by 1.2 when we press the 's' key.
     * 
     * @return true if excecuted, false if otherwise.
     */
    public boolean powerDecrease() { // for keyPressed
        if (this.power >= 0) {
            this.updatePower(this.power-(float)1.2);

            return true;
        }

        return false;
    }
}

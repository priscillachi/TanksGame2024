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

    public void drawHealthBar() { // what the method says
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
    }

    public void displayHealthPowerText() { // what the method says
        int xCoordinate = ((864*3)/5) - (this.barWidth/2);
        int yCoordinate = 10;
        
        app.strokeWeight(1);
        app.textSize(18);
        app.fill(0);
        app.text("Health:", xCoordinate-(this.barWidth/2)+14, yCoordinate+this.barHeight-5); // word health next to bar
        app.text("Power:", xCoordinate-(this.barWidth/2)+14, yCoordinate+this.barHeight+22); // word power next to bar
        app.text((int)this.power, xCoordinate, yCoordinate+this.barHeight+22); // power number next to bar
        app.text(this.health, xCoordinate + this.barWidth + 10, yCoordinate+this.barHeight-5);
    }

    public void updateHealth(int health) { 
        this.health = health;
        this.healthLength = ((float)this.health/(float)100) * (float)this.barWidth;

        if (this.health <= this.power) {
            this.power = this.health;
            this.powerLength = ((float)this.power/(float)100) * (float)this.barWidth;
        }
    }

    public int getHealth() {
        return this.health;
    }

    public void updatePower(float power) {
        if (power <= this.health) {
            this.power = power;
            this.powerLength = ((float)this.power/(float)100) * (float)this.barWidth;
        }
    }

    public void powerIncrease() {
        if (this.power <= this.health) {
            this.updatePower(this.power+(float)1.2);
        }
    }

    public void powerDecrease() {
        if (this.power >= 0) {
            this.updatePower(this.power-(float)1.2);
        }
    }

    public float getPower() {
        return this.power;
    }
}

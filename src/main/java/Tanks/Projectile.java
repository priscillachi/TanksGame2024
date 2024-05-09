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


public class Projectile {
    
    private App app;
    private float xCoordinate;
    private float yCoordinate;
    private float velocity;
    private float xVelocity;
    private float yVelocity;
    private float diameter;
    private int wind;
    private float angle;
    private float power;
    private float gravity;
    private int[] colourScheme;
    private PShape projectile;
    
    public Projectile(App app, float xCoordinate, float yCoordinate, float angle, float power, float diameter, int[] colourScheme) {
        this.app = app;
        this.gravity = (float)0.24;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.angle = angle;
        this.power = power;
        this.diameter = diameter;
        this.wind = this.app.currentLevel.getWind();
        this.colourScheme = colourScheme;
        this.setVelocity();
    }

    public void setVelocity() {
        this.velocity = (float)((0.16 * this.power) + 2);
        this.xVelocity = this.velocity * (float)Math.cos(Math.abs(this.angle));
        this.yVelocity = this.velocity * (float)Math.sin(Math.abs(this.angle));
    }

    public void updateVelocityCoordinates() {

        this.yVelocity -= this.gravity;

        if ((this.wind < 0 && this.angle < 0) || (this.wind >= 0 && this.angle >= 0)) {
            this.xVelocity += (float)Math.abs(this.wind * 0.001);
        } else if ((this.wind >= 0 && this.angle < 0) || (this.wind < 0 && this.angle >= 0)) {
                this.xVelocity -= (float)Math.abs(this.wind * 0.001);
        }

        if (this.angle < 0) {
            this.xCoordinate -= this.xVelocity;
        } else {
            this.xCoordinate += this.xVelocity;
        }

        this.yCoordinate -= this.yVelocity;
    }

    public void drawProjectile() {
        projectile = app.createShape(app.ELLIPSE, xCoordinate, yCoordinate, diameter, diameter);
        projectile.setFill(app.color(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]));
        projectile.setStroke(app.color(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]));
        app.shape(projectile);
        //app.stroke(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
        //app.fill(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]);
        //app.ellipse(xCoordinate, yCoordinate, diameter, diameter);
        this.updateVelocityCoordinates();
    }

    public void clearProjectile() {
        projectile = null;
    }

    public void drawExplosion() {
        ;
    }

    public float getXCoordinate() {
        return this.xCoordinate;
    }

    public float getYCoordinate() {
        return this.yCoordinate;
    }

}

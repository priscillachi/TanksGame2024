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
    private Tank tank;
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
    private boolean projectileShot = false;
    private boolean explosionOut = false;
    private float explosionRadius = 0;
    
    public Projectile(App app, Tank tank, float xCoordinate, float yCoordinate, float angle, float power, float diameter, int[] colourScheme) {
        this.app = app;
        this.tank = tank;
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
        this.xVelocity = this.velocity * (float)Math.cos(Math.abs(this.angle)); // get x component
        this.yVelocity = this.velocity * (float)Math.sin(Math.abs(this.angle)); // get y component
    }

    public void updateVelocityCoordinates() {

        this.yVelocity -= this.gravity; // minus gravity

        if ((this.wind < 0 && this.angle < 0) || (this.wind >= 0 && this.angle >= 0)) {
            this.xVelocity += (float)Math.abs(this.wind * 0.001); // if angle and wind are in different direction
        } else if ((this.wind >= 0 && this.angle < 0) || (this.wind < 0 && this.angle >= 0)) {
            this.xVelocity -= (float)Math.abs(this.wind * 0.001); // if angle and wind are in same direction
        }

        if (this.angle < 0) {
            this.xCoordinate -= this.xVelocity; // minus velocity if angle < 0
        } else if (this.angle > 0) {
            this.xCoordinate += this.xVelocity; // add velocity if angle > 0
        } else { // if angle = 0, check turret angle
            if (this.tank.getAngle() < 0) {
                this.xCoordinate -= this.xVelocity;
            } else if (this.tank.getAngle() > 0) {
                this.xCoordinate += this.xVelocity;
            }
        }

        this.yCoordinate -= this.yVelocity; // minus velocity
    }

    public void drawProjectile() {
        if (this.projectileShot == true) {
            projectile = app.createShape(app.ELLIPSE, xCoordinate, yCoordinate, diameter, diameter);
            projectile.setFill(app.color(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]));
            projectile.setStroke(app.color(this.colourScheme[0], this.colourScheme[1], this.colourScheme[2]));
            app.shape(projectile);
            this.updateVelocityCoordinates();
        }
    }

    public void drawExplosion() {

        if (this.explosionOut == true) {
            app.stroke(255, 0, 0);
            app.fill (255, 0, 0);
            app.ellipse(this.xCoordinate, this.yCoordinate, this.explosionRadius*2, this.explosionRadius*2);
            app.stroke(255, 102, 0);
            app.fill(255, 102, 0);
            app.ellipse(this.xCoordinate, this.yCoordinate, this.explosionRadius, this.explosionRadius);
            app.stroke(255, 255, 0);
            app.fill(255, 255, 0);
            app.ellipse(this.xCoordinate, this.yCoordinate, this.explosionRadius*2/5, this.explosionRadius*2/5);
            this.expandExplosion();
        }
    }

    public void expandExplosion() {
        this.explosionRadius += 5; // 30 pixels over 0.2s = 150 pixels/s = 150 pixels per 30 frames = 5 pixels per frame
    }

    public float getExplosionRadius() {
        return this.explosionRadius;
    }

    public float getXCoordinate() {
        return this.xCoordinate;
    }

    public float getYCoordinate() {
        return this.yCoordinate;
    }

    public void setProjectileShot(boolean value) {
        this.projectileShot = value;
    }

    public boolean getProjectileShot() {
        return this.projectileShot;
    }

    public void setExplosionOut(boolean value) {
        this.explosionOut = value;
    }

    public boolean getExplosionOut() {
        return this.explosionOut;
    }

    public boolean insideExplosion(float x, float y) {
        return (((x-this.xCoordinate)*(x-this.xCoordinate))+((y-this.yCoordinate)*(y-this.yCoordinate)) <= this.explosionRadius * this.explosionRadius);
    }

}

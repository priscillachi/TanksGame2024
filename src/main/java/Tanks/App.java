package Tanks;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONTokener;

import com.jogamp.newt.event.KeyListener;

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


public class App extends PApplet {

    public static final int CELLSIZE = 32; //8;
    public static final int CELLHEIGHT = 32;

    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; //CELLSIZE*BOARD_WIDTH;
    public static int HEIGHT = 640; //BOARD_HEIGHT*CELLSIZE+TOPBAR;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;

    public static final int INITIAL_PARACHUTES = 3;

    public static final int FPS = 30;

    public String configPath;

    public static Random random = new Random();

    public JSONObject jsonData;
    public JSONArray levelsData;
    public JSONObject playersData;

    public Level level1 = new Level(this, 1);
    public Level level2 = new Level(this, 2);
    public Level level3 = new Level(this, 3);

    public Level currentLevel;
    public float[] terrainMovingAveragePoints;

    //private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    //private int index = 0;
    private Projectile projectile;
    private boolean projectileShot = false;
    private float projectileXCoordinate;
    private float projectileYCoordinate;
    private int playersNumber;
    private int playerTurn;
	
	// Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }


    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
		//See PApplet javadoc:
		
        jsonData = loadJSONObject(configPath);
        levelsData = jsonData.getJSONArray("levels");
        playersData = jsonData.getJSONObject("player_colours");

        this.currentLevel = level1;

        this.currentLevel.getBackgroundTerrain().setTerrainMatrix();
        this.currentLevel.getBackgroundTerrain().calculateMovingAverage();
        terrainMovingAveragePoints = this.currentLevel.getBackgroundTerrain().getMovingAveragePoints();
        this.currentLevel.getBackgroundTerrain().setForegroundColour();
        this.currentLevel.setPlayers();
        this.currentLevel.sortPlayers();
        playersNumber = this.currentLevel.getPlayersObj().size();
        playerTurn = 0;
        this.currentLevel.setTrees();
        
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){

        if (key==CODED) {
            if (keyCode == UP) {
                this.currentLevel.getTurn().getTank().rotateTurretLeft();
            } else if (keyCode == DOWN) {
                this.currentLevel.getTurn().getTank().rotateTurretRight();
            } else if (keyCode == LEFT) {
                this.currentLevel.getTurn().getTank().moveTankLeft();
            } else if (keyCode == RIGHT) {
                this.currentLevel.getTurn().getTank().moveTankRight();
            }
        } else {
            if (key == 'w' || key == 'W') {
                this.currentLevel.getTurn().getHealthPower().powerIncrease();
            } else if (key == 's' || key == 'S') {
                this.currentLevel.getTurn().getHealthPower().powerDecrease();
            } else if (key == ' ') {
                float turretAngle = this.currentLevel.getTurn().getTank().getAngle();
                float angle = 0;
                float xCoordinate = 0;
                float yCoordinate = 0;
                float power = this.currentLevel.getTurn().getPower();
                float diameter = this.currentLevel.getTurn().getTank().getTurretWidth() * 2;
                int[] colourScheme = this.currentLevel.getTurn().getColourScheme();

                if (turretAngle < 0) {
                    angle = -turretAngle - (float)Math.PI/2;
                } else {
                    angle = (float)Math.PI/2 - turretAngle;
                }

                if (angle < 0) {
                    xCoordinate = this.currentLevel.getTurn().getTank().getTurretXCoordinate() - (this.currentLevel.getTurn().getTank().getTankHeight() * (float)Math.cos(Math.abs(angle)));
                } else {
                    xCoordinate = this.currentLevel.getTurn().getTank().getTurretXCoordinate() + (this.currentLevel.getTurn().getTank().getTankHeight() * (float)Math.cos(Math.abs(angle)));
                }
                yCoordinate = this.currentLevel.getTurn().getTank().getTurretYCoordinate() - (this.currentLevel.getTurn().getTank().getTankHeight() * (float)Math.sin(Math.abs(angle)));

                projectile = new Projectile(this, xCoordinate, yCoordinate, angle, power, diameter, colourScheme);
                this.projectileShot = true;
                this.playerTurn += 1;
            }
        }
        
    }

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased() {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport
    

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {

        if (this.playerTurn < this.playersNumber) {
            this.currentLevel.setTurn(this.currentLevel.getPlayersObj().get(playerTurn));
        }
        
        if (this.playerTurn == this.playersNumber) {
            this.playerTurn = 0;
        }
        
        this.currentLevel.setBackground();
        this.currentLevel.getBackgroundTerrain().setTerrain();

        for (int i=0; i<this.currentLevel.getTrees().size(); i++) {
            this.currentLevel.getTrees().get(i).groundTree();
            this.currentLevel.getTrees().get(i).drawTree();
        }

        for (int i=0; i<this.currentLevel.getPlayersObj().size(); i++) {
            this.currentLevel.getPlayersObj().get(i).getTank().drawTurret();
            this.currentLevel.getPlayersObj().get(i).getTank().drawTank();
            
        }

        //pushMatrix();
        //if (this.currentLevel.getTurn().getTank().getRotateTurret()==true); {
            //translate(this.currentLevel.getTurn().getTank().getTurretWidth()/2, this.currentLevel.getTurn().getTank().getTankHeight());
            //rotate(this.currentLevel.getTurn().getTank().getAngle());
        //}
        //this.currentLevel.getTurn().getTank().drawTurret();
        //popMatrix();
        //this.currentLevel.getTurn().getTank().setRotateTurret(false);

        this.currentLevel.getTurn().getHealthPower().drawHealthBar();
        this.currentLevel.getTurn().getHealthPower().displayHealthPowerText();
        this.currentLevel.getTurn().displayPlayerText();
        this.currentLevel.displayFuelParachute();
        this.currentLevel.displayWind();
        this.currentLevel.displayScoreboard();

        if (projectile != null && this.projectileShot == true) {
            projectile.drawProjectile();
            projectileXCoordinate = projectile.getXCoordinate();
            projectileYCoordinate = projectile.getYCoordinate();

            if (projectileXCoordinate >= 0 && projectileXCoordinate <= 864) {
                if (projectileYCoordinate >= this.terrainMovingAveragePoints[(int)(projectileXCoordinate)]) {
                    projectile.clearProjectile();
                    projectile = null;
                    projectileShot = false;
                }
            }
        }



        //----------------------------------
        //display HUD:
        //----------------------------------
        //TODO

        //----------------------------------
        //display scoreboard:
        //----------------------------------
        //TODO
        
		//----------------------------------
        //----------------------------------

        //TODO: Check user action
    }


    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}

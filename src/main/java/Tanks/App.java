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

    public Level currentLevel = level1;

    private boolean projectileShot = false;
    private int playersNumber;
    private int playerTurn = 0;
	
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

        this.level1.getBackgroundTerrain().setTerrainMatrix();
        this.level1.getBackgroundTerrain().calculateMovingAverage();
        this.level1.getBackgroundTerrain().setForegroundColour();
        this.level1.setPlayers();
        this.level1.sortPlayers();
        this.level1.setTrees();

        this.level2.getBackgroundTerrain().setTerrainMatrix();
        this.level2.getBackgroundTerrain().calculateMovingAverage();
        this.level2.getBackgroundTerrain().setForegroundColour();
        this.level2.setPlayers();
        this.level2.sortPlayers();
        this.level2.setTrees();

        this.level3.getBackgroundTerrain().setTerrainMatrix();
        this.level3.getBackgroundTerrain().calculateMovingAverage();
        this.level3.getBackgroundTerrain().setForegroundColour();
        this.level3.setPlayers();
        this.level3.sortPlayers();
        this.level3.setTrees();
        
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
                this.currentLevel.getTurn().getTank().setProjectile();
                this.currentLevel.getTurn().getTank().getProjectile().setProjectileShot(true);
                this.playerTurn += 1; // switch turns
                this.currentLevel.updateWind(); // change wind by + or - 5
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
        
        this.playersNumber = this.currentLevel.getAlivePlayers().size(); // switch turns
        if (this.playerTurn < this.playersNumber) {
            this.currentLevel.setTurn(this.currentLevel.getAlivePlayers().get(playerTurn));
        }
        
        if (this.playerTurn == this.playersNumber) { // when last player is reached, go back to first player
            this.playerTurn = 0;
        }
        
        this.currentLevel.setBackground();
        this.currentLevel.getBackgroundTerrain().setTerrain();

        for (int i=0; i<this.currentLevel.getTrees().size(); i++) { // draw trees every frame
            this.currentLevel.getTrees().get(i).groundTree();
            this.currentLevel.getTrees().get(i).drawTree();
        }

        for (int i=0; i<this.currentLevel.getAlivePlayers().size(); i++) { // draw tanks every frame
            this.currentLevel.getAlivePlayers().get(i).getTank().drawTurret();
            this.currentLevel.getAlivePlayers().get(i).getTank().drawTank();
            
        }

        this.currentLevel.getTurn().getHealthPower().drawHealthBar(); // draw healthbar
        this.currentLevel.getTurn().getHealthPower().displayHealthPowerText(); // display health and power amounts
        this.currentLevel.getTurn().displayPlayerText(); // display turn
        this.currentLevel.displayFuelParachute(); // draw parachute and fuel
        this.currentLevel.displayWind(); // draw wind
        this.currentLevel.displayScoreboard(); // draw scoreboard
        
        for (int i=0; i<this.currentLevel.getAlivePlayers().size(); i++) {
            if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile() != null &&
            this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getProjectileShot() == true) {
                this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().drawProjectile();

                float projectileXCoordinate = this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getXCoordinate();
                float projectileYCoordinate = this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getYCoordinate();

                if (projectileXCoordinate >= 0 && projectileXCoordinate <= 864) {

                    if (projectileYCoordinate >= this.currentLevel.getBackgroundTerrain().getMovingAveragePoints()[(int)(projectileXCoordinate)]) {
                        this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setExplosionOut(true);
                        this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setProjectileShot(false);
                    }

                }
            }

            if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile() != null && 
            this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getExplosionOut() == true) {
                this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().drawExplosion();

                if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getExplosionRadius() == 30) {
                    this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setExplosionOut(false);
                }
            }

            // calculate tank damage
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

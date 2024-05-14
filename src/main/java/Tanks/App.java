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
    private int playersNumber;
    private int playerTurn = 0;
	

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
		
        jsonData = loadJSONObject(configPath); // read JSON file
        levelsData = jsonData.getJSONArray("levels");
        playersData = jsonData.getJSONObject("player_colours");

        this.level1.getBackgroundTerrain().setTerrainMatrix();
        this.level1.getBackgroundTerrain().calculateMovingAverage();
        this.level1.getBackgroundTerrain().setForegroundColour();
        this.level1.setPlayers();
        this.level1.sortPlayers();
        this.level1.setTrees();
        this.level1.getAlivePlayers().get(this.playerTurn).getTank().setArrowOn(true); // arrow on for first tank
        this.playersNumber = this.currentLevel.getAlivePlayers().size();

        this.level2.getBackgroundTerrain().setTerrainMatrix();
        this.level2.getBackgroundTerrain().calculateMovingAverage();
        this.level2.getBackgroundTerrain().setForegroundColour();
        this.level2.setPlayers();
        this.level2.sortPlayers();
        this.level2.setTrees();
        this.level2.getAlivePlayers().get(this.playerTurn).getTank().setArrowOn(true); // arrow on for first tank
        this.playersNumber = this.currentLevel.getAlivePlayers().size();

        this.level3.getBackgroundTerrain().setTerrainMatrix();
        this.level3.getBackgroundTerrain().calculateMovingAverage();
        this.level3.getBackgroundTerrain().setForegroundColour();
        this.level3.setPlayers();
        this.level3.sortPlayers();
        this.level3.setTrees();
        this.level3.getAlivePlayers().get(this.playerTurn).getTank().setArrowOn(true); // arrow on for first tank
        this.playersNumber = this.currentLevel.getAlivePlayers().size();
        
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){

        if (key==CODED) {
            if (keyCode == UP) {
                this.currentLevel.getTurn().getTank().rotateTurretLeft();
            } 
            if (keyCode == DOWN) {
                this.currentLevel.getTurn().getTank().rotateTurretRight();
            } 
            if (keyCode == LEFT) {
                this.currentLevel.getTurn().getTank().moveTankLeft();
            } 
            if (keyCode == RIGHT) {
                this.currentLevel.getTurn().getTank().moveTankRight();
            }
        } else {
            if (key == 'w' || key == 'W') {
                this.currentLevel.getTurn().getHealthPower().powerIncrease();
            } 
            if (key == 's' || key == 'S') {
                this.currentLevel.getTurn().getHealthPower().powerDecrease();
            } 
            if (key == ' ') {
                this.currentLevel.getTurn().getTank().setProjectile();
                this.currentLevel.getTurn().getTank().getProjectile().setProjectileShot(true);
                if (this.playerTurn < this.playersNumber-1) {
                    this.playerTurn += 1; // switch turns
                } else if (this.playerTurn == this.playersNumber-1) {
                    this.playerTurn = 0;
                }

                if (this.currentLevel.getAlivePlayers().size() > 0) {
                    this.currentLevel.getAlivePlayers().get(this.playerTurn).getTank().setArrowOn(true); // arrow on for next tank
                    this.currentLevel.getAlivePlayers().get(this.playerTurn).getTank().setArrowCount(0);
                }
                this.currentLevel.updateWind(); // change wind by + or - 5
            }
            if (key == 'r') {
                if (this.currentLevel == level3 && this.playersNumber <= 1) {
                    this.level1 = new Level(this, 1); // reset level objects
                    this.level2 = new Level(this, 2);
                    this.level3 = new Level(this, 3);

                    this.currentLevel = level1; // assign currentLevel as level1
                    this.setup(); // call setup

                } else {
                    ; // repairs
                }
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

        this.playersNumber = this.currentLevel.getAlivePlayers().size(); // count number of players

        if (this.playerTurn < this.playersNumber) {
            this.currentLevel.setTurn(this.currentLevel.getAlivePlayers().get(playerTurn)); // handling switching turns
        } else if (this.playerTurn == this.playersNumber) { 
            this.playerTurn = 0; // when last player is reached, go back to first player
        }
        
        this.currentLevel.setBackground(); 
        this.currentLevel.getBackgroundTerrain().setTerrain();

        for (int i=0; i<this.currentLevel.getTrees().size(); i++) { // draw trees every frame
            this.currentLevel.getTrees().get(i).groundTree();
            this.currentLevel.getTrees().get(i).drawTree();
        }

        for (int i=0; i<this.currentLevel.getAlivePlayers().size(); i++) { // draw tanks every frame
            if (this.currentLevel.getAlivePlayers().get(i).getParachuteOn()==false) {
                this.currentLevel.getAlivePlayers().get(i).getTank().groundTank();
                this.currentLevel.getAlivePlayers().get(i).getTank().drawTurret();
                this.currentLevel.getAlivePlayers().get(i).getTank().drawTank();
            }
        }

        if (this.currentLevel.getAlivePlayers().size()>1) {
            this.currentLevel.displayScoreboard(); // draw scoreboard in top right corner if game has not ended
        }

        if (this.currentLevel.getAlivePlayers().size()>0) { // if there are still players alive
            this.currentLevel.getTurn().getTank().drawArrow();
            this.currentLevel.getTurn().getHealthPower().drawHealthBar(); // draw healthbar
            this.currentLevel.getTurn().getHealthPower().displayHealthPowerText(); // display health and power amounts
        }

        this.currentLevel.getTurn().displayPlayerText(); // display turn
        this.currentLevel.displayFuelParachute(); // draw parachute and fuel
        this.currentLevel.displayWind(); // draw wind
        
        for (int i=0; i<this.currentLevel.getAlivePlayers().size(); i++) {
            float explosionCentreX=-69; // initialise random number hehe
            float explosionCentreY=-69; // use these to check if they have been assigned properly later i.e. coordinates != -69

            if (this.currentLevel.getAlivePlayers().get(i).getHealthPower().getHealth() <= 0) { // player is dead if health <= 0
                this.currentLevel.getAlivePlayers().get(i).setPlayerAlive(false); // at the end of the draw() loop (scroll down), player will be removed from alivePlayers ArrayList
            }

            if (this.currentLevel.getAlivePlayers().get(i).getTank().getYCoordinate()>640) { // player is dead if they all terrain below them falls
                this.currentLevel.getAlivePlayers().get(i).setPlayerAlive(false);
            }

            if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile() != null && // ensures that projectile has been shot
            this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getProjectileShot() == true) {
                this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().drawProjectile();

                float projectileXCoordinate = this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getXCoordinate(); // tracks coordinates of projectiles
                float projectileYCoordinate = this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getYCoordinate();

                if (projectileXCoordinate >= 0 && projectileXCoordinate <= 864) { // projectile is still in screen

                    if (projectileYCoordinate >= this.currentLevel.getBackgroundTerrain().getMovingAveragePoints()[(int)(projectileXCoordinate)]) { // when projectile hits terrain
                        this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setExplosionOut(true); // sets explosion
                        this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setProjectileShot(false); // remove projectile drawing
                        explosionCentreX = projectileXCoordinate; // assign explosion centre coordinates
                        explosionCentreY = projectileYCoordinate;
                    }
                }
            }

            if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile() != null && // ensures that explosion has started (i.e. projectile has hit terrain)
            this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getExplosionOut() == true) {
                    
                this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().drawExplosion();

                if (this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().getExplosionRadius() == 30) { // when radius hits 30, remove explosion
                    this.currentLevel.getAlivePlayers().get(i).getTank().getProjectile().setExplosionOut(false); // sets explosion as false to remove drawing
                }
            }

            // terrain destruction here
            if (explosionCentreX != -69 && explosionCentreY != -69) { // check that explosion centre coordinates have been assigned properly
                float rangeLeft = explosionCentreX - 30;
                float rangeRight = explosionCentreX + 30;

                if (rangeLeft >= 0 && rangeRight <= 896) { // fix range PLS
                    for (int j=(int)rangeLeft; j<(int)rangeRight+1; j++) {
                        if (this.currentLevel.getBackgroundTerrain().insideExplosion(j, explosionCentreX, explosionCentreY)) {
                            float newY = this.currentLevel.getBackgroundTerrain().calculateNewY1(j, explosionCentreX, explosionCentreY);
                            this.currentLevel.getBackgroundTerrain().updateTerrain(j, newY);
                        } else if (this.currentLevel.getBackgroundTerrain().aboveExplosion(j, explosionCentreX, explosionCentreY)) {
                            float newY = this.currentLevel.getBackgroundTerrain().calculateNewY2(j, explosionCentreX, explosionCentreY);
                            this.currentLevel.getBackgroundTerrain().updateTerrain(j, newY);
                        }
                    }
                }
            }

            // calculate tank damage & parachute if needed
            for (int j=0; j<this.currentLevel.getAlivePlayers().size(); j++) {

                if (explosionCentreX != -69 && explosionCentreY != -69 && // ensures that explosion centre coordinates have been assigned properly
                this.currentLevel.getAlivePlayers().get(i).getTank().insideExplosion(this.currentLevel.getAlivePlayers().get(j).getTank().getTankCentreX(), 
                this.currentLevel.getAlivePlayers().get(j).getTank().getTankCentreY(), explosionCentreX, explosionCentreY)) { // checks if tank within circle of explosion

                    float damage = this.currentLevel.getAlivePlayers().get(i).getTank().damage(this.currentLevel.getAlivePlayers().get(j).getTank().getTankCentreX(),
                    this.currentLevel.getAlivePlayers().get(j).getTank().getTankCentreY(), explosionCentreX, explosionCentreY); // calculate damage

                    this.currentLevel.getAlivePlayers().get(j).getHealthPower().setLoseHealth(true); 
                    this.currentLevel.getAlivePlayers().get(j).getHealthPower().decreaseHealth((int)damage); // decrease tank health if in circle of explosion, proportionate to distance
                    this.currentLevel.getAlivePlayers().get(j).getHealthPower().setLoseHealth(false);
                    
                    if (i != j) { // ensures that tank cannot shoot itself to gain points
                        this.currentLevel.getAlivePlayers().get(i).setGainScore(true);
                        this.currentLevel.getAlivePlayers().get(i).increaseScore((int)damage); // increase score
                        this.currentLevel.getAlivePlayers().get(i).setGainScore(false);
                    }
                    
                }

                // check for parachute if tank higher than ground
                if (this.currentLevel.getAlivePlayers().get(j).getTank().getYCoordinate()+this.currentLevel.getAlivePlayers().get(j).getTank().getTankHeight()<
                this.currentLevel.getBackgroundTerrain().getMovingAveragePoints()[(int)this.currentLevel.getAlivePlayers().get(j).getTank().getTankCentreX()]) { 
                    if (this.currentLevel.getAlivePlayers().get(j).getParachute()>0) {
                        this.currentLevel.getAlivePlayers().get(j).setParachuteOn(true);
                        this.currentLevel.getAlivePlayers().get(j).drawParachute();
                        this.currentLevel.getAlivePlayers().get(j).getTank().drawTurret();
                        this.currentLevel.getAlivePlayers().get(j).getTank().drawTank();

                        this.currentLevel.getAlivePlayers().get(j).getTank().increaseYCoordinate((float)0.5);

                        if (this.currentLevel.getAlivePlayers().get(j).getTank().getYCoordinate() >= 
                        this.currentLevel.getBackgroundTerrain().getMovingAveragePoints()[(int)this.currentLevel.getAlivePlayers().get(j).getTank().getXCoordinate()+
                        (this.currentLevel.getAlivePlayers().get(j).getTank().getTankWidth()/2)]-(this.currentLevel.getAlivePlayers().get(j).getTank().getTankHeight())) {
                            this.currentLevel.getAlivePlayers().get(j).setParachuteOn(false);
                            int currentParachuteNum = this.currentLevel.getAlivePlayers().get(j).getParachute();
                            this.currentLevel.getAlivePlayers().get(j).setParachute(currentParachuteNum-1);
                        }

                    } else { // no parachute = damage from fall
                        float heightFall = ((this.currentLevel.getBackgroundTerrain().getMovingAveragePoints()[(int)this.currentLevel.getAlivePlayers().get(j).getTank().getXCoordinate()+
                        (this.currentLevel.getAlivePlayers().get(j).getTank().getTankWidth()/2)]-(this.currentLevel.getAlivePlayers().get(j).getTank().getTankHeight())) -
                        this.currentLevel.getAlivePlayers().get(j).getTank().getYCoordinate());

                        this.currentLevel.getAlivePlayers().get(j).getHealthPower().setLoseHealth(true); 
                        this.currentLevel.getAlivePlayers().get(j).getHealthPower().decreaseHealth((int)heightFall); // decrease tank health if in circle of explosion, proportionate to distance
                        this.currentLevel.getAlivePlayers().get(j).getHealthPower().setLoseHealth(false);

                        if (i != j) { // ensures that tank cannot shoot itself to gain points
                            this.currentLevel.getAlivePlayers().get(i).setGainScore(true);
                            this.currentLevel.getAlivePlayers().get(i).increaseScore((int)heightFall); // increase score
                            this.currentLevel.getAlivePlayers().get(i).setGainScore(false);
                        }
                    }
                }
            }
        }

        for (int i=0; i<this.currentLevel.getAlivePlayers().size(); i++) {
            if (this.currentLevel.getAlivePlayers().get(i).getPlayerAlive()==false) {
                this.currentLevel.removeAlivePlayer(this.currentLevel.getAlivePlayers().get(i)); // remove player from alivePlayers if not alive
            }
        }


        this.playersNumber = this.currentLevel.getAlivePlayers().size(); // update number of alive players

        if (this.playersNumber <= 1) { // switches level OR game over

            this.currentLevel.setSwitchLevelOn(true);
            this.currentLevel.increaseSwitchLevelCount(1); // count per frame to keep track of when 1 second is over

            if (this.currentLevel == level1) {
                for (int i=0; i<this.currentLevel.getPlayersObj().size(); i++) {
                    this.level2.getPlayersObj().get(i).setScore(this.currentLevel.getPlayersObj().get(i).getScore()); // transfers existing scores over to Player objects in next level
                    this.level2.getPlayersObj().get(i).setParachute(this.currentLevel.getPlayersObj().get(i).getParachute()); // transfers number of parachutes over to next level
                }
            
                if (this.level1.getSwitchLevelCount() == 30) { // 1 second break between level switches (i.e. 30FPS --> count will go up to 30 in a second)
                    this.currentLevel = level2;
                    this.level1.setSwitchLevelOn(false);
                    this.level1.setSwitchLevelCount(0);
                }

            } else if (this.currentLevel == level2) {
                for (int i=0; i<this.currentLevel.getPlayersObj().size(); i++) {
                    this.level3.getPlayersObj().get(i).setScore(this.currentLevel.getPlayersObj().get(i).getScore());
                    this.level3.getPlayersObj().get(i).setParachute(this.currentLevel.getPlayersObj().get(i).getParachute());
                    
                }

                if (this.level2.getSwitchLevelCount() == 30) {
                    this.currentLevel = level3;
                    this.level2.setSwitchLevelOn(false);
                    this.level2.setSwitchLevelCount(0);
                }
                
            } else if (this.currentLevel == level3) {
                this.currentLevel.displayFinalScoreboard();
            }
        }
    }


    public static void main(String[] args) {
        PApplet.main("Tanks.App");
    }

}

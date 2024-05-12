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


public class Level {

    private BackgroundTerrain backgroundTerrain;
    private App app;
    private ArrayList<String> players = new ArrayList<String>();
    private ArrayList<int[]> playersCoordinates = new ArrayList<int[]>();
    private ArrayList<int[]> colourSchemes = new ArrayList<int[]>();
    private ArrayList<Player> playersObj = new ArrayList<Player>();
    private ArrayList<Player> alivePlayers = new ArrayList<Player>();
    private int level;
    private PImage treeImage;
    private ArrayList<Tree> trees = new ArrayList<Tree>();
    private String[][] terrainMatrix;
    private ArrayList<int[]> treeCoordinates = new ArrayList<int[]>();
    private PImage windPositiveImage;
    private PImage windNegativeImage;
    private int windLevel;
    private Random random = new Random();
    private Player turn;
    private PImage fuelImage;
    private PImage parachuteImage;

    public Level(App app, int level) {
        this.app = app;
        this.level = level;
        this.backgroundTerrain = new BackgroundTerrain(app, level, this);
        this.setWind();
    }

    public void setWind() {
        int initialWind = random.nextInt(35+35)-35;
        this.windLevel = initialWind;
    }

    public void updateWind() {
        int windChange = random.nextInt(5+6)-5;
        this.windLevel += windChange;
    }

    public int getWind() {
        return this.windLevel;
    }

    public void displayWind() { // what the method says
        this.windPositiveImage = app.loadImage(app.getClass().getResource("wind.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " ")); // get image
        this.windNegativeImage = app.loadImage(app.getClass().getResource("wind-1.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " ")); // get image

        if (this.windLevel >= 0) {
            app.image(this.windPositiveImage, 750, 2, 50, 50);
            String wind = String.format("%d", this.windLevel);
            app.strokeWeight(1);
            app.textSize(18);
            app.fill(0);
            app.text(wind, 810, 30);
        } else if (this.windLevel < 0) {
            app.image(this.windNegativeImage, 750, 2, 50, 50);
            String wind = String.format("%d", Math.abs(this.windLevel));
            app.strokeWeight(1);
            app.textSize(18);
            app.fill(0);
            app.text(wind, 810, 30);
        }
    }

    public void setBackground() {
        this.backgroundTerrain.setBackground();
    }

    public BackgroundTerrain getBackgroundTerrain() {
        return this.backgroundTerrain;
    }

    public void setTerrain() {
        this.backgroundTerrain.setTerrain();
    }

    public void setTerrainMatrix() {
        this.terrainMatrix = this.backgroundTerrain.getTerrainMatrix();
    }

    public int getLevel() {
        return this.level;
    }

    public ArrayList<String> getPlayers() {
        return this.players;
    }

    public ArrayList<Player> getPlayersObj() {
        return this.playersObj;
    }

    public ArrayList<Player> getAlivePlayers() {
        return this.alivePlayers;
    }

    public void removeAlivePlayer(Player player) {
        this.alivePlayers.remove(player);
    }

    public ArrayList<int[]> getPlayersCoordinates() {
        return this.playersCoordinates;
    }

    public ArrayList<int[]> getTreeCoordinates() {
        return this.treeCoordinates;
    }

    public PImage getTreeImage() {
        return this.treeImage;
    }

    public ArrayList<Tree> getTrees() {
        return this.trees;
    }

    public void setTrees() {

        if (!app.levelsData.getJSONObject(level-1).isNull("trees")) { // make sure "trees" field is available
            setTerrainMatrix(); // get matrix

            String image = app.levelsData.getJSONObject(level-1).getString("trees"); // read "trees" field
            this.treeImage = app.loadImage(app.getClass().getResource(image).getPath().toLowerCase(Locale.ROOT).replace("%20", " ")); // get image

            for (int i=0; i<28; i++) {
                for (int j=0; j<20; j++) {
                    if (this.terrainMatrix[j][i].equals("T")) { // read coordinates of T
                    
                        int[] coordinates = {(i*32),(j*32)}; // add coordinates
                        this.treeCoordinates.add(coordinates); // keep in list to keep track
                    }
                }
            }

            for (int i=0; i<this.treeCoordinates.size(); i++) {
                Tree treeObj = new Tree(app, this, this.treeCoordinates.get(i)[0], this.treeCoordinates.get(i)[1]);
                this.trees.add(treeObj);
            }
        }
    }


    public void setPlayers() {
        setTerrainMatrix();

        for (int i=0; i<28; i++) {
            for (int j=0; j<20; j++) {
                if (!this.terrainMatrix[j][i].equals(" ") && !this.terrainMatrix[j][i].equals("X") && !this.terrainMatrix[j][i].equals("T") 
                && !this.terrainMatrix[j][i].equals(null) && !this.terrainMatrix[j][i].equals("\n") && !this.terrainMatrix[j][i].equals("\t")) {
                    this.players.add(terrainMatrix[j][i]);

                    int[] coordinates = {(i*32),j*32};
                    this.playersCoordinates.add(coordinates);
                }
            }
        }

        JSONObject playersData = app.playersData;

        for (int i=0; i<this.players.size(); i++) {

            if (!playersData.isNull(this.players.get(i))) {
                String colours = playersData.getString(this.players.get(i)); // add colour scheme
                String[] coloursSplit = colours.split(",");
                int[] colourScheme = new int[3];
                
                for (int j=0; j<coloursSplit.length; j++) {
                    colourScheme[j] = Integer.parseInt(coloursSplit[j]);
                }

                this.colourSchemes.add(colourScheme);
            }
        }


        for (int i=0; i<this.players.size(); i++) {
            Player playerObj = new Player(this.app, this, this.playersCoordinates.get(i)[0], this.playersCoordinates.get(i)[1], this.colourSchemes.get(i), this.players.get(i));
            this.playersObj.add(playerObj);
            playerObj.setTank();
            playerObj.setHealthPower();
        }

    }


    public void sortPlayers() { // alphabetical order (maybe change sort algo later if laggy)
        ArrayList<Player> playersObjSorted = new ArrayList<Player>();
        ArrayList<String> playersSorted = new ArrayList<String>();
        ArrayList<int[]> playersCoordinatesSorted = new ArrayList<int[]>();
        ArrayList<int[]> colourSchemesSorted = new ArrayList<int[]>();

        while (this.players.size() > 0) {
            String smallest = this.players.get(0);
            int index=0;

            for (int i = 0; i<this.players.size(); i++) {
                if (this.players.get(i).compareTo(smallest) < 0) {
                    smallest = this.players.get(i);
                    index = i;
                }
            }

            playersSorted.add(smallest);
            playersObjSorted.add(this.playersObj.get(index));
            playersCoordinatesSorted.add(this.playersCoordinates.get(index));
            colourSchemesSorted.add(this.colourSchemes.get(index));

            this.players.remove(index);
            this.playersObj.remove(index);
            this.playersCoordinates.remove(index);
            this.colourSchemes.remove(index);
        }

        this.players = playersSorted;
        this.playersObj = playersObjSorted;
        this.playersCoordinates = playersCoordinatesSorted;
        this.colourSchemes = colourSchemesSorted;

        for (int i=0; i<this.playersObj.size(); i++) {
            this.alivePlayers.add(this.playersObj.get(i));
        }
    }


    public void setTurn(Player player) { 
        this.turn = player;
    }

    public Player getTurn() {
        return this.turn;
    }


    public void displayScoreboard() { // what the method says
        app.strokeWeight(5);
        app.stroke(0);
        app.noFill();
        app.rect(700, 50, 150, 28*(1+this.players.size()));
        app.line(700, 50+28, 850, 50+28);
        app.strokeWeight(1);
        app.textSize(18);
        app.text("Scores", 710, 50+20);
        for (int i=0; i<this.playersObj.size(); i++) {
            String playerPrint = String.format("Player %s", this.playersObj.get(i).getPlayerString());
            String playerScore = String.format("%d", this.playersObj.get(i).getScore());
            int[] colourScheme = this.colourSchemes.get(i);
            app.fill(colourScheme[0], colourScheme[1], colourScheme[2]);
            app.text(playerPrint, 710, 50+(26*(i+2)));

            app.fill(0);
            app.text(playerScore, 815, 50+(26*(i+2)));
        }
    }


    public void displayFuelParachute() { // what the method says
        fuelImage = app.loadImage(app.getClass().getResource("fuel.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        app.image(fuelImage, 180, 5, 28, 28);
        this.turn.displayFuel();

        parachuteImage = app.loadImage(app.getClass().getResource("parachute.png").getPath().toLowerCase(Locale.ROOT).replace("%20", " "));
        app.image(parachuteImage, 180, 35, 28, 28);
        this.turn.displayParachute();
    }
}

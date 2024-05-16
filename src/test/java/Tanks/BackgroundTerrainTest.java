package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BackgroundTerrainTest {
    App app = new App();
    BackgroundTerrain backgroundTerrain = app.level1.getBackgroundTerrain();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void setBackgroundTest() {
        assertTrue(backgroundTerrain.setBackground() == true);
    }

    @Test 
    public void setForegroundColourTest() {
        assertTrue(backgroundTerrain.setForegroundColour() == true);
    }

    @Test 
    public void setTerrainMatrixTest() {
        assertTrue(backgroundTerrain.setTerrainMatrix() == true);
    }

    @Test
    public void calculateMovingAverageTest() {
        assertTrue(backgroundTerrain.calculateMovingAverage() == true);
    }

}

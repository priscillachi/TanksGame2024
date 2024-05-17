package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BackgroundTerrainTest {
    App app = new App();
    BackgroundTerrain backgroundTerrain = app.level1.getBackgroundTerrain();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000);
    }

    @Test 
    public void setBackgroundTest() {
        assertTrue(this.backgroundTerrain.setBackground() == true);
    }

    @Test 
    public void setForegroundColourTest() {
        assertTrue(this.backgroundTerrain.setForegroundColour() == true);
    }

    @Test 
    public void setTerrainMatrixTest() {
        assertTrue(this.backgroundTerrain.setTerrainMatrix() == true);
    }

    @Test
    public void calculateMovingAverageTest() {
        assertTrue(this.backgroundTerrain.calculateMovingAverage() == true);
    }

}

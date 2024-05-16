package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
    App app = new App();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void checkLevelsExist() {
        assertTrue(app.level1 != null);
        assertTrue(app.level2 != null);
        assertTrue(app.level3 != null);
    }

    @Test 
    public void displayFinalScoreboard() {
        assertTrue(app.level1.displayFinalScoreboard() == true);
    }

}

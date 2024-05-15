package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
    App app = new App();

    @Test 
    public void checkLevelExists() {
        Level level = new Level(app, 1);
        assertTrue(level != null);
    }
}

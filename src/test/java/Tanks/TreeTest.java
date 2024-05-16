package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TreeTest {
    App app = new App();
    Tree tree = new Tree(app, app.level1, -1, 30);

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void testGroundTree() {
        assertTrue(tree.groundTree() == true);
    }

    @Test 
    public void testDrawTree() {
        assertTrue(tree.drawTree() == false);
    }
    
}

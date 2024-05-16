package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class HealthPowerTest {
    App app = new App();
    int[] colours = {255, 0, 0};
    Player player = new Player(app, app.level2, 20, 20, colours, "X");
    HealthPower healthPower = new HealthPower(app, app.level2, player);

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }
    
    @Test 
    public void decreaseHealthTrue() {
        healthPower.setLoseHealth(true);
        healthPower.decreaseHealth(60);
        assertTrue(healthPower.getHealth() == 40);
    }

    @Test 
    public void decreaseHealthFalse() {
        healthPower.setLoseHealth(false);
        assertTrue(healthPower.decreaseHealth(60)==false);
    }

    @Test 
    public void updatePower() {
        assertTrue(healthPower.updatePower(130)==false);
        assertTrue(healthPower.updatePower(20)==true);
        assertTrue(healthPower.getPower() == 20);
    }

    @Test 
    public void powerIncreaseDecrease() {
        healthPower.updateHealth(90);
        assertTrue(healthPower.powerIncrease()==true);
        assertTrue(healthPower.powerDecrease()==true);

        healthPower.updatePower(-20);
        assertTrue(healthPower.powerDecrease()==false);
    }
}

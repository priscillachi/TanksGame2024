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
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000); 
    }
    
    @Test 
    public void decreaseHealthTrue() {
        this.healthPower.setLoseHealth(true);
        this.healthPower.decreaseHealth(60);
        assertTrue(this.healthPower.getHealth() == 40);
    }

    @Test 
    public void decreaseHealthFalse() {
        this.healthPower.setLoseHealth(false);
        assertTrue(this.healthPower.decreaseHealth(60)==false);
    }

    @Test 
    public void updatePower() {
        assertTrue(this.healthPower.updatePower(130)==false);
        assertTrue(this.healthPower.updatePower(20)==true);
        assertTrue(this.healthPower.getPower() == 20);
    }

    @Test 
    public void powerIncreaseDecrease() {
        this.healthPower.updateHealth(90);
        assertTrue(this.healthPower.powerIncrease()==true);
        assertTrue(this.healthPower.powerDecrease()==true);

        this.healthPower.updatePower(-20);
        assertTrue(this.healthPower.powerDecrease()==false);
    }
}

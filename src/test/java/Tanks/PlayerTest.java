package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    App app = new App();
    int[] colour = {255, 0, 0};
    Player player = new Player(app, app.level1, 20, 20, colour, "X");

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000); 
    }

    @Test 
    public void drawShield() {
        this.player.setTank();

        this.player.setShieldOn(true);
        this.player.setShieldCount(24);
        assertTrue(this.player.drawShield()==true);

        this.player.setShieldOn(false);
        assertFalse(this.player.drawShield()==true);
    }

    @Test 
    public void drawParachute() {
        this.player.setTank();
        app.level1.displayFuelParachute();

        this.player.setParachuteOn(true);
        assertTrue(this.player.drawParachute()==true);

        this.player.setParachuteOn(false);
        assertFalse(this.player.drawParachute()==true);
    }

    @Test 
    public void increaseScore() {
        this.player.setGainScore(true);
        this.player.increaseScore(20);
        assertEquals(this.player.getScore(),20);

        this.player.setGainScore(false);
        this.player.increaseScore(20);
        assertEquals(this.player.getScore(),20);
    }

    @Test
    public void updateHealthPower() {
        this.player.setHealthPower();

        this.player.updateHealth(80);
        assertEquals(this.player.getHealth(), 80);

        this.player.updatePower(40);
        assertEquals(this.player.getPower(), 40);

    }
}

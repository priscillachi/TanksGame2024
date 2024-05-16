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
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void drawShield() {
        player.setTank();

        player.setShieldOn(true);
        player.setShieldCount(24);
        assertTrue(player.drawShield()==true);

        player.setShieldOn(false);
        assertFalse(player.drawShield()==true);
    }

    @Test 
    public void drawParachute() {
        player.setTank();
        app.level1.displayFuelParachute();

        player.setParachuteOn(true);
        assertTrue(player.drawParachute()==true);

        player.setParachuteOn(false);
        assertFalse(player.drawParachute()==true);
    }

    @Test 
    public void increaseScore() {
        player.setGainScore(true);
        player.increaseScore(20);
        assertEquals(player.getScore(),20);

        player.setGainScore(false);
        player.increaseScore(20);
        assertEquals(player.getScore(),20);
    }

    @Test
    public void updateHealthPower() {
        player.setHealthPower();

        player.updateHealth(80);
        assertEquals(player.getHealth(), 80);

        player.updatePower(40);
        assertEquals(player.getPower(), 40);

    }
}

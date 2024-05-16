package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    App app = new App();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test
    public void checkAppExists() {
        assertTrue(app != null);
    }

    @Test 
    public void appTest() {
        app.draw();
        assertTrue(app.playersNumber == app.currentLevel.getAlivePlayers().size());

        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)0.2);
        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.draw();
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().getXCoordinate()!=app.currentLevel.getAlivePlayers().get(0).getTank().getXCoordinate());
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().getYCoordinate()!=app.currentLevel.getAlivePlayers().get(0).getTank().getYCoordinate());

        app.currentLevel.getAlivePlayers().get(0).getHealthPower().updateHealth(0);
        app.draw();
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getPlayerAlive()==false);
    }
}

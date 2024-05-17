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

    @Test
    public void parachuteTest() {
        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)1.57);
        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setXCoordinate(20);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(false);
        for (int i=0; i<30; i++) {
            app.draw();
            i+=1;
        }
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().getProjectileShot()==true);

        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(true);
        app.currentLevel.getAlivePlayers().get(0).getTank().setYCoordinate(app.currentLevel.getAlivePlayers().get(0).getTank().getYCoordinate()-10);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getHealthPower().getLoseHealth()==false);
    }


    @Test 
    public void belowMap() {
        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)1.57);
        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setXCoordinate(20);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(false);
        for (int i=(int)app.currentLevel.getAlivePlayers().get(0).getTank().getXCoordinate()-30; i<(int)app.currentLevel.getAlivePlayers().get(0).getTank().getXCoordinate()+60; i++) {
            app.currentLevel.getBackgroundTerrain().updateTerrain(i, 750);
            i+=1;
        }

        app.currentLevel.getAlivePlayers().get(0).getTank().setYCoordinate(700);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(true);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }

        app.currentLevel.getAlivePlayers().get(0).getTank().setExplosionRadius(30);
        app.draw();
        //assertTrue(app.currentLevel.getAlivePlayers().size()!=0);
    }
}

package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    App app = new App();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app); // initialise
        app.delay(1000); 
    }

    @Test
    public void checkAppExists() {
        assertTrue(app != null);
    }

    @Test 
    public void appTest() { // test app in general
        app.draw();
        assertTrue(app.playersNumber == app.currentLevel.getAlivePlayers().size());
        Player player = app.currentLevel.getAlivePlayers().get(0);

        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)0.2);
        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.delay(1000); 
        app.draw();
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().getXCoordinate()!=app.currentLevel.getAlivePlayers().get(0).getTank().getXCoordinate());
        assertTrue(app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().getYCoordinate()!=app.currentLevel.getAlivePlayers().get(0).getTank().getYCoordinate());

        app.currentLevel.getAlivePlayers().get(0).getHealthPower().updateHealth(0);
        app.delay(1000); 
        app.draw();
        assertTrue(player.getPlayerAlive()==false);
    }


    @Test 
    public void belowMap() { // test if tank falls below map; parachute must be off for tank to be able to fall below map
        Player player = app.currentLevel.getAlivePlayers().get(0);

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

        app.currentLevel.getAlivePlayers().get(0).getTank().setYCoordinate(701);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(true);
        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }

        app.currentLevel.getAlivePlayers().get(0).getTank().setExplosionRadius(30);
        app.delay(1000);
        app.draw();

        assertTrue(player.getBelowMap()==true);
    }

    @Test 
    public void shieldNoParachuteTest() { // shield on, parachute off i.e. no parachute left
        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)1.57);

        app.currentLevel.getAlivePlayers().get(1).setShield(2);
        app.currentLevel.getAlivePlayers().get(1).setParachute(0);
        Player player = app.currentLevel.getAlivePlayers().get(1);

        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(true);
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setXCoordinate(app.currentLevel.getAlivePlayers().get(1).getTank().getTankCentreX());
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setYCoordinate(app.currentLevel.getAlivePlayers().get(1).getTank().getYCoordinate()+18);
        app.currentLevel.getAlivePlayers().get(1).setShieldOn(true);

        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }

        assertTrue(player.getShieldCount()!=0);
    }

    @Test
    public void noShieldParachuteTest() { // shield off, parachute on, i.e. no shield left
        app.currentLevel.getAlivePlayers().get(0).setTank();
        app.currentLevel.getAlivePlayers().get(0).getTank().setAngle((float)1.57);

        app.currentLevel.getAlivePlayers().get(1).setShield(0);
        app.currentLevel.getAlivePlayers().get(1).setParachute(1);
        Player player = app.currentLevel.getAlivePlayers().get(1);

        app.currentLevel.getAlivePlayers().get(0).getTank().setProjectile();
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setProjectileShot(true);
        app.currentLevel.getAlivePlayers().get(0).setParachuteOn(true);
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setXCoordinate(app.currentLevel.getAlivePlayers().get(1).getTank().getTankCentreX());
        app.currentLevel.getAlivePlayers().get(0).getTank().getProjectile().setYCoordinate(app.currentLevel.getAlivePlayers().get(1).getTank().getYCoordinate()+18);

        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }

        assertTrue(player.getParachute()==0);
    }

    @Test 
    public void switchLevels() { // switch levels when there are one or less number of players (i.e. size of alivePlayers is 1 or 0)
        app.currentLevel.getAlivePlayers().clear();
        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }
        assertTrue(app.currentLevel==app.level2);

        app.currentLevel.getAlivePlayers().clear();
        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }
        assertTrue(app.currentLevel==app.level3);

        app.currentLevel.getAlivePlayers().clear();
        app.delay(1000);
        for (int i=0; i<30; i++) {
            app.draw();
            i += 1;
        }
        app.key = 'r';
        //app.delay(1000);
        app.keyPressed(null);
        assertTrue(app.currentLevel==app.level1);

    }

    @Test 
    public void keyTest() { // test keys - these will display on the screen when gradle test or gradle test jacocoTestReport is ran
        app.key = 'w';
        app.keyPressed(null);

        app.key = 's';
        app.keyPressed(null);

        app.key = ' ';
        app.keyPressed(null);

        app.key = ' ';
        app.playerTurn = app.playersNumber-1;
        app.delay(1000);
        app.keyPressed(null);
        
        app.key = 'f';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(300);
        app.delay(1000);
        app.keyPressed(null);

        app.key = 'h';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(300);
        app.delay(1000);
        app.keyPressed(null);

        app.key = 'p';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(300);
        app.delay(1000);
        app.keyPressed(null);

        app.key = 'f';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(0);
        app.delay(1000); 
        app.keyPressed(null);

        app.key = 'h';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(0);
        app.delay(1000);
        app.keyPressed(null);

        app.key = 'p';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(0);
        app.delay(1000); 
        app.keyPressed(null);

        app.key = 'r';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(300);
        app.currentLevel.getAlivePlayers().get(app.playerTurn).getHealthPower().updateHealth(20);
        app.delay(1000); 
        app.keyPressed(null);

        app.key = 'r';
        app.currentLevel.getAlivePlayers().get(app.playerTurn).setScore(300);
        app.currentLevel.getAlivePlayers().get(app.playerTurn).getHealthPower().updateHealth(90);
        app.delay(1000); 
        app.keyPressed(null);

        app.key = 'r';
        app.currentLevel = app.level3;
        app.currentLevel.getAlivePlayers().clear();
        app.delay(1000); 
        app.keyPressed(null);

        app.keyCode = app.UP;
        app.keyPressed(null);

        app.keyCode = app.DOWN;
        app.keyPressed(null);

        app.keyCode = app.LEFT;
        app.keyPressed(null);

        app.keyCode = app.RIGHT;
        app.keyPressed(null);
    }
}

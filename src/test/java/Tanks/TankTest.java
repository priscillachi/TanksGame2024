package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TankTest {
    App app = new App();
    int[] colour = {255, 0, 0};
    Player player = new Player(app, app.level1, 20, 20, colour, "X");
    Tank tank = new Tank(20, 20, colour, app, app.level1, player);

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void setProjectileExplosion() {
        player.setHealthPower();
        tank.setProjectile();
        assertTrue(tank.drawExplosion()==false);

        tank.setExplosionOut(true);
        assertTrue(tank.drawExplosion()==true);

        tank.rotateTurretLeft();
        tank.setProjectile();
        assertTrue(tank.getProjectile()!=null);

        tank.rotateTurretRight();
        tank.rotateTurretRight();
        tank.setProjectile();
        assertTrue(tank.getProjectile()!=null);

        tank.setAngle((float)Math.PI/2);
        tank.setProjectile();
        assertTrue(tank.getProjectile()!=null);
    }

    @Test 
    public void moveTank() {
        tank.moveTankLeft();
        assertTrue(tank.getXCoordinate()<20);

        tank.moveTankRight();
        tank.moveTankRight();
        assertTrue(tank.getXCoordinate()>20);

        tank.setXCoordinate(0);
        tank.moveTankLeft();
        assertTrue(tank.getXCoordinate()==0);

        tank.setXCoordinate(864);
        tank.moveTankRight();
        assertTrue(tank.getXCoordinate()==864);

        player.setFuel(0);
        tank.moveTankLeft();
        assertTrue(tank.getXCoordinate()==864);

        player.setFuel(1);
        tank.moveTankRight();
        assertTrue(tank.getXCoordinate()==864);
    }

    @Test 
    public void explosion() {
        assertTrue(tank.insideExplosion(20,20,40,40)==true);
        assertTrue(tank.damage(20,20,40,40)==
        ((30-(float)Math.sqrt(((20-40)*(20-40)) + ((20-40)*(20-40))))/30) * 60);

        assertFalse(tank.insideExplosion(20,20,800,800)==true);
    }

    @Test 
    public void increaseYCoordinate() {
        player.setParachuteOn(true);
        tank.increaseYCoordinate(1);
        assertEquals(tank.getYCoordinate(), 21);

        player.setParachuteOn(false);
        tank.increaseYCoordinate(1);
        assertEquals(tank.getYCoordinate(), 21);
    }
}

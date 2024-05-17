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
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000); 
    }

    @Test 
    public void setProjectileExplosion() {
        this.player.setHealthPower();
        this.tank.setProjectile();
        assertTrue(this.tank.drawExplosion()==false);

        this.tank.setExplosionOut(true);
        assertTrue(this.tank.drawExplosion()==true);

        this.tank.rotateTurretLeft();
        this.tank.setProjectile();
        assertTrue(this.tank.getProjectile()!=null);

        this.tank.rotateTurretRight();
        this.tank.rotateTurretRight();
        this.tank.setProjectile();
        assertTrue(this.tank.getProjectile()!=null);

        this.tank.setAngle((float)Math.PI/2);
        this.tank.setProjectile();
        assertTrue(this.tank.getProjectile()!=null);
    }

    @Test 
    public void moveTank() {
        this.tank.moveTankLeft();
        assertTrue(this.tank.getXCoordinate()<20);

        this.tank.moveTankRight();
        this.tank.moveTankRight();
        assertTrue(this.tank.getXCoordinate()>20);

        this.tank.setXCoordinate(0);
        this.tank.moveTankLeft();
        assertTrue(this.tank.getXCoordinate()==0);

        this.tank.setXCoordinate(864);
        this.tank.moveTankRight();
        assertTrue(this.tank.getXCoordinate()==864);

        this.player.setFuel(0);
        this.tank.moveTankLeft();
        assertTrue(this.tank.getXCoordinate()==864);

        this.player.setFuel(1);
        this.tank.moveTankRight();
        assertTrue(this.tank.getXCoordinate()==864);
    }

    @Test 
    public void explosion() {
        assertTrue(this.tank.insideExplosion(20,20,40,40)==true);
        assertTrue(this.tank.damage(20,20,40,40)==
        ((30-(float)Math.sqrt(((20-40)*(20-40)) + ((20-40)*(20-40))))/30) * 60);

        assertFalse(this.tank.insideExplosion(20,20,800,800)==true);
    }

    @Test 
    public void increaseYCoordinate() {
        this.player.setParachuteOn(true);
        this.tank.increaseYCoordinate(1);
        assertEquals(this.tank.getYCoordinate(), 21);

        this.player.setParachuteOn(false);
        this.tank.increaseYCoordinate(1);
        assertEquals(this.tank.getYCoordinate(), 21);
    }
}

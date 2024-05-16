package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {
    App app = new App();
    int[] colour = {255, 0, 0};
    Player player = new Player(app, app.level1, 20, 20, colour, "X");
    HealthPower healthPower = new HealthPower(app, app.level2, player);
    Tank tank = new Tank(20, 20, colour, app, app.level1, player);
    Projectile projectile = new Projectile(app, tank, 20, 20, tank.getAngle(), healthPower.getPower(), 4, colour);

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, app);
        app.delay(1000); // to give time to initialise stuff before drawing begins
    }

    @Test 
    public void updateVelocityCoordinates() {
        projectile.setWind(-15);
        projectile.setAngle((float)-0.2);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);

        projectile.setWind(15);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);

        projectile.setAngle((float)0.35);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);

        projectile.setWind(-15);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);

        projectile.setAngle(0);
        tank.setAngle(-(float)Math.PI/2);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);

        projectile.setAngle(0);
        tank.setAngle((float)Math.PI/2);
        projectile.updateVelocityCoordinates();
        assertTrue(projectile.getXCoordinate()!=0 && projectile.getYCoordinate()!=0);
    }

    @Test 
    public void drawProjectile() {
        projectile.setProjectileShot(true);
        assertTrue(projectile.drawProjectile()==true);

        projectile.setProjectileShot(false);
        assertFalse(projectile.drawProjectile()==true);
    }

    @Test 
    public void drawExplosion() {
        projectile.setExplosionOut(true);
        assertTrue(projectile.drawExplosion()==true);

        projectile.setExplosionOut(false);
        assertFalse(projectile.drawExplosion()==true);
    }
    
}

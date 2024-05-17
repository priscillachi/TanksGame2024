package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectileTest {
    App app = new App();
    int[] colour = {255, 0, 0};
    Player player = new Player(app, app.level1, 20, 20, colour, "X");
    HealthPower healthPower = new HealthPower(app, app.level1, player);
    Tank tank = new Tank(20, 20, colour, app, app.level1, player);
    Projectile projectile = new Projectile(app, tank, 20, 20, tank.getAngle(), healthPower.getPower(), 4, colour);

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000); 
    }

    @Test 
    public void updateVelocityCoordinates() {
        this.projectile.setWind(-15);
        this.projectile.setAngle((float)-0.2);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);

        this.projectile.setWind(15);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);

        this.projectile.setAngle((float)0.35);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);

        this.projectile.setWind(-15);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);

        this.projectile.setAngle(0);
        this.tank.setAngle(-(float)Math.PI/2);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);

        this.projectile.setAngle(0);
        this.tank.setAngle((float)Math.PI/2);
        this.projectile.updateVelocityCoordinates();
        assertTrue(this.projectile.getXCoordinate()!=0 && this.projectile.getYCoordinate()!=0);
    }

    @Test 
    public void drawProjectile() {
        this.projectile.setProjectileShot(true);
        assertTrue(this.projectile.drawProjectile()==true); // assert true

        this.projectile.setProjectileShot(false);
        assertFalse(this.projectile.drawProjectile()==true); // assert false
    }

    @Test 
    public void drawExplosion() {
        this.projectile.setExplosionOut(true);
        assertTrue(this.projectile.drawExplosion()==true); // assert true

        this.projectile.setExplosionOut(false);
        assertFalse(this.projectile.drawExplosion()==true); // assert false
    }
    
}

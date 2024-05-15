package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {

    @Test
    public void checkAppExists() {
        App app = new App();
        assertTrue(app != null);
    }
}

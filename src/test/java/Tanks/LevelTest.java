package Tanks;


import processing.core.PApplet;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
    App app = new App();

    @BeforeEach
    public void beforeEach() {
        PApplet.runSketch(new String[] { "App" }, this.app);
        this.app.delay(1000);
    }

    @Test 
    public void checkLevelsExist() {
        assertTrue(this.app.level1 != null);
        assertTrue(this.app.level2 != null);
        assertTrue(this.app.level3 != null);
    }

    @Test 
    public void displayFinalScoreboard() {
        assertTrue(this.app.level1.displayFinalScoreboard() == true);
        assertTrue(this.app.level2.displayFinalScoreboard() == true);
        assertTrue(this.app.level3.displayFinalScoreboard() == true);
    }

    @Test 
    public void getLevel() {
        assertTrue(this.app.level1.getLevel() == 1);
    }

    @Test 
    public void getPlayers() {
        assertTrue(this.app.level1.getPlayers().size() == 4);
    }

    @Test 
    public void sortScores() {
        this.app.delay(1000);
        for (int i=0; i<30; i++) {
            this.app.draw();
            i += 1;
        }

        this.app.level1.getPlayersObj().get(0).setScore(200);
        this.app.level1.getPlayersObj().get(1).setScore(100);
        this.app.level1.getPlayersObj().get(2).setScore(430);
        this.app.level1.getPlayersObj().get(3).setScore(96);

        this.app.level1.sortScores();

        assertTrue(this.app.level1.getPlayersObj().get(0).getScore() == 430);
    }

}

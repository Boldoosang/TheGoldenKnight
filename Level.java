import javax.swing.*;
import java.awt.*;

public class Level {
    TileMap tileMap;
    TileMapManager tileManager;
    BackgroundManager bgManager;
    SoundManager soundManager;
    private int currentLevel;
    private JFrame window;

    //Initializes the levels.
    public Level(JFrame window, Scoreboard scoreboard) {
        this.window = window;
        tileManager = new TileMapManager(window, scoreboard);
        soundManager = SoundManager.getInstance();
    }

    public void loadLevel(int level){
        bgManager = new BackgroundManager (window, level);
        currentLevel = level;
        String path;

        //Gets the current map and map music.
        if(level == 1){
            path = "maps/map1.txt";
            soundManager.stopAllSounds();
            soundManager.playSound("level1", true);
        } else if(level == 2){
            path = ("maps/map2.txt");
            soundManager.stopAllSounds();
            soundManager.playSound("level2", true);
        } else if(level == 3){
            path = ("maps/map3.txt");
            soundManager.stopAllSounds();
            soundManager.playSound("menu", true);
        } else {
            path = "";
        }

        //Loads the map and places the entities.
        try {
            tileMap = tileManager.loadMap(path);
            placeEntities();
        }
        catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //Draws the background and tileMap entities.
    public void draw(Graphics2D imageContext){
        bgManager.draw(imageContext);
        tileMap.draw(imageContext);
    }

    //Moves the player left.
    public void moveLeft() {
        tileMap.moveLeft(bgManager);
    }

    //Moves the player right.
    public void moveRight() {
        tileMap.moveRight(bgManager);
    }

    //Moves the player up/jumps.
    public void moveUp() {
        tileMap.moveUp();
    }

    //Kills the player; used in debugging.
    public void die(){
        tileMap.die();
    }

    //Performs an attack as the player.
    public void playerAttack(){
        tileMap.playerAttack();
    }

    //Updates the entities/sprites for the current level.
    public void updateSprites(){
        tileMap.updatePlayer();
        tileMap.updateEnemies();
        tileMap.updateSprites();
    }

    //Places entities based on the level.
    public void placeEntities(){
        if(currentLevel == 1){
            tileMap.placeEntities(1);
        } else if(currentLevel == 2){
            tileMap.placeEntities(2);
        } else if(currentLevel == 3){
            tileMap.placeEntities(3);
            System.out.println("Menu Level!");
        } else {
            System.out.println("Invalid level!");
        }

    }

    //Determines if the level is won.
    public boolean getLevelWon(){
        return tileMap.getLevelWon();
    }

    //Determines if the player is dead.
    public boolean isPlayerDead(){
        return tileMap.isPlayerDead();
    }
}

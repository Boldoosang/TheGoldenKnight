import javax.swing.*;

public class TrainedAssassin extends Assassin{
    public TrainedAssassin(JFrame window, TileMap t) {
        super(window, t);
        this.window = window;
        tileMap = t;			// tile map on which the player's sprite is displayed


        //Loads the animations
        loadDeathAnimation("images/characters/assassin-2.png");
        loadRunAnimation("images/characters/assassin-2.png");
        loadAttackAnimation("images/characters/assassin-2.png");

        //Sets the current animations.
        currentAnimation = runAnimation;

        //Defines assassin parameters.

        x = 0;
        y = 740;
        originalDX = 3;
        DX = originalDX;
        aggressionRange = 500;
        DEFAULT_HEALTH = 200;
        health = DEFAULT_HEALTH;
        healthbarWidth = 75;
        knockbackDistance = 40;
    }
}

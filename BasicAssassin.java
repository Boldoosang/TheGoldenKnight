import javax.swing.*;
import java.awt.geom.Rectangle2D;

public class BasicAssassin extends Assassin {
    public BasicAssassin(JFrame window, TileMap t) {
        super(window, t);
        this.window = window;
        tileMap = t;			// tile map on which the player's sprite is displayed


        //Loads the animations
        loadDeathAnimation("images/characters/assassin-1.png");
        loadRunAnimation("images/characters/assassin-1.png");
        loadAttackAnimation("images/characters/assassin-1.png");

        //Sets the current animations.
        currentAnimation = runAnimation;

        //Defines assassin parameters.
        x = 0;
        y = 740;
        originalDX = 2;
        DX = originalDX;
        aggressionRange = 400;
        DEFAULT_HEALTH = 100;
        health = DEFAULT_HEALTH;
        knockbackDistance = 50;
    }
}

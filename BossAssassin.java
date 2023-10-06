import javax.swing.*;

public class BossAssassin extends Assassin{
    private int regenRate;
    private int DEFAULT_KNOCKBACK;

    public BossAssassin(JFrame window, TileMap t) {
        super(window, t);
        this.window = window;
        tileMap = t;

        //Loads the animations
        loadDeathAnimation("images/characters/assassin-3.png");
        loadRunAnimation("images/characters/assassin-3.png");
        loadAttackAnimation("images/characters/assassin-3.png");

        //Sets the current animations.
        currentAnimation = runAnimation;

        //Defines boss parameters.
        x = 1000;
        y = 740;
        originalDX = 4;
        DX = originalDX;
        aggressionRange = 700;
        DEFAULT_HEALTH = 750;
        health = DEFAULT_HEALTH;
        healthbarWidth = 100;
        regenRate = 2;
        DEFAULT_KNOCKBACK = 30;
        knockbackDistance = DEFAULT_KNOCKBACK;
    }


    //Regenerates health as the boss.
    public void regenerateHealth(){
        if(health < DEFAULT_HEALTH){
            health += regenRate;
        }
    }

    //Updates the boss state.
    public void bossUpdate(){
        regenerateHealth();
    }
}

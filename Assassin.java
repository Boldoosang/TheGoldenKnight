import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class Assassin implements Behavior {
    protected static int XSIZE = 50;	// width of the assassin sprite
    protected static int YSIZE = 60;	// height of the assassin sprite

    protected int originalDX = 3;	// speed of the assassin

    protected int DX = originalDX;	// current speed of the assassin

    protected JFrame window;		// reference to the JFrame on which assassin is drawn
    protected TileMap tileMap;

    protected int x;			// x-position of assassin
    protected int y;			// y-position of assassin
    protected double speedUp;

    protected int updatesPassed; //Number of game updates passed.

    //Used for assassin animations.
    protected Animation deathAnimation;
    protected Animation runAnimation;
    protected Animation attackAnimation;
    protected Animation currentAnimation;

    //Behaviour booleans
    protected boolean attackMode;
    protected boolean evadeMode;

    //Range and bounds for behaviour of assassin.
    protected Integer aggressionRange;
    protected Ellipse2D aggressionBounds;

    //Assassin basic states and values.
    protected boolean dead;
    protected char direction;
    protected int DEFAULT_HEALTH;
    protected int health;
    protected int healthbarWidth;
    protected int knockbackDistance;

    private SoundManager soundManager;
    private boolean alreadyPlayedDetected;

    public Assassin(JFrame window, TileMap t) {
        this.window = window;
        tileMap = t;			// tile map on which the player's sprite is displayed

        //Loads the animations of the assassins.
        loadDeathAnimation("images/characters/assassin-1.png");
        loadRunAnimation("images/characters/assassin-1.png");
        loadAttackAnimation("images/characters/assassin-1.png");

        //Sets the current animation.
        currentAnimation = runAnimation;

        //Initializes the assassin's base state.
        x = 1400;
        y = 1200;
        attackMode = false;
        dead = false;
        updatesPassed = 0;
        direction = 'r';
        speedUp = 1.5;
        alreadyPlayedDetected = false;
        healthbarWidth = 50;
        soundManager = SoundManager.getInstance();
    }


    //Returns the hitbox of the assassin.
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x+12, y, XSIZE-24, YSIZE);
    }


    //Returns an ellipse representing the aggression of the assassin.
    public Ellipse2D.Double getAggressionRange() {
        return new Ellipse2D.Double(x-(aggressionRange/2), y-(aggressionRange/2), aggressionRange, aggressionRange);
    }

    //Updates the assassin.
    public void update(){
        //Starts the currently assigned animation.
        currentAnimation.start(XSIZE, YSIZE);
        updatesPassed++;

        //If the assassin is not dead, continue to roam the plan as long as there are tiles on the bottom before and after the assassin.
        if(!dead) {
            if(!tileMap.onCurrentTile(x + DX, getY()) && tileMap.onCurrentTile(x + 10*DX, getY()+64)) {
                x += DX;
            } else {
                DX = -DX;
            }

            //Updates the speed of the assassin once the assassin has locked unto the player.
            if(attackMode){
                DX = (int) Math.round((speedUp * originalDX));
            }
        }
    }

    //Loads the death animation from the strip file and assigns it.
    public void loadDeathAnimation(String path) {
        Image stripImage = ImageManager.loadImage(path);

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        deathAnimation = new Animation(window, false);


        for(int i=0; i <10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 4*imageHeight, (i * imageWidth) + imageWidth, (4*imageHeight)+imageHeight,
                    null);

            deathAnimation.addFrame(frameImage, 50);
        }

    }


    //Loads the run animation from the strip file and assigns it.
    public void loadRunAnimation(String path) {
        Image stripImage = ImageManager.loadImage(path);

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        runAnimation = new Animation(window, true);


        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 2 * imageHeight, (i * imageWidth) + imageWidth, (2 * imageHeight) + imageHeight,
                    null);

            runAnimation.addFrame(frameImage, 50);
        }
    }

    //Loads the attack animation from the strip file and assigns it.
    public void loadAttackAnimation(String path) {
        Image stripImage = ImageManager.loadImage(path);

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        attackAnimation = new Animation(window, true);

        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 3 * imageHeight, (i * imageWidth) + imageWidth, (3 * imageHeight) + imageHeight,
                    null);

            attackAnimation.addFrame(frameImage, 50);
        }
    }


    //Gets the X position of the assassin.
    public int getX() {
        return x;
    }

    //Sets the X position of the assassin.
    public void setX(int x) {
        this.x = x;
    }

    //Gets the Y position of the assassin.
    public int getY() {
        return y;
    }

    //Sets the Y position of the assassin.
    public void setY(int y) {
        this.y = y;
    }

    //Gets the current animation of the assassin.
    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    //Determines if the assassin is dead.
    public boolean isDead(){
        return this.dead;
    }

    //Sets the assassin to dead.
    public void setDead(boolean dead){
        this.dead = dead;

        soundManager.playSound("kill", false);
        if(dead){
            currentAnimation = deathAnimation;
        }
    }

    //Sets the attack state, animation, and updates the speed and direction of the assassin.
    public void setAttackMode(boolean state, char direction){
        attackMode = state;

        if(attackMode) {
            currentAnimation = attackAnimation;
            if (direction == 'l')
                DX = -Math.abs(DX);
            else
                DX = Math.abs(DX);
        } else {
            currentAnimation = runAnimation;
            DX = originalDX * (int)Math.signum(DX);
        }
    }

    //No longer used.
    public void setEvadeMode(boolean state, char direction){
        evadeMode = state;

        if(evadeMode) {
            currentAnimation = runAnimation;
            if (direction == 'l')
                DX = 60* Math.abs(DX);
            else
                DX = 60*-Math.abs(DX);
        } else {
            currentAnimation = runAnimation;
            DX = originalDX * (int)Math.signum(DX);
        }
    }

    //Determines if the player is within range.
    public boolean checkPlayerWithinRange(Rectangle2D playerBounds, boolean isPlayerDead){
        aggressionBounds = getAggressionRange();
        if (aggressionBounds.intersects(playerBounds) && !isPlayerDead) {
            return true;
        }
        return false;
    }

    //Default attacking behaviour of the assassin once a player is within aggression range.
    public void attackPlayerBehavior(Rectangle2D playerBounds, int playerX, boolean isPlayerDead){
        char direction = 'r';
        boolean withinRange = false;

        //Determines if a player is within range.
        if (withinRange = checkPlayerWithinRange(playerBounds, isPlayerDead)) {
            //Plays the alert sound.
            if (!alreadyPlayedDetected) {
                soundManager.playSound("detected", false);
                soundManager.playSound("enemyAttack", false);
                alreadyPlayedDetected = true;
            }

            withinRange = true;
            //Updates the direction of the player based on the movement towards the player.
            if (getX() - playerX > playerX - getX())
                direction = 'l';
            else
                direction = 'r';
        } else {
            alreadyPlayedDetected = false;
        }

        //Sets the current mode of attack of the assassin; whether to attack or not.
        setAttackMode(withinRange, direction);
    }

    //No longer used due to undesired behaviour.
    public void evadePlayerBehaviour(Rectangle2D playerBounds, int playerX, boolean isPlayerDead){
        char direction = 'r';
        boolean withinRange = false;

        if (withinRange = checkPlayerWithinRange(playerBounds, isPlayerDead)) {
            if (!alreadyPlayedDetected) {
                soundManager.playSound("detected", false);
                soundManager.playSound("enemyAttack", false);
                alreadyPlayedDetected = true;
            }

            withinRange = true;
            if (getX() - playerX > playerX - getX())
                direction = 'l';
            else
                direction = 'r';
        } else {
            alreadyPlayedDetected = false;
        }

        setEvadeMode(withinRange, direction);
    }


    //Returns the direction of the assassin based on their velocity.
    public char getDirection(){
        if(DX > 0)
            return 'r';
        else
            return 'l';
    }

    //Returns the cardinal plane weight of the direction that the assassin is facing.
    public int getDirectionWeight(){
        if(getDirection() == 'r')
            return 1;

        return -1;
    }

    //Updates the health of the assassin.
    public void updateHealth(int damage){
        health -= damage;

        if(health <= 0){
            setDead(true);
        }
    }

    //Gets the knockback distance for the assassin.
    public int getKnockbackDistance(){
        return knockbackDistance;
    }

    //Draws the health of the assassin.
    public void drawHealth(Graphics2D g, int offsetX){
        int currentHealthBar = (int)((((float)health/DEFAULT_HEALTH)*healthbarWidth));
        Rectangle2D.Double healthBar = new Rectangle2D.Double(offsetX + x - (healthbarWidth-XSIZE)/2, y-20, currentHealthBar, 8);
        g.setColor(new Color(0xDDEC0505, true));
        g.fill(healthBar);
        g.draw(healthBar);

        Rectangle2D.Double healthBarOutline = new Rectangle2D.Double(offsetX + x - (healthbarWidth-XSIZE)/2, y-20, healthbarWidth, 8);
        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(0xBE262323, true));
        g.draw(healthBarOutline);
    }
}
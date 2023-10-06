import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Player {

    private int XSIZE = 50;	// width of the player's sprite
    private int YSIZE = 60;	// height of the player's sprite

    private static final int DX = 16;	// amount of X pixels to move in one keystroke
    private static final int DY = 50;	// amount of Y pixels to move in one keystroke

    private JFrame window;		// reference to the JFrame on which player is drawn
    private TileMap tileMap;

    private int x;			// x-position of player's sprite
    private int y;			// y-position of player's sprite
    private char direction;

    private boolean jumping;
    private boolean collisionUp;
    private boolean falling;
    private boolean attacking;
    private boolean dead;
    private int timeElapsed;
    private int jumpStartY;
    private int fallTimeElapsed;
    private int damage;

    //Player animations
    private Animation idleAnimation;
    private Animation runAnimation;
    private Animation currentAnimation;
    private Animation jumpAnimation;
    private Animation attackAnimation;
    private Animation deathAnimation;

    private SoundManager soundManager;

    private int updateSinceLastMovement;
    private int screenHeight;
    private int screenWidth;

    private boolean walkEffect;

    //Creates a player and initializes its state.
    public Player (JFrame window, TileMap t) {
        this.window = window;
        tileMap = t;			// tile map on which the player's sprite is displayed

        soundManager = SoundManager.getInstance();
        jumping = false;
        collisionUp = false;
        attacking = false;
        timeElapsed = 0;
        fallTimeElapsed = 0;
        falling = false;
        direction = 'r';
        damage = 50;
        dead = false;
        walkEffect = false;
        jumpStartY = getY();

        //Loads the animations.
        loadIdleAnimation();
        loadRunAnimation();
        loadJumpAnimation();
        loadAttackAnimation();
        loadDeathAnimation();

        updateSinceLastMovement = 0;

        //Sets the initial animation.
        currentAnimation = idleAnimation;

        screenHeight = window.getHeight();
        screenWidth = window.getWidth();
    }

    //Gets hitbox of the player.
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
    }


    public Image loadImage (String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    //Performs a player attack.
    public boolean attack() {
        if (!window.isVisible ())
            return false;

        //If the player attacks from not attacking, play the full attack animation.
        if(!attacking && !dead){
            updateSinceLastMovement = 0;
            currentAnimation = attackAnimation;
            attacking = true;
            currentAnimation.start(getWidth(),getHeight());
            return true;
        } else if(attacking){
            //Otherwise, if the player is continuously attacking, repeatedly start the animation.
            currentAnimation.start(getWidth(),getHeight());
        }

        return false;
    }


    //Loads idle animation.
    public void loadIdleAnimation() {

        Image stripImage = ImageManager.loadImage("images/characters/player.png");

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        idleAnimation = new Animation(window, true);


        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 0 * imageHeight, (i * imageWidth) + imageWidth, (0 * imageHeight) + imageHeight,
                    null);

            idleAnimation.addFrame(frameImage, 50);
        }
    }

    //Loads death animation.
    public void loadDeathAnimation() {

        Image stripImage = ImageManager.loadImage("images/characters/player.png");

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        deathAnimation = new Animation(window, false);


        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 4 * imageHeight, (i * imageWidth) + imageWidth, (4 * imageHeight) + imageHeight,
                    null);

            deathAnimation.addFrame(frameImage, 120);
        }
    }

    //Loads attack animation.
    public void loadAttackAnimation() {

        Image stripImage = ImageManager.loadImage("images/characters/player.png");

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        attackAnimation = new Animation(window, false);


        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 3 * imageHeight, (i * imageWidth) + imageWidth, (3 * imageHeight) + imageHeight,
                    null);

            attackAnimation.addFrame(frameImage, 15);
        }
    }

    //Loads jump animation.
    public void loadJumpAnimation() {
        Image stripImage = ImageManager.loadImage("images/characters/player.png");

        int imageWidth = (int) stripImage.getWidth(null) / 10;
        int imageHeight = stripImage.getHeight(null) / 5;

        jumpAnimation = new Animation(window, true);


        for (int i = 0; i < 10; i++) {
            BufferedImage frameImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) frameImage.getGraphics();

            g.drawImage(stripImage,
                    0, 0, imageWidth, imageHeight,
                    i * imageWidth, 1 * imageHeight, (i * imageWidth) + imageWidth, (1 * imageHeight) + imageHeight,
                    null);

            jumpAnimation.addFrame(frameImage, 50);
        }
    }

    //Loads run animation.
    public void loadRunAnimation() {
        Image stripImage = ImageManager.loadImage("images/characters/player.png");

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


    //Moves the player left and updates the animation.
    public void moveLeft () {
        if (!window.isVisible ()) return;

        if(!dead) {
            if(!walkEffect) {
                soundManager.playSound("walk", true);
                walkEffect = true;
            }
            updateSinceLastMovement = 0;
            direction = 'l';

            if ((x - DX) > 0)
                x = x - DX;

            currentAnimation = runAnimation;
        }
        // check if x is outside the left side of the tile map
    }

    //Moves the player right and updates the animation.
    public void moveRight () {

        if (!window.isVisible ()) return;

        if(!dead) {
            if(!walkEffect) {
                soundManager.playSound("walk", true);
                walkEffect = true;
            }

            updateSinceLastMovement = 0;
            direction = 'r';

            int tileMapWidth = tileMap.getWidthPixels();
            int playerWidth = getWidth();

            if ((x + DX + playerWidth) <= tileMapWidth)
                x = x + DX;

            currentAnimation = runAnimation;
        }
    }


    //Enables the player to jump and updates the animation.
    public void moveUp () {
        if (!window.isVisible ()) return;

        updateSinceLastMovement = 0;
        if(!jumping && !collisionUp && !falling && !dead){
            soundManager.playSound("jump", false);
            currentAnimation = jumpAnimation;
            jumping = true;
            jumpStartY = getY();
            timeElapsed = 0;
            fallTimeElapsed = 0;
        }
    }

    //Kills the player and updates the animation.
    public void die () {
        if (!window.isVisible ()) return;

        dead = true;
        currentAnimation = deathAnimation;
        currentAnimation.start(getWidth(),getHeight());
    }


    //Updates the player's state using the tile above and the tile below.
    public void update(boolean groundBelow, boolean groundAbove) {
        int distance;

        //Enables attacking and walking animations to complete by allowing them to run for 3 updates after they have finished.
        updateSinceLastMovement++;

        //Once the player is not dead, jumping, or attacking, set the animation to idle.
        if(updateSinceLastMovement > 3 && !jumping && !dead && !attacking) {
            currentAnimation = idleAnimation;
            soundManager.stopSound("walk");
            walkEffect = false;
        }


        //If the player is not attacking and not dead, start the current animation.
        if(!dead && !attacking)
            currentAnimation.start(getWidth(), getHeight());

        //If the player is jumping calculate the jump and update the player.
        if (jumping) {
            timeElapsed++;
            //Used to get the halfway point of the jump.
            int halfTime = 2*timeElapsed;

            if(groundAbove){
                collisionUp = true;
                fallTimeElapsed++;

                distance = (int) (- 2.8 * fallTimeElapsed * fallTimeElapsed);

                //Ensures that no acceleration will result in the falling through of tiles.
                if(distance < -32){
                    distance = -32;
                }

                y = getY() - distance;

                //Seamless jumping at the same height.
                if(y >= jumpStartY){
                    collisionUp = false;
                    setY(jumpStartY);
                    jumping = false;
                    return;
                }
                jumping = false;

            }

            distance = (int) (50 * timeElapsed - 2.8 * timeElapsed * timeElapsed);

            //Performs falling twice as fast; when fallpoint = 0, distance will be halfway.
            int fallPoint = (int) (50 * halfTime - 2.8 * halfTime * halfTime);

            if(distance < -32){
                distance = -32;
            }

            //When fallpoint is 0, disable jumping and treat the remainder of movement as if the player is falling.
            if (fallPoint < 0){
                if(groundBelow){
                    jumping = false;
                    return;
                }
            }

            y = jumpStartY - distance;



            if (y > jumpStartY) {
                jumping = false;
                y = jumpStartY;
                return;
            }
        } else if(!groundBelow && !jumping){
            fallTimeElapsed++;
            falling = true;

            distance = (int) (- 2.8 * fallTimeElapsed * fallTimeElapsed);

            if(distance < -32){
                distance = -32;
            }

            y = y - distance;
        } else {
            collisionUp = false;
            falling = false;

            if(attacking && updateSinceLastMovement > 3) {
                attacking = false;
                currentAnimation.setAnimationStarted(false);
            }
        }

        //Resets the player to the floor height if the player falls through the map.
        if(y > screenHeight - tileMap.tilesToPixels(1) - getHeight()){
            y = screenHeight - getHeight() - tileMap.tilesToPixels(1);
        }
    }

    //Gets the X position of the player.
    public int getX() {
        return x;
    }


    //Sets the X position of the player in pixels.
    public void setX(int x) {
        this.x = x;
    }

    //Sets the fall time elapsed for the player.
    public void setFallTimeElapsed(int fallTimeElapsed){
        this.fallTimeElapsed = fallTimeElapsed;
    }

    //Gets the Y position of the player.
    public int getY() {
        return y;
    }

    //Gets the speed of the player in the X axis.
    public int getDX(){
        return DX;
    }

    //Sets the Y position of the player in pixels.
    public void setY(int y) {
        this.y = y;
    }

    //Returns the width of the player.
    public int getWidth(){return XSIZE; }

    //Returns the height of the player.
    public int getHeight(){return YSIZE; }

    //Returns the current animation of the player.
    public Animation getCurrentAnimation(){
        return currentAnimation;
    }

    //Returns the direction of the player.
    public char getDirection(){
        return direction;
    }

    //Returns the alive state of the player.
    public boolean isDead(){
        return dead;
    }

    //Returns the damage of the player's attack.
    public int getDamage(){
        return damage;
    }

    //Updates the player's falling state.
    public void setPlayerFalling(boolean falling){
        this.falling = falling;
    }
}

/* BackgroundManager manages many backgrounds (wraparound images 
   used for the game's background). 

   Backgrounds 'further back' move slower than ones nearer the
   foreground of the game, creating a parallax distance effect.

   When a sprite is instructed to move left or right, the sprite
   doesn't actually move, instead the backgrounds move in the 
   opposite direction (right or left).

*/

import java.awt.Graphics2D;
import javax.swing.JFrame;


public class BackgroundManager {
    //Sets the level backgrounds.
    private String bgImages[];
    private String level1BG[] = {
            "images/backgrounds/layer_08.png",
            "images/backgrounds/layer_07.png",
            "images/backgrounds/layer_06.png",
            "images/backgrounds/layer_05.png",
            "images/backgrounds/layer_04.png",
            "images/backgrounds/layer_03.png",
            "images/backgrounds/layer_02.png",
            "images/backgrounds/layer_01.png"};

    private String level3BG[] = {
            "images/backgrounds/layer2_07.png",
            "images/backgrounds/layer2_06.png",
            "images/backgrounds/layer2_05.png",
            "images/backgrounds/layer2_04.png",
            "images/backgrounds/layer2_03.png",
            "images/backgrounds/layer2_02.png",
            "images/backgrounds/layer2_01.png"};

    private String level2BG[] = {
            "images/backgrounds/L1.png",
            "images/backgrounds/L2.png",
            "images/backgrounds/L3.png",
            "images/backgrounds/L4.png",
            "images/backgrounds/L5.png",};

    private int moveAmount[] = {1, 2, 3, 4, 4, 4, 5, 10};
    // pixel amounts to move each background left or right
    // a move amount of 0 makes a background stationary

    private Background[] backgrounds;
    private int numBackgrounds;

    private JFrame window;			// JFrame on which backgrounds are drawn

    public BackgroundManager(JFrame window, int level) {
        this.window = window;

        //Determines which backgrounds to use based on the level.
        if(level == 1){
            bgImages = level1BG;
        } else if(level == 2){
            bgImages = level2BG;
        } else {
            bgImages = level3BG;
        }

        numBackgrounds = bgImages.length;
        backgrounds = new Background[numBackgrounds];


        for (int i = 0; i < numBackgrounds; i++) {
            backgrounds[i] = new Background(window, bgImages[i], moveAmount[i]);
        }
    }


    public void moveRight() {
        for (int i=0; i < numBackgrounds; i++)
            backgrounds[i].moveRight();
    }


    public void moveLeft() {
        for (int i=0; i < numBackgrounds; i++)
            backgrounds[i].moveLeft();
    }


    // The draw method draws the backgrounds on the screen. The
    // backgrounds are drawn from the back to the front.

    public void draw (Graphics2D g2) {
        for (int i=0; i < numBackgrounds; i++)
            backgrounds[i].draw(g2);
    }
}


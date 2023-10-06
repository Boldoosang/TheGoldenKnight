import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for certain AWT classes
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.awt.image.BufferStrategy;	// need this to implement page flipping
import java.awt.image.RescaleOp;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class GameWindow extends JFrame implements
        Runnable,
        KeyListener,
        MouseListener,
        MouseMotionListener
{
    private static final int NUM_BUFFERS = 2;	// used for page flipping

    private int pWidth, pHeight;     		// width and height of screen

    private Thread gameThread = null;            	// the thread that controls the game
    private volatile boolean isRunning = false;    	// used to stop the game thread

    private BufferedImage image;			// drawing area for each frame

    private Image gameLogo;
    private Image completedChalice;

    private Image quit0Image;			// first image for quit button
    private Image quit1Image;			// second image for quit button

    private Image pause0Image;			// first image for pause button
    private Image pause1Image;			// second image for pause button

    private Image resume0Image;			// first image for resume button
    private Image resume1Image;			// second image for resume button

    private Image play0Image;			// first image for play button
    private Image play1Image;			// second image for play button

    private Image restart0Image;			// first image for restart button
    private Image restart1Image;			// second image for restart button


    private boolean finishedOff = false;		// used when the game terminates
    private boolean levelWon;
    private boolean gameWon = false;
    private volatile boolean gameStarted = false;
    private int colorSeq;

    private volatile boolean isOverQuitButton = false;
    private Rectangle quitButtonArea;		// used by the quit button

    private volatile boolean isOverPauseButton = false;
    private Rectangle pauseButtonArea;		// used by the pause 'button'
    private volatile boolean isPaused = false;

    private boolean playerDied;
    private int colorDiff = 5;

    private volatile boolean isOverRestartButton = false;
    private Rectangle restartButtonArea;		// used by the restart 'button'

    private volatile boolean isOverPlayButton = false;
    private Rectangle playButtonArea;		// used by the play 'button'

    private GraphicsDevice device;			// used for full-screen exclusive mode
    private Graphics gScr;
    private BufferStrategy bufferStrategy;

    private boolean controllerLeft;
    private boolean controllerRight;
    private boolean controllerJump;
    private boolean controllerAttack;

    private SoundManager soundManager;
    private Level currentLevel;
    private int currentLevelNum;
    private boolean playingOutcomeMusic;
    private float logoOpacity;
    private float logoOpacityChange;

    private Scoreboard scoreboard;

    //Initializes the game window.
    public GameWindow() {
        //Sets the title of the window.
        super("The Golden Knight - The Chalice of Ruins");

        initFullScreen();

        //Loads the UI Images
        quit0Image = loadImage("images/buttons/Quit0.png");
        quit1Image = loadImage("images/buttons/Quit1.png");

        play0Image = loadImage("images/buttons/Play0.png");
        play1Image = loadImage("images/buttons/Play1.png");

        pause0Image = loadImage("images/buttons/Pause0.png");
        pause1Image = loadImage("images/buttons/Pause1.png");

        resume0Image = loadImage("images/buttons/Resume0.png");
        resume1Image = loadImage("images/buttons/Resume1.png");

        restart0Image = loadImage("images/buttons/Restart0.png");
        restart1Image = loadImage("images/buttons/Restart1.png");

        gameLogo = loadImage("images/buttons/logo.png");
        completedChalice = loadImage("images/sprites/completedChalice.png");

        //Creates the scoreboard.
        scoreboard = new Scoreboard(this);

        //Resets the scoreboard.
        scoreboard.resetScore();

        //Defines the button areas.
        setButtonAreas();

        //Adds the listeners to the window.
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        //Creates a new level with the scoreboard.
        currentLevelNum = 1;
        currentLevel = new Level(this, scoreboard);

        //Initializes the game variables.
        soundManager = SoundManager.getInstance();
        image = new BufferedImage (pWidth, pHeight, BufferedImage.TYPE_INT_RGB);
        levelWon = false;
        currentLevelNum = 1;
        playerDied = false;
        colorSeq = 0;
        playingOutcomeMusic = false;
        controllerLeft = false;
        controllerRight = false;
        controllerJump = false;
        controllerAttack = false;
        logoOpacity = 1f;
        logoOpacityChange = -1f;

        //Starts the game.
        startGame();
    }


    // implementation of Runnable interface
    public void run () {
        try {
            isRunning = true;
            while (isRunning) {
                if (isPaused == false) {
                    gameUpdate();
                }
                screenUpdate();
                Thread.sleep (8);
            }
        }
        catch(InterruptedException e) {}

        finishOff();
    }


	/* This method performs some tasks before closing the game.
	   The call to System.exit() should not be necessary; however,
	   it prevents hanging when the game terminates.
	*/

    private void finishOff() {
        if (!finishedOff) {
            finishedOff = true;
            restoreScreen();
            System.exit(0);
        }
    }


	/* This method switches off full screen mode. The display
	   mode is also reset if it has been changed.
	*/

    private void restoreScreen() {
        Window w = device.getFullScreenWindow();

        if (w != null)
            w.dispose();

        device.setFullScreenWindow(null);
    }


    //Updates the game logic.
    public void gameUpdate () {
        //Handles user input.
        handleInput();

        //Updates the sprites of the level.
        currentLevel.updateSprites();

        //Gets state of game information.
        levelWon = currentLevel.getLevelWon();
        playerDied = currentLevel.isPlayerDead();

        //Restart the level if a user loses a life.
        if(scoreboard.getLostLifeFlag()){
            scoreboard.clearLostLifeFlag();
            restartLevel();
        }

        //Move to next level if the user has won.
        if(levelWon){
            currentLevelNum += 1;
            //If the user has won the second level and is now on the third level, set the gameWon to true.
            if(levelWon && currentLevelNum == 3)
                gameWon = true;

            levelWon = false;
            currentLevel.loadLevel(currentLevelNum);
        }
    }


    private void screenUpdate() {

        try {
            gScr = bufferStrategy.getDrawGraphics();
            gameRender(gScr);
            gScr.dispose();
            if (!bufferStrategy.contentsLost())
                bufferStrategy.show();
            else
                System.out.println("Contents of buffer lost.");

            Toolkit.getDefaultToolkit().sync();
        }
        catch (Exception e) {
            e.printStackTrace();
            isRunning = false;
        }
    }

    //Determines when the show win and lose messages. Also displays credits.
    public void displayContextMessages(Graphics2D imageContext){
        if(gameWon){
            winMessage(imageContext);
        } else if(playerDied){
            gameOverMessage(imageContext);
        }

        credits(imageContext);
    }


    public void gameRender (Graphics gScr) {
        //Gets the main image to be displayed on the screen.
        Graphics2D imageContext = (Graphics2D) image.getGraphics();

        //Individual draw each context, message, and entities to the canvas.
        currentLevel.draw(imageContext);
        scoreboard.draw(imageContext);
        drawButtons(imageContext);
        displayContextMessages(imageContext);

        Graphics2D g2 = (Graphics2D) gScr;
        //Draws the canvas contents to the screen all at once.
        g2.drawImage(image, 0, 0, pWidth, pHeight, null);

        imageContext.dispose();
        g2.dispose();
    }


    private void initFullScreen() {				// standard procedure to get into FSEM

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = ge.getDefaultScreenDevice();

        setUndecorated(true);	// no menu bar, borders, etc.
        setIgnoreRepaint(true);	// turn off all paint events since doing active rendering
        setResizable(false);	// screen cannot be resized

        if (!device.isFullScreenSupported()) {
            System.out.println("Full-screen exclusive mode not supported");
            System.exit(0);
        }

        device.setFullScreenWindow(this); // switch on full-screen exclusive mode

        // we can now adjust the display modes, if we wish

        showCurrentMode();

        pWidth = getBounds().width;
        pHeight = getBounds().height;

        System.out.println("Width of window is " + pWidth);
        System.out.println("Height of window is " + pHeight);

        try {
            createBufferStrategy(NUM_BUFFERS);
        }
        catch (Exception e) {
            System.out.println("Error while creating buffer strategy " + e);
            System.exit(0);
        }

        bufferStrategy = getBufferStrategy();
    }


    // This method provides details about the current display mode.
    private void showCurrentMode() {
        DisplayMode dms[] = device.getDisplayModes();

        for (int i=0; i<dms.length; i++) {
            System.out.println("Display Modes Available: (" +
                    dms[i].getWidth() + "," + dms[i].getHeight() + "," +
                    dms[i].getBitDepth() + "," + dms[i].getRefreshRate() + ")  " );
        }

        DisplayMode dm = device.getDisplayMode();

        System.out.println("Current Display Mode: (" +
                dm.getWidth() + "," + dm.getHeight() + "," +
                dm.getBitDepth() + "," + dm.getRefreshRate() + ")  " );
    }


    // Specify screen areas for the buttons and create bounding rectangles

    private void setButtonAreas() {
        //Places the button rectangle areas.

        int leftOffset = (pWidth - (3 * 180) - (4 * 20)) / 2;
        pauseButtonArea = new Rectangle(leftOffset, 60, 180, 50);

        leftOffset = leftOffset + 200;
        restartButtonArea = new Rectangle(leftOffset, 60, 180, 50);

        leftOffset = leftOffset + 200;
        quitButtonArea = new Rectangle(leftOffset, 60, 180, 50);

        playButtonArea = new Rectangle(pWidth/2 - 175, 150, 350, 97);
    }


    public Image loadImage (String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    private void drawButtons (Graphics2D g) {
        Font oldFont, newFont;

        oldFont = g.getFont();		// save current font to restore when finished

        newFont = new Font ("TimesRoman", Font.ITALIC + Font.BOLD, 18);
        g.setFont(newFont);		// set this as font for text on buttons

        //Draws Play Button
        if (!gameStarted) {
            g.drawImage(play0Image, playButtonArea.x, playButtonArea.y, 350, 97, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, logoOpacity));

            logoOpacity += 0.02f * logoOpacityChange;
            if(logoOpacity >= 1.00f || logoOpacity <= 0.50f){
                logoOpacityChange = -logoOpacityChange;
            }
            g.drawImage(gameLogo, pWidth/2 - 400, (int)(playButtonArea.y + playButtonArea.getHeight()) + 50, 800, 196, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }

        if (!gameStarted && isOverPlayButton)
            g.drawImage(play1Image, playButtonArea.x, playButtonArea.y, 350, 97, null);

        //Draws Pause Buttons
        if (!isPaused)
            g.drawImage(pause0Image, pauseButtonArea.x, pauseButtonArea.y, 180, 50, null);

        if (!isPaused && isOverPauseButton)
            g.drawImage(pause1Image, pauseButtonArea.x, pauseButtonArea.y, 180, 50, null);

        //Draws Resume Buttons
        if (isPaused)
            g.drawImage(resume0Image, pauseButtonArea.x, pauseButtonArea.y, 180, 50, null);

        if (isPaused && isOverPauseButton)
            g.drawImage(resume1Image, pauseButtonArea.x, pauseButtonArea.y, 180, 50, null);


        //Draws Restart Buttons
        if (isOverRestartButton)
            g.drawImage(restart1Image, restartButtonArea.x, restartButtonArea.y, 180, 50, null);
        else
            g.drawImage(restart0Image, restartButtonArea.x, restartButtonArea.y, 180, 50, null);

        //Draws quit button.
        if (isOverQuitButton)
            g.drawImage(quit1Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);
        else
            g.drawImage(quit0Image, quitButtonArea.x, quitButtonArea.y, 180, 50, null);

        g.setFont(oldFont);		// reset font

    }

    //Starts the game on the menu screen.
    private void startGame() {
        currentLevel.loadLevel(3);
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    //Restarts the game when the user has lost all lives and presses restart.
    private void restartGame() {
        currentLevelNum = 1;
        gameWon = false;

        scoreboard.resetScore();
        playingOutcomeMusic = false;
        currentLevel.loadLevel(currentLevelNum);
        playingOutcomeMusic = false;
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    //Restarts the level when the user loses a life.
    private void restartLevel() {
        currentLevel.loadLevel(currentLevelNum);
    }

    //Prints the credits at the top left of the screen.
    private void credits(Graphics2D g){
        Font font = new Font("Bahnschrift", Font.PLAIN, 20);
        Random rand = new Random();
        g.setColor(new Color(0x11E10C));
        g.setFont(font);

        g.drawString("816021226 - Justin Baldeosingh", 10, 25);
        g.drawString("COMP 3609 - Game Programming", 10, 50);
        g.drawString("Made with free Internet Resources", 10, 75);


    }

    //Displays the win message.
    private void winMessage(Graphics2D g){
        //Ensures that the outcome music is played once and not constantly starting.
        if(!playingOutcomeMusic) {
            playingOutcomeMusic = true;
            soundManager.stopAllSounds();
            soundManager.playSound("win", false);
            soundManager.playSound("achievement", false);
        }

        Font font = new Font("Segoe UI Black", Font.PLAIN, 64);
        FontMetrics metrics = this.getFontMetrics(font);

        String msg = "You won! Thanks for playing!";

        int x = (pWidth - metrics.stringWidth(msg)) / 2;
        int y = (pHeight - metrics.getHeight()) / 2;
        int widthOffset = 30;
        int heightOffset = 30;

        Rectangle2D.Double scoreboardBackground = new Rectangle2D.Double(x, y, metrics.stringWidth(msg)+widthOffset, 2*metrics.getHeight() + heightOffset);
        g.setColor(new Color(0xB830882E, true));
        g.fill(scoreboardBackground);
        g.draw(scoreboardBackground);

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(0xBE262323, true));
        g.draw(scoreboardBackground);

        //Fades and unfades the color for the text, from black to white and back.
        if(colorSeq < 0) {
            colorSeq = 0;
            colorDiff *= -1;
        }
        if(colorSeq >= 255){
            colorDiff *= -1;
        }

        g.setColor(new Color(colorSeq, colorSeq, colorSeq));
        g.setFont(font);
        g.drawString(msg, x+widthOffset/2, (int)(y+(scoreboardBackground.getHeight()/2.5)));

        colorSeq += colorDiff;

        //Displays the score of the player upon completion.
        String scoreMessage = "Score: " + scoreboard.getScore() + " with " + scoreboard.getLives() + " lives!";
        g.drawString(scoreMessage, ((pWidth - metrics.stringWidth(scoreMessage)) / 2), (int)(y+(scoreboardBackground.getHeight()/2) + 30 + heightOffset));

        g.drawImage(completedChalice, (pWidth-80)/2,pHeight-TileMap.TILE_SIZE-100, 80, 100, null);


        //Displays the achievements of the player.
        HashMap<String, Boolean> achievements = scoreboard.getAchievements();

        Image flawlessAchievement = scoreboard.flawlessAchievementImage;
        Image pacifistAchievement = scoreboard.pacifistAchievementImage;
        Image completedAchievement = scoreboard.completedAchievementImage;

        int achievementWidth = flawlessAchievement.getWidth(null);
        int achievementHeight = flawlessAchievement.getHeight(null);

        int achievementAdjustedWidth = 350;
        int achievementAdjustedHeight = (int)(achievementHeight * ((float)achievementAdjustedWidth/achievementWidth));

        AtomicInteger currentAchievement = new AtomicInteger(0);
        g.drawImage(completedAchievement, pWidth - 15 - achievementAdjustedWidth, scoreboard.getX() + (currentAchievement.get() * achievementAdjustedHeight) + scoreboard.getScoreboardSizeY() + 60, achievementAdjustedWidth, achievementAdjustedHeight, null);
        currentAchievement.getAndIncrement();
        //For each of the achievements, draw its image on the right side of the display.
        achievements.forEach((key, unlocked) -> {
            if(key == "flawless" && unlocked){
                g.drawImage(flawlessAchievement, pWidth - 15 - achievementAdjustedWidth, scoreboard.getX() + (currentAchievement.get() * achievementAdjustedHeight) + scoreboard.getScoreboardSizeY() + 60, achievementAdjustedWidth, achievementAdjustedHeight, null);
                currentAchievement.getAndIncrement();
            }

            if(key == "pacifist" && unlocked){

                g.drawImage(pacifistAchievement, pWidth - 15 - achievementAdjustedWidth, scoreboard.getX() + (currentAchievement.get() * achievementAdjustedHeight) + scoreboard.getScoreboardSizeY() + 60, achievementAdjustedWidth, achievementAdjustedHeight, null);
                currentAchievement.getAndIncrement();
            }
        });


    }

    //Displays the game over message.
    private void gameOverMessage(Graphics2D g) {
        if(!playingOutcomeMusic) {
            soundManager.stopAllSounds();
            soundManager.playSound("lose", false);
            playingOutcomeMusic = true;
        }
        Font font = new Font("Segoe UI Black", Font.PLAIN, 64);
        FontMetrics metrics = this.getFontMetrics(font);

        String msg = "You died. Press restart to try again!";

        int x = (pWidth - metrics.stringWidth(msg)) / 2;
        int y = (pHeight - metrics.getHeight()) / 2;
        int widthOffset = 30;
        int heightOffset = 30;

        Rectangle2D.Double scoreboardBackground = new Rectangle2D.Double(x, y, metrics.stringWidth(msg)+widthOffset, 2*metrics.getHeight() + heightOffset);
        g.setColor(new Color(0x8EBB769A, true));
        g.fill(scoreboardBackground);
        g.draw(scoreboardBackground);

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(0xBE262323, true));
        g.draw(scoreboardBackground);

        g.setColor(new Color(0x810202));
        g.setFont(font);
        g.drawString(msg, x+widthOffset/2, (int)(y+(scoreboardBackground.getHeight()/2.5)));

        String scoreMessage = "Score: " + scoreboard.getScore();
        g.drawString(scoreMessage, ((pWidth - metrics.stringWidth(scoreMessage)) / 2), (int)(y+(scoreboardBackground.getHeight()/2) + 30 + heightOffset));

    }

    //Handle the player's movement through bypassing the java key listeners.
    public void handleInput(){
        if(!playerDied) {
            if (controllerLeft) {
                currentLevel.moveLeft();
            }
            if (controllerRight) {
                currentLevel.moveRight();
            }
            if (controllerJump) {
                currentLevel.moveUp();
            }
            if (controllerAttack) {
                currentLevel.playerAttack();
            }
        }
    }

    // implementation of methods in KeyListener interface

    public void keyPressed (KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_P){
            isPaused = !isPaused;
        }

        if (isPaused)
            return;

        if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END)) {
            isRunning = false;		// user can quit anytime by pressing
            return;				//  one of these keys (ESC, Q, END)
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            controllerLeft = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            controllerRight = true;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            controllerJump = true;
        }
        if(keyCode == KeyEvent.VK_Z){
            controllerAttack = true;
        }
    }


    public void keyReleased (KeyEvent e) {
        if (isPaused)
            return;

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            controllerLeft = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            controllerRight = false;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            controllerJump = false;
        }
        if(keyCode == KeyEvent.VK_Z){
            controllerAttack = false;
        }
    }


    public void keyTyped (KeyEvent e) {

    }


    // implement methods of MouseListener interface

    public void mouseClicked(MouseEvent e) {

    }


    public void mouseEntered(MouseEvent e) {

    }


    public void mouseExited(MouseEvent e) {

    }


    public void mousePressed(MouseEvent e) {
        testMousePress(e.getX(), e.getY());
    }


    public void mouseReleased(MouseEvent e) {

    }


    // implement methods of MouseMotionListener interface

    public void mouseDragged(MouseEvent e) {

    }


    public void mouseMoved(MouseEvent e) {
        testMouseMove(e.getX(), e.getY());
    }


	/* This method handles mouse clicks on one of the buttons
	   (Pause, Stop, Start Anim, Pause Anim, and Quit).
	*/

    private void testMousePress(int x, int y) {
        if (isOverRestartButton) {			// mouse click on Stop button
            gameStarted = true;
            isPaused = false;
            playerDied = false;
            playingOutcomeMusic = false;
            restartGame();
            scoreboard.resetScore();
        }
        else
        if (isOverPauseButton) {		// mouse click on Pause button
            isPaused = !isPaused;     	// toggle pausing
        }
        else
        if (isOverPlayButton && !gameStarted) {		// mouse click on Pause Anim button
            restartGame();
            scoreboard.resetScore();
            scoreboard.showScoreboard(true);
            gameStarted = true;
            playerDied = false;
            playingOutcomeMusic = false;
        }
        else if (isOverQuitButton) {		// mouse click on Quit button
            isRunning = false;		// set running to false to terminate
        }
    }


	/* This method checks to see if the mouse is currently moving over one of
	   the buttons (Pause, Stop, Show Anim, Pause Anim, and Quit). It sets a
	   boolean value which will cause the button to be displayed accordingly.
	*/

    private void testMouseMove(int x, int y) {
        if (isRunning) {
            isOverPauseButton = pauseButtonArea.contains(x,y) ? true : false;
            isOverRestartButton = restartButtonArea.contains(x,y) ? true : false;
            isOverPlayButton = playButtonArea.contains(x,y) ? true : false;
            isOverQuitButton = quitButtonArea.contains(x,y) ? true : false;
        }
    }

}
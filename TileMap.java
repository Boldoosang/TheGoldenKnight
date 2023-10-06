import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;
import javax.swing.*;

/**
    The TileMap class contains the data for a tile-based
    map, including Sprites. Each tile is a reference to an
    Image. Images are used multiple times in the tile map.
    map.
*/

public class TileMap {

    public static final int TILE_SIZE = 64;

    private Image[][] tiles;
    private int mapWidth;
    private static int mapHeight;

    public Player player;
    private Chalice chalice;
    public Assassin[] mainAssassins;

    private JFrame window;
    private Dimension dimension;
    private boolean playerFalling;
    private int numAssassins;
    private boolean levelWon;

    private Image backgroundEntity;

    private static int screenHeight;
    private int screenWidth;

    private SoundManager soundManager;

    private int screenLeftParallax;
    private int screenRightParallax;

    private Scoreboard scoreboard;

    /**
        Creates a new TileMap with the specified width and
        height (in number of tiles) of the map.
    */
    public TileMap(JFrame window, int width, int height, Scoreboard scoreboard) {
        soundManager = SoundManager.getInstance();
        this.scoreboard = scoreboard;
        this.window = window;
        dimension = window.getSize();

        screenHeight = dimension.height;
        screenWidth = dimension.width;

        mapWidth = width;
        mapHeight = height;

        tiles = new Image[mapWidth][mapHeight];
        levelWon = false;
    }

    //Places the entities for a specific level.
    public void placeEntities(int level){
        if(level == 1) {
            numAssassins = 12;
            player = new Player (window, this);
            chalice = new Chalice();
            mainAssassins = new Assassin[numAssassins];

            for (int i = 0; i < numAssassins; i++) {
                if (i < 7)
                    mainAssassins[i] = new BasicAssassin(window, this);
                else if (i < 11)
                    mainAssassins[i] = new TrainedAssassin(window, this);
                else if (i < 12)
                    mainAssassins[i] = new BossAssassin(window, this);
            }

            backgroundEntity = loadImage("images/forest.png");

            chalice.setChalice(1);
            chalice.setX(calcSpawnXPixels(64));
            //chalice.setX(calcSpawnXPixels(2));
            chalice.setY(calcSpawnYPixels(5));

            player.setX(calcSpawnXPixels(1));
            player.setY(calcSpawnYPixels(4));

            //Basic Assassins
            mainAssassins[0].setY(calcSpawnYPixels(2));
            mainAssassins[0].setX(calcSpawnXPixels(7));

            mainAssassins[1].setY(calcSpawnYPixels(4));
            mainAssassins[1].setX(calcSpawnXPixels(12));

            mainAssassins[2].setY(calcSpawnYPixels(3));
            mainAssassins[2].setX(calcSpawnXPixels(22));

            mainAssassins[3].setY(calcSpawnYPixels(7));
            mainAssassins[3].setX(calcSpawnXPixels(38));

            mainAssassins[4].setY(calcSpawnYPixels(7));
            mainAssassins[4].setX(calcSpawnXPixels(39));

            mainAssassins[5].setY(calcSpawnYPixels(7));
            mainAssassins[5].setX(calcSpawnXPixels(40));

            mainAssassins[6].setY(calcSpawnYPixels(7));
            mainAssassins[6].setX(calcSpawnXPixels(41));

            //Trained Assassins
            mainAssassins[7].setY(calcSpawnYPixels(2));
            mainAssassins[7].setX(calcSpawnXPixels(17));

            mainAssassins[8].setY(calcSpawnYPixels(2));
            mainAssassins[8].setX(calcSpawnXPixels(25));

            mainAssassins[9].setY(calcSpawnYPixels(2));
            mainAssassins[9].setX(calcSpawnXPixels(36));

            mainAssassins[10].setY(calcSpawnYPixels(2));
            mainAssassins[10].setX(calcSpawnXPixels(41));

            //Boss Assassins
            mainAssassins[11].setY(calcSpawnYPixels(2));
            mainAssassins[11].setX(calcSpawnXPixels(53));

            playerFalling = false;
        } else if(level == 2){
            numAssassins = 24;

            player = new Player (window, this);
            chalice = new Chalice();

            mainAssassins = new Assassin[numAssassins];

            for (int i = 0; i < numAssassins; i++) {
                if (i < 4)
                    mainAssassins[i] = new BasicAssassin(window, this);
                else if (i < 20)
                    mainAssassins[i] = new TrainedAssassin(window, this);
                else if (i < 24)
                    mainAssassins[i] = new BossAssassin(window, this);
            }

            player.setX(calcSpawnXPixels(1));
            player.setY(calcSpawnYPixels(5));

            chalice.setChalice(2);
            //chalice.setX(calcSpawnXPixels(2));
            chalice.setX(calcSpawnXPixels(108));
            chalice.setY(calcSpawnYPixels(7));

            //Basic Assassins
            mainAssassins[0].setY(calcSpawnYPixels(2));
            mainAssassins[0].setX(calcSpawnXPixels(7));

            mainAssassins[1].setY(calcSpawnYPixels(2));
            mainAssassins[1].setX(calcSpawnXPixels(10));

            mainAssassins[2].setY(calcSpawnYPixels(2));
            mainAssassins[2].setX(calcSpawnXPixels(13));

            mainAssassins[3].setY(calcSpawnYPixels(2));
            mainAssassins[3].setX(calcSpawnXPixels(16));

            //Trained Assassins
            mainAssassins[4].setY(calcSpawnYPixels(5));
            mainAssassins[4].setX(calcSpawnXPixels(8));

            mainAssassins[5].setY(calcSpawnYPixels(4));
            mainAssassins[5].setX(calcSpawnXPixels(15));

            mainAssassins[6].setY(calcSpawnYPixels(9));
            mainAssassins[6].setX(calcSpawnXPixels(28));

            mainAssassins[7].setY(calcSpawnYPixels(10));
            mainAssassins[7].setX(calcSpawnXPixels(33));

            mainAssassins[8].setY(calcSpawnYPixels(9));
            mainAssassins[8].setX(calcSpawnXPixels(37));

            mainAssassins[9].setY(calcSpawnYPixels(8));
            mainAssassins[9].setX(calcSpawnXPixels(42));

            mainAssassins[10].setY(calcSpawnYPixels(7));
            mainAssassins[10].setX(calcSpawnXPixels(49));

            mainAssassins[11].setY(calcSpawnYPixels(6));
            mainAssassins[11].setX(calcSpawnXPixels(54));

            mainAssassins[12].setY(calcSpawnYPixels(6));
            mainAssassins[12].setX(calcSpawnXPixels(58));

            mainAssassins[13].setY(calcSpawnYPixels(6));
            mainAssassins[13].setX(calcSpawnXPixels(62));

            mainAssassins[15].setY(calcSpawnYPixels(3));
            mainAssassins[15].setX(calcSpawnXPixels(93));

            mainAssassins[14].setY(calcSpawnYPixels(6));
            mainAssassins[14].setX(calcSpawnXPixels(71));

            mainAssassins[16].setY(calcSpawnYPixels(2));
            mainAssassins[16].setX(calcSpawnXPixels(80));

            mainAssassins[17].setY(calcSpawnYPixels(3));
            mainAssassins[17].setX(calcSpawnXPixels(77));

            mainAssassins[18].setY(calcSpawnYPixels(8));
            mainAssassins[18].setX(calcSpawnXPixels(92));

            mainAssassins[19].setY(calcSpawnYPixels(6));
            mainAssassins[19].setX(calcSpawnXPixels(88));


            //Boss Assassins
            mainAssassins[20].setY(calcSpawnYPixels(2));
            mainAssassins[20].setX(calcSpawnXPixels(70));

            mainAssassins[21].setY(calcSpawnYPixels(6));
            mainAssassins[21].setX(calcSpawnXPixels(88));

            mainAssassins[22].setY(calcSpawnYPixels(2));
            mainAssassins[22].setX(calcSpawnXPixels(97));

            mainAssassins[23].setY(calcSpawnYPixels(2));
            mainAssassins[23].setX(calcSpawnXPixels(100));


        } else if(level == 3){
            player = new Player (window, this);

            player.setX(calcSpawnXPixels(1));
            player.setY(calcSpawnYPixels(5));
        } else {
            System.out.println("Invalid level!");
        }

        soundManager.playSound("respawn", false);
    }

    /**
        Gets the width of this TileMap (number of pixels across).
    */
    public int getWidthPixels() {
	return tilesToPixels(mapWidth);
    }


    /**
        Gets the width of this TileMap (number of tiles across).
    */
    public int getWidth() {
        return mapWidth;
    }


    /**
        Gets the height of this TileMap (number of tiles down).
    */
    public static int getHeight() {
        return mapHeight;
    }


    /**
        Gets the tile at the specified location. Returns null if
        no tile is at the location or if the location is out of
        bounds.
    */
    public Image getTile(int x, int y) {
        if (x < 0 || x >= mapWidth ||
            y < 0 || y >= mapHeight)
        {
            return null;
        }
        else {
            return tiles[x][y];
        }
    }


    /**
        Sets the tile at the specified location.
    */
    public void setTile(int x, int y, Image tile) {
        tiles[x][y] = tile;
    }

    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }


    /**
        Class method to convert a pixel position to a tile position.
    */

    public static int pixelsToTiles(int pixels) {
        return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
        Class method to convert a tile position to a pixel position.
    */

    public static int tilesToPixels(int numTiles) {
        return numTiles * TILE_SIZE;
    }

    /**
        Draws the specified TileMap.
    */
    public void draw(Graphics2D g2)
    {
        int mapWidthPixels = tilesToPixels(mapWidth);
	    int screenWidth = dimension.width;
	    int screenHeight = dimension.height;

        // get the scrolling position of the map
        // based on player's position
        int offsetX = 0;
        if(player != null) {
            offsetX = screenWidth / 2 - Math.round(player.getX()) - TILE_SIZE;
            offsetX = Math.min(offsetX, 0);
            offsetX = Math.max(offsetX, screenWidth - mapWidthPixels);
        }

        // get the y offset to draw all sprites and tiles

        int offsetY = screenHeight - tilesToPixels(mapHeight);

        // draw the visible tiles

        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX +
            pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<mapHeight; y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = getTile(x, y);
                if (image != null) {
                    g2.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
        }

        //Updates the player animations.
        if(player != null)
            getPlayerAnimation().update(Math.round(player.getX() + offsetX), Math.round(player.getY()));

        //Updates the assassin animations.
        int finalOffsetX = offsetX;
        if(mainAssassins != null){
            for(Assassin a: mainAssassins){
                if(a != null)
                    a.getCurrentAnimation().update(Math.round(a.getX() + finalOffsetX), Math.round(a.getY()));
            }
        };


        // Draws rotating chalice
        if(chalice != null)
            g2.drawImage(chalice.getImage(),
                    Math.round(chalice.getX()) + offsetX,
                    Math.round(chalice.getY()), //+ offsetY,
                    chalice.getWidth(),chalice.getHeight(),null);

        drawSprites(g2, offsetX);

    }

    public void drawSprites(Graphics2D imageContext, int offsetX){
        Animation playerAnimation = getPlayerAnimation();

        //Draws the player animation.
        if(playerAnimation != null)
            playerAnimation.draw(imageContext, getPlayerDirection());

        //Draws the assassin animations.
        if(mainAssassins != null){
            for(Assassin a: mainAssassins){
                if(a != null) {
                    a.getCurrentAnimation().draw(imageContext, a.getDirection());
                    if(!a.isDead())
                        a.drawHealth(imageContext, offsetX);
                }
            }
        }
    }

    //Updates the sprites.
    public void updateSprites(){
        //Updates the chalice.
        if(chalice != null) {
            chalice.update();
        }
    }

    //Kills the player. Debug function.
    public void die(){
        if(player == null)
            return;
        player.die();
    }

    //Gets the area of attacking for the player; roughly 25 pixels.
    public Rectangle2D.Double getAttackingArea(char direction){

        int attackingX = player.getX();
        int attackingY = player.getY();
        int attackingRange = 25;

        if(direction == 'l'){
            return new Rectangle2D.Double (attackingX - attackingRange, attackingY, attackingRange, player.getHeight());
        } else {
            attackingX = player.getX() + player.getWidth();

            return new Rectangle2D.Double (attackingX, attackingY, attackingRange, player.getHeight());
        }



    }

    //Moves the player left; moves the background right;
    public void moveLeft(BackgroundManager bgManager) {
        int x;

        if(player == null)
            return;

        x = player.getX();

        int offset = getMapHeightOffsetPixels();
        if(offset == 0)
            offset = TILE_SIZE;


        int playerTileXLeft = pixelsToTiles(player.getX() - player.getDX());
        //int playerTileYLeft = pixelsToTiles(player.getY()) - mapHeight - 1;
        int playerTileYLeft = mapHeight - pixelsToTiles(screenHeight + offset - player.getY());

        Image tileToLeft = getTile(playerTileXLeft,playerTileYLeft);

        if(tileToLeft == null){
            player.moveLeft();
            screenLeftParallax = screenWidth/2 - player.getWidth();
            screenRightParallax = tilesToPixels(mapWidth) - screenWidth/2 - player.getWidth();
            if(x > screenLeftParallax && x < screenRightParallax && !player.isDead()){
                bgManager.moveLeft();
            }
        }
    }

    //Moves the player right; moves the background left;
    public void moveRight(BackgroundManager bgManager) {
        int x;

        if(player == null)
            return;

        x = player.getX();

        int offset = getMapHeightOffsetPixels();
        if(offset == 0)
            offset = TILE_SIZE;

        int playerTileXRight = pixelsToTiles(player.getX() + player.getWidth());
        //int playerTileYRight = pixelsToTiles(player.getY()) - mapHeight - 1;
        int playerTileYRight = mapHeight - pixelsToTiles(screenHeight + offset - player.getY());


        Image tileToRight = getTile(playerTileXRight,playerTileYRight);

        if(tileToRight == null){
            player.moveRight();

            screenLeftParallax = screenWidth/2 - player.getWidth();
            screenRightParallax = tilesToPixels(mapWidth) - screenWidth/2 - player.getWidth();

            if(x < screenRightParallax && x > screenLeftParallax && !player.isDead()) {
                bgManager.moveRight();
            }
        }

    }

    //Moves the player up; jump.
    public void moveUp(){

        if(player == null)
            return;

        int offset = getMapHeightOffsetPixels();
        if(offset == 0)
            offset = TILE_SIZE;


        int startPlayerTileX = pixelsToTiles(player.getX() + player.getDX());
        int endPlayerTileX = pixelsToTiles(player.getX());
        //int playerTileY = pixelsToTiles(player.getY()) - mapHeight - 1;
        int playerTileY = mapHeight - pixelsToTiles(screenHeight + offset - player.getY());

        Image leftTileAbove = getTile(startPlayerTileX, playerTileY);
        Image rightTileAbove = getTile(endPlayerTileX, playerTileY);

        if(leftTileAbove == null && rightTileAbove == null){
            player.moveUp();;
        }
    }

    //Enables the player to attack.
    public void playerAttack(){

        if(player == null)
            return;

        boolean successfulAttack = player.attack();

        char direction = getPlayerDirection();

        if(successfulAttack) {
            soundManager.playSound("attack", false);
            Rectangle2D.Double attackSquare = getAttackingArea(direction);
            if (mainAssassins != null){
                for (Assassin a : mainAssassins) {
                    if(a != null)
                        //If the player hits an assassin, add score and reduce the health of the enemy.
                        //Also play a sound and push the enemy back.
                        if (attackSquare.intersects(a.getBoundingRectangle()) && !a.isDead()) {
                            soundManager.playSound("hit", false);
                            scoreboard.addScore(50);
                            a.updateHealth(player.getDamage());
                            a.setX(a.getX() + -1 * a.getDirectionWeight() * a.getKnockbackDistance());
                        }
                }
            }
        }
    }


    //Determines if there is ground below the player.
    public boolean checkGroundBelow() {
        int startPlayerTileX = pixelsToTiles(player.getX() + TILE_SIZE/2);
        int endPlayerTileX = pixelsToTiles(player.getX());
        //int playerTileY = pixelsToTiles(player.getY() - player.getHeight()/2) - mapHeight ;
        int playerTileY = getHeight() - pixelsToTiles(screenHeight - player.getY());

        Image leftTileBelow = getTile(startPlayerTileX,playerTileY);
        Image rightTileBelow = getTile(endPlayerTileX,playerTileY);


        if(leftTileBelow != null || rightTileBelow != null) {
            return true;
        } else {
            return false;
        }
    }

    //Determines if an enemy is on a current tile.
    public boolean onCurrentTile(int x, int y){
        int leftTileX = pixelsToTiles(x);
        int rightTileX = pixelsToTiles(x + 50);
        //int tileY = pixelsToTiles(y - 60) - mapHeight; //To fix
        int tileY = getHeight() - pixelsToTiles(screenHeight - y) - 1;

        Image onLeft = getTile(leftTileX,tileY);
        Image onRight = getTile(rightTileX,tileY);

        if(onLeft != null || onRight != null) {
            return true;
        } else {
            return false;
        }
    }

    //Updates a player's falling state and adjusts their position if they enter a block.
    public void playerFall(boolean groundBelow){
        int playerTileY = getHeight() - pixelsToTiles(screenHeight - player.getY());
        if(groundBelow) {
            playerFalling = false;
            player.setPlayerFalling(playerFalling);
            int currentPlayerHeight = screenHeight - player.getHeight() - tilesToPixels(mapHeight - playerTileY);
            player.setY(currentPlayerHeight);
        } else {
            if(!playerFalling){
                playerFalling = true;
                player.setPlayerFalling(playerFalling);
                player.setFallTimeElapsed(0);
            }
        }
    }

    //Determines if there is a block above the player's head.
    public boolean checkGroundAbove() {
        int startPlayerTileX = pixelsToTiles(player.getX() + 2*player.getDX());
        int endPlayerTileX = pixelsToTiles(player.getX());
        //int playerTileY1 = pixelsToTiles(player.getY()) - mapHeight - 2;
        int playerTileY = mapHeight - pixelsToTiles(screenHeight + getMapHeightOffsetPixels() - player.getY()) - 1;

        Image leftTileAbove = getTile(startPlayerTileX, playerTileY);
        Image rightTileAbove = getTile(endPlayerTileX, playerTileY);

        if(leftTileAbove != null || rightTileAbove != null) {
            return true;
        }

        return false;
    }

    //Updates the enemies.
    public void updateEnemies(){
        //Random rand = new Random();
        //int behaviorChance = rand.nextInt(1500);
        if(mainAssassins != null) {
            for (Assassin a : mainAssassins) {
                if(a != null) {
                    a.update();
                    if (!a.isDead()) {
                        determineKillsPlayer(a);
                        if(a instanceof BossAssassin){
                            BossAssassin b = (BossAssassin) a;
                            b.bossUpdate();
                        }

                        //I removed behavior for evading because it made the game look buggy.
                        //if(behaviorChance < 1475)
                            a.attackPlayerBehavior(player.getBoundingRectangle(), player.getX(), player.isDead());
                        //else {
                            //a.evadePlayerBehaviour(player.getBoundingRectangle(), player.getX(), player.isDead());
                        //}


                    }
                }
            }
        }
    }

    //Determines if an assassin has killed the player and updates the player's state.
    public void determineKillsPlayer(Assassin a){
        if (a.getBoundingRectangle().intersects(player.getBoundingRectangle())) {
            if(getLives() > 1) {
                loseLife();
                soundManager.playSound("lifeLost", false);
            } else {
                scoreboard.loseLife();
                player.die();
            }
        }
    }

    //Updates the player's state.
    public void updatePlayer(){
        if (!window.isVisible ()) return;

        if(player != null) {

            boolean groundBelow = checkGroundBelow();
            playerFall(groundBelow);
            boolean groundAbove = checkGroundAbove();
            player.update(groundBelow, groundAbove);

            if (!player.isDead() && chalice != null) {
                if (chalice.getBoundingRectangle().intersects(player.getBoundingRectangle())) {
                    levelWon = true;
                }
            }
        }
    }

    public Animation getPlayerAnimation(){
        if(player != null)
            return player.getCurrentAnimation();
        return null;
    }

    public char getPlayerDirection(){
        return player.getDirection();
    }

    public boolean getLevelWon(){
        return levelWon;
    }

    public boolean isPlayerDead(){
        if(player != null)
            return player.isDead();
        return false;
    }

    public Image loadImage (String fileName) {
        return new ImageIcon(fileName).getImage();
    }

    //Used for ease of positioning enemies in the Y axis.
    public static int calcSpawnYPixels(int tileY){
        int tileOffset = mapHeight - tileY;
        int yPixels = tilesToPixels(pixelsToTiles(screenHeight) - getHeight() + tileOffset) +  getMapHeightOffsetPixels() + 4;

        return yPixels;
    }
    //Used for ease of positioning enemies in the X axis.
    public static int calcSpawnXPixels(int tileX){
        int xPixels = tilesToPixels(tileX);

        return xPixels;
    }

    //Since the screen height is not exactly divisible by 64, get the remainder of pixels for the screen height.
    public static int getMapHeightOffsetPixels(){
        return screenHeight-tilesToPixels(pixelsToTiles(screenHeight));
    }


    public int getLives(){
        return scoreboard.getLives();
    }

    public void loseLife(){
        scoreboard.loseLife();
        scoreboard.removeScore(500);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

public class Scoreboard extends JFrame {
    private JFrame window;
    private int score;
    private boolean show;
    private int lives;
    private int DEFAULT_LIVES = 3;
    private Image heartImage;
    private boolean lostLife;
    private SoundManager soundManager = SoundManager.getInstance();
    private boolean pacifist;
    private boolean flawless;
    public Image pacifistAchievementImage;
    public Image flawlessAchievementImage;
    public Image completedAchievementImage;

    private int scoreboardSizeX;
    private int scoreboardSizeY;


    //Defines the starting scoreboard attributes.
    public Scoreboard(JFrame window){
        score = 0;
        lives = DEFAULT_LIVES;
        this.window = window;
        this.show = false;
        heartImage = new ImageIcon("images/sprites/heart.png").getImage();
        lostLife = false;
        pacifist = true;
        flawless = true;

        pacifistAchievementImage = new ImageIcon("images/achievements/pacifist.png").getImage();
        flawlessAchievementImage = new ImageIcon("images/achievements/flawless.png").getImage();
        completedAchievementImage = new ImageIcon("images/achievements/completed.png").getImage();

        scoreboardSizeX = 250;
        scoreboardSizeY = 100;
    }

    //Gets the X size of the scoreboard in pixels.
    public int getScoreboardSizeX(){
        return scoreboardSizeX;
    }

    //Gets the Y size of the scoreboard in pixels.
    public int getScoreboardSizeY(){
        return scoreboardSizeY;
    }

    //Adds score to the player.
    public void addScore(int score){
        this.score += score;
        pacifist = false;
    }

    //Removes score from the player.
    public void removeScore(int score){
        flawless = false;
        this.score -= score;
        if(this.score < 0)
            this.score = 0;
    }


    //Gets the state of the achievements of a player.
    public HashMap<String, Boolean> getAchievements(){
        HashMap<String, Boolean> achievements = new HashMap<String, Boolean>();
        achievements.put("flawless", flawless);
        achievements.put("pacifist", pacifist);
        return achievements;
    }

    //Gets the score of the player.
    public int getScore(){
        return score;
    }

    //Gets the lives of the player.
    public int getLives(){
        return lives;
    }

    //Updates the lives of the player once they lose a life.
    public void loseLife(){
        lives--;
        if(lives > 0)
            this.lostLife = true;
    }

    //Resets the scoreboard.
    public void resetScore(){
        score = 0;
        lives = DEFAULT_LIVES;
        lostLife = false;
        pacifist = true;
        flawless = true;
    }

    //Determines if the scoreboard is shown.
    public void showScoreboard(boolean show){
        this.show = show;
    }

    //Draws the scoreboard and lives to the screen.
    public void draw(Graphics2D g){
        if(show == false)
            return;

        Font font = new Font("Calibri", Font.BOLD, 28);
        String score = "Score: " + getScore();

        int numLives = getLives();

        int x = window.getWidth() - scoreboardSizeX - 30;
        int y = 30;

        Rectangle2D.Double scoreboardBackground = new Rectangle2D.Double(x, y, scoreboardSizeX, scoreboardSizeY);
        g.setColor(new Color(0xA578A7C2, true));
        g.fill(scoreboardBackground);
        g.draw(scoreboardBackground);

        g.setStroke(new BasicStroke(3));
        g.setColor(new Color(0xBE262323, true));
        g.draw(scoreboardBackground);

        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString(score, (int)(10 + scoreboardBackground.getX()), (int) (30 + scoreboardBackground.getY()));

        g.drawString("Lives: ", (int)(17 + scoreboardBackground.getX()), (int) (60+ scoreboardBackground.getY()));

        for(int i = 0; i < numLives; i++)
            g.drawImage(heartImage, 100+x + i * 45, y+40, 40,40, null);
    }

    //Clears the life lost flag.
    public void clearLostLifeFlag(){
        this.lostLife = false;
    }

    //Returns whether a life has been lost.
    public boolean getLostLifeFlag(){
        return this.lostLife;
    }
}

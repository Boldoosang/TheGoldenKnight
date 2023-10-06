import java.awt.geom.Rectangle2D;

public interface Behavior {
    void attackPlayerBehavior(Rectangle2D playerBounds, int playerX, boolean isPlayerDead);
    //void evadePlayerBehaviour(Rectangle2D playerBounds, int playerX, boolean isPlayerDead);
}

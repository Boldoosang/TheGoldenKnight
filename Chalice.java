import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;

public class Chalice implements ImageFX {
    private static final int XSIZE = 50;	// width of the chalice
    private static final int YSIZE = 50;	// height of the chalice

    private int x;			// x-position of chalice
    private int y;			// y-position of chalice

    //Rotation of chalice parameters.
    private int angle;
    private int angleChange;

    //Images of the chalice.
    private Image spriteImage, firstChalice, secondChalice;

    //Initializes the chalice.
    public Chalice () {
        //Loads the image for the first chalice piece and second chalice piece.
        firstChalice = new ImageIcon("images/sprites/chalice-1.png").getImage();
        secondChalice = new ImageIcon("images/sprites/chalice-2.png").getImage();

        //Initializes to the first chalice piece.
        spriteImage = firstChalice;

        //Sets an arbitrary position to spawn the chalice away from the player.
        x = 1400;
        y = 50;

        //Sets the rotation parameters.
        angle = 0;
        angleChange = 5;
    }

    //Returns the hitbox of the chalice.
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, XSIZE, YSIZE);
    }

    //Sets the chalice piece image.
    public void setChalice(int chalice){
        if(chalice == 1){
            spriteImage = firstChalice;
        } else {
            spriteImage = secondChalice;
        }
    }

    //Returns the X position of the chalice.
    public int getX() {
        return x;
    }

    //Sets the X position of the chalice.
    public void setX(int x) {
        this.x = x;
    }

    //Returns the Y position of the chalice.
    public int getY() {
        return y;
    }

    //Sets the Y position of the chalice.
    public void setY(int y) {
        this.y = y;
    }

    //Returns the width of the chalice.
    public int getWidth(){
        return XSIZE;
    }

    //Returns the height of the chalice.
    public int getHeight(){
        return YSIZE;
    }

    //Returns the current rotated image of the chalice.
    public Image getImage() {
        int width, height;
        width = spriteImage.getWidth(null);		// find width of image
        height = spriteImage.getHeight(null);	// find height of image

        BufferedImage dest = new BufferedImage (width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dest.createGraphics();

        AffineTransform origAT = g2d.getTransform();

        AffineTransform rotation = new AffineTransform();

        rotation.rotate(Math.toRadians(angle*-1), width/2, height/2);
        g2d.transform(rotation);

        g2d.drawImage(spriteImage, 0, 0, null);	// copy in the image

        g2d.setTransform(origAT);    		// restore original transform

        g2d.drawImage(dest, x, y, XSIZE, YSIZE, null);

        g2d.dispose();
        return dest;
    }

    //Updates the chalice rotation.
    public void update() {				// modify angle of rotation

        angle = angle + angleChange;

        if (angle >= 360)			// reset to 0 degrees if 360 degrees reached
            angle = 0;

    }
}
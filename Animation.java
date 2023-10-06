import java.awt.Image;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
    The Animation class manages a series of images (frames) and
    the amount of time to display each frame.
*/

public class Animation {

    private int XSIZE;		// width of image for animation
    private int YSIZE;		// height of image for animation

    private int dx = 0;
    private int dy = 0;

    long singleAnimationTime = 0;

    private int x;
    private int y;

    private Dimension dimension;
    private boolean animationStarted;

    private JFrame window;			// JFrame on which animation is being displayed
    private ArrayList<AnimFrame> frames;	// collection of frames for animation
    private int currFrameIndex;			// current frame being displayed
    private long animTime;			// time that the animation has run for already
    private long startTime;			// start time of the animation or time since last update
    private long totalDuration;			// total duration of the animation
 
    private int active;
    private boolean playedOnce;
    private boolean looping;

    private SoundManager soundManager;		// reference to SoundManager to play clip


    /**
        Creates a new, empty Animation.
    */

    //Initializes a new animation.
    public Animation(JFrame window, boolean looping) {
        this.window = window;
        this.looping = looping;
        frames = new ArrayList<AnimFrame>();	// animation is a collection of frames        	totalDuration = 0;
        active = 0;				// keeps track of how many animations have completed
        soundManager = SoundManager.getInstance();
                            // get reference to Singleton instance of SoundManager
        animationStarted = false;
    }


    /**
        Adds an image to the animation with the specified
        duration (time to display the image).
    */

    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }


    /**
        Starts this animation over from the beginning.
    */

    //Starts the animation.
    public synchronized void start(int XSIZE, int YSIZE) {
        //Starts the animation if the animation has not started.
        if(!animationStarted) {
            this.XSIZE = XSIZE;
            this.YSIZE = YSIZE;
            animationStarted = true;
            playedOnce = false;
            active = 1;                // 1 indicates first animation sequence
            animTime = 0;                // reset time animation has run for, to zero
            currFrameIndex = 0;            // reset current frame to first fram,e
            startTime = System.currentTimeMillis();    // reset start time to current time
        }
    }


    /**
        Updates this animation's current image (frame), if
        neccesary.
    */

    public synchronized void update(int x, int y) {
        //Updates the animations.
        if (active == 0)
           return;

        long currTime = System.currentTimeMillis(); // find the current time
        long elapsedTime = currTime - startTime; // find how much time has elapsed since last update
        startTime = currTime;		         	// set start time to current time

        //If there are frames in the animation, play the animation.
        if (frames.size() > 1) {
            //Process a looping animation.
            if(looping) {
                animTime += elapsedTime;        // add elapsed time to time animation has run for

                if (animTime >= totalDuration) {
                    active += 1;
                    // if the time animation has run for > total duration
                    animTime = animTime % totalDuration;
                    //    reset time animation has run for
                    currFrameIndex = 0;        //    reset current frame to first frame
                }

                while (animTime > getFrame(currFrameIndex).endTime) {
                    currFrameIndex++;        // set frame corresponding to time animation has run for
                }
            } else {
                //Process a single play animation.
                if(playedOnce == false) {
                    singleAnimationTime += elapsedTime;
                    animTime += elapsedTime;        // add elapsed time to time animation has run for

                    if (animTime >= totalDuration) {
                        active += 1;
                        // if the time animation has run for > total duration
                        animTime = animTime % totalDuration;
                        playedOnce = true;

                    }

                    while (animTime > getFrame(currFrameIndex).endTime) {
                        currFrameIndex++;        // set frame corresponding to time animation has run for
                    }
                }
            }
        }

        dimension = window.getSize();

        this.x = x;
        this.y = y;
    }

    //Sets whether the animation has started.
    public void setAnimationStarted(boolean animationStarted){
        this.animationStarted = animationStarted;
    }

    /**
        Gets this Animation's current image. Returns null if this
        animation has no images.
    */

    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }


    public void draw (Graphics2D g2, char direction) {		// draw the current frame on the JPanel
        if (active == 0)
            return;

        if(direction == 'r')
            g2.drawImage(getImage(), x, y, XSIZE, YSIZE, null);
        else if(direction == 'l')
            g2.drawImage(getImage(), x+XSIZE, y, -XSIZE, YSIZE, null);
    }


    private AnimFrame getFrame(int i) {		// returns ith frame in the collection
        return frames.get(i);
    }


    private class AnimFrame {			// inner class for the frames of the animation
        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}

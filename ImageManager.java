import javax.swing.*;
import java.awt.*;

/**
 The ImageManager class manages the loading and processing of images.
 */

public class ImageManager {

    public ImageManager() {

    }

    //Loads an image form a path.
    public static Image loadImage (String fileName) {
        return new ImageIcon(fileName).getImage();
    }

}

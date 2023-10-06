import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.ImageIcon;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class TileMapManager {
    private ArrayList<Image> tiles;

    private JFrame window;
    private Scoreboard scoreboard;

    public TileMapManager(JFrame window, Scoreboard scoreboard) {
        this.window = window;
        this.scoreboard = scoreboard;
        loadTileImages();
    }


    /**
        Gets an image from the images/ folder.
    */
    public Image loadImage(String name) {
        String filename = "images/tiles" + name;

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("Image file could not be opened: " + filename);
        }
        else
            System.out.println("Image file opened: " + filename);

        return new ImageIcon(filename).getImage();
    }

    public TileMap loadMap(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        int mapWidth = 0;
        int mapHeight = 0;

        // read every line in the text file into the list

        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                mapWidth = Math.max(mapWidth, line.length());
            }
        }

        // parse the lines to create a TileMap
        mapHeight = lines.size();
        TileMap newMap = new TileMap(window, mapWidth, mapHeight, scoreboard);
        for (int y=0; y<mapHeight; y++) {
            String line = lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, tiles.get(tile));
                }
            }
        }

        return newMap;
    }


    public void loadTileImages() {
        File file;

        tiles = new ArrayList<Image>();
        char ch = 'A';
        while (true) {
            String filename = "images/tiles/tile_" + ch + ".png";
	    file = new File(filename);
            if (!file.exists()) {
		System.out.println("Image file could not be opened: " + filename);
                break;
            }

		Image tileImage = new ImageIcon(filename).getImage();
           	tiles.add(tileImage);
            ch++;
        }
    }
}

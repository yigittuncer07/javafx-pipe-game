import javafx.scene.image.ImageView;

public abstract class Tile {
    public boolean isMoveable;
    public boolean isFree;
    public char[] connections = new char[2];
    public ImageView imageview;

}


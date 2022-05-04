import javafx.scene.image.ImageView;

public class EmptyC extends Tile {
    EmptyC(boolean isFree) {
        this.isFree = isFree;
        isMovable = true;
        if (isFree) {
            imageview = new ImageView(PipeGame.emptyFree);
        } else {
            imageview = new ImageView(PipeGame.empty);
        }
    }
}

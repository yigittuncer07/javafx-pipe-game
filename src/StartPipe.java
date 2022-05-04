import javafx.scene.image.ImageView;

public class StartPipe extends Tile {
    StartPipe(String position) {
        if (position.equals("Vertical")) {
            connections[0] = 'd';
            imageview = new ImageView(PipeGame.starterV);
        } else {
            connections[0] = 'l';
            imageview = new ImageView(PipeGame.starterH);
        }
        isMovable = false;
        isFree = false;
    }
}

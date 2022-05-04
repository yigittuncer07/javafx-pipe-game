import javafx.scene.image.ImageView;

public class EndPipe extends Tile {
    EndPipe(String position) {
        if (position.equals("Vertical")) {
            connections[0] = 'd';
            imageview = new ImageView(PipeGame.endV);
        } else {
            connections[0] = 'l';
            imageview = new ImageView(PipeGame.endH);
        }
        isMovable = false;
        isFree = false;
    }
}

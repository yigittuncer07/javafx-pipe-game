import javafx.scene.image.ImageView;

public class CornerPipe extends Tile {
    CornerPipe(String position, boolean isMoveable) {
        if (position.equals("00")) {
            //// u,d,l,r means pipes' directions
            connections[0] = 'l';
            connections[1] = 'u';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.corner00);
            } else {
                imageview = new ImageView(PipeGame.pipeStatic00);
            }
        } else if (position.equals("01")) {
            connections[0] = 'r';
            connections[1] = 'u';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.corner01);
            } else {
                imageview = new ImageView(PipeGame.pipeStatic01);
            }
        } else if (position.equals("10")) {
            connections[0] = 'l';
            connections[1] = 'd';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.corner10);
            } else {
                imageview = new ImageView(PipeGame.pipeStatic10);
            }
        } else if (position.equals("11")) {
            connections[0] = 'r';
            connections[1] = 'd';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.corner11);
            } else {
                imageview = new ImageView(PipeGame.pipeStatic11);
            }
        } else if (position.equals("Vertical")) {
            // u,d,l,r means pipes' directions
            connections[0] = 'u';
            connections[1] = 'd';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.pipeV);
            } else {
                imageview = new ImageView(PipeGame.pipeStaticV);
            }
        } else {
            connections[0] = 'l';
            connections[1] = 'r';
            if (isMoveable) {
                imageview = new ImageView(PipeGame.pipeH);
            } else {
                imageview = new ImageView(PipeGame.pipeStaticH);
            }
        }
        this.isMovable = isMoveable;
        isFree = false;
    }
}

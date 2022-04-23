import javafx.scene.image.Image;

public class EndPipe extends Tile {
    EndPipe(String position)
    {
        if (position.equals("Vertical"))
        {
            connections[0]='d';
            Image image= new Image("images/EndV.png");
        }
        else
        {
            connections[0]='l';
            Image image = new Image("images/EndH.png");
        }
        isMoveable=false;
        isFree=false;
    }
}

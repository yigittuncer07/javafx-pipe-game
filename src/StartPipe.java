
public class StartPipe extends Tile {
    StartPipe(String position)
    {
        if (position.equals("Vertical"))
        {
            connections[0]='d';
            image="images/StarterV.png";
        }
        else
        {
            connections[0]='l';
            image="images/StarterV.png";
        }
        isMoveable=false;
        isFree=false;
    }
}

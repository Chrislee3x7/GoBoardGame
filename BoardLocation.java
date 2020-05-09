
public class BoardLocation
{
    private int x = 0;
    private int y = 0;
    private final BoardZone boardZone;
   
    public BoardLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
        boardZone = defineBoardZone();
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }
    
    public BoardZone getBoardZone()
    {
        return boardZone;
    }
    
    private BoardZone defineBoardZone()
    {
        if (x == 0 || x == 18)
        {
            if (y == 0 || y == 18)
            {
                return BoardZone.CORNER;
            }
            else
            {
                return BoardZone.EDGE;
            }
        }
        else if (y == 0 || y == 18)
        {
            return BoardZone.EDGE;
        }
        else
        {
            return BoardZone.CENTER;
        }
    }
    
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }
    
}

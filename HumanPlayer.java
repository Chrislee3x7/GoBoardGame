import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HumanPlayer implements Player
{
    
    private StoneColor color;
    
    public HumanPlayer(StoneColor playerColor)
    {
        color = playerColor;
    }
    
    public String getPrompt()
    {
        return "Player1's move.";
    }
    
    public String getWinMessage()
    {
        return "You've won!";
    }
    
    
    @Override
    public String toString()
    {
        switch(color)
        {
            case BLACK:
                return "BLACK";
            case WHITE:
                return "WHITE";
        }
        return "POOP";
    }

    @Override
    public StoneColor getColor()
    {
        return color;
    }
}

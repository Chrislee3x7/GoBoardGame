import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class HumanPlayer implements Player
{
    
    private StoneColor color;
    
    private String name;
    
    public HumanPlayer(StoneColor playerColor, String playerName)
    {
        color = playerColor;
        name = playerName;
    }
    
    public String getPrompt()
    {
        return "Player1's move.";
    }
    
    public String getWinMessage()
    {
        return "You've won!";
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
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

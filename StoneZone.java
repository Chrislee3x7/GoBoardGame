import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class StoneZone extends JComponent
{
    private GameBoard myGameBoard;
    public StoneZone(GameBoard gameBoard)
    {
        this.myGameBoard = gameBoard;
    }
    
//    public void repaintLocation( BoardLocation location )
//    {
//        // Stone deletedStone = new Stone(StoneColor.RED, location, this);
//        // deletedStone.display();
//        repaint( new Rectangle( location.getX() * 40 + 20,
//            location.getY() * 40 + 20,
//            40,
//            40 ));
//    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println("Called paint method on the glass pane");
        for (int i = 0; i < myGameBoard.getWidth(); i++)
        {
            for (int j = 0; j < myGameBoard.getHeight(); j++)
            {
                Stone stone = myGameBoard.getStone( i, j );
                if (stone != null)
                {
                    //System.out.println("im a stone");
                    stone.display((Graphics2D) g);
                    //getGraphics().fillRect( 40, 40, 40, 40 );
                }
            }
        }
    }
}

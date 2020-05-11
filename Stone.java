import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;


public class Stone
{
    private StoneColor color;

    private BoardLocation location;
    
    private StoneZone stoneZone;

    private static final int INITIAL_OFFSET_CENTER = DrawBoard.BOARD_EDGE_OFFSET
        + DrawBoard.BOARD_EDGELINES_OFFSET;

    private static final int LINE_GAP_LENGTH = 40;

    private static final int PIECE_DIAMETER = 38;


    public Stone( StoneColor color, BoardLocation location, StoneZone stoneZone )
    {
        this.color = color;
        this.location = location;
        this.stoneZone = stoneZone;
    }


    public StoneColor getColor()
    {
        return color;
    }
    
    public BoardLocation getLocation()
    {
        return location;
    }
    
    //public HashSet<BoardLocation> findLiberties()
    {
        //first get all stone
        
    }
    
    public void display(Graphics2D g)
    {
        //System.out.println(this + " displayed this stone");
        int x = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getX()
            - PIECE_DIAMETER / 2;
        int y = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getY()
            - PIECE_DIAMETER / 2 + GameBoard.BORDER_WIDTH;
        int centerX = INITIAL_OFFSET_CENTER
            + LINE_GAP_LENGTH * location.getX();
        int centerY = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getY()
            + GameBoard.BORDER_WIDTH;
        g.fillOval( x, y, PIECE_DIAMETER, PIECE_DIAMETER );
        Color[] colors = new Color[2];
        float[] dist = { 0.2f, 0.8f };
        switch ( color )
        {
            case BLACK:
                colors[0] = Color.GRAY;
                colors[1] = Color.BLACK;
                break;
            case WHITE:
                colors[0] = Color.WHITE;
                colors[1] = Color.LIGHT_GRAY;
                break;
            default:
                colors[0] = Color.PINK;
                colors[1] = Color.RED;
        }
        RadialGradientPaint gp = new RadialGradientPaint( centerX,
            centerY,
            PIECE_DIAMETER / 2,
            dist,
            colors );
        g.setPaint( gp );
        g.fillOval( x, y, PIECE_DIAMETER, PIECE_DIAMETER );
        
        
    }
}

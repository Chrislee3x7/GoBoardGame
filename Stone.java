import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.util.HashSet;
import java.util.Set;


public class Stone
{
    private StoneColor color;

    private BoardLocation location;
    
    private Board board;

    private static final int INITIAL_OFFSET_CENTER = Board.BOARD_EDGE_OFFSET
        + Board.BOARD_EDGELINES_OFFSET;

    private static final int LINE_GAP_LENGTH = 40;

    private static final int PIECE_DIAMETER = 38;


    public Stone( StoneColor color, BoardLocation location, Board board )
    {
        this.color = color;
        this.location = location;
        this.board = board;
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
    
    public void display()
    {
        Graphics2D g = (Graphics2D) board.getGraphics();
        int x = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getX()
            - PIECE_DIAMETER / 2;
        int y = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getY()
            - PIECE_DIAMETER / 2 + Board.BORDER_WIDTH;
        int centerX = INITIAL_OFFSET_CENTER
            + LINE_GAP_LENGTH * location.getX();
        int centerY = INITIAL_OFFSET_CENTER + LINE_GAP_LENGTH * location.getY()
            + Board.BORDER_WIDTH;
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

public class DrawBoard extends JPanel
{

    private static final int WINDOW_LENGTH = 800;

    public static final int BORDER_WIDTH = 0;

    private static final int LINE_GAP_DISTANCE = 40;

    public static final int BOARD_EDGE_OFFSET = 10;

    public static final int BOARD_EDGELINES_OFFSET = 30;

    private static final int BOARD_DIMMENSION = 780;

    private static final int REFERENCE_DOT_DIAMETER = 10;

    private static final int FIRST_DOT_OFFSET = LINE_GAP_DISTANCE * 4
        - REFERENCE_DOT_DIAMETER / 2;

    private static final Color BACKGROUND_COLOR = new Color( 0, 100, 0 );

    private static final Color BOARD_COLOR = new Color( 255, 210, 120 );

    private static final Color BOARD_LINES_COLOR = new Color( 0, 0, 0 );
    
    private Graphics2D g2D;
    
    private GameBoard myGameBoard;
    
    public DrawBoard( GameBoard gameBoard )
    {
        myGameBoard = gameBoard;
        setPreferredSize(new Dimension( 800, 800 ));
        
        setBackground( BACKGROUND_COLOR );
        g2D = (Graphics2D) getGraphics();
    }
    
    public Graphics2D getGraphics2D()
    {
        return (Graphics2D)getGraphics();
    }
    
    public void paintComponent(Graphics g)
    {
        //System.out.println("DrawBoard repaint method called");
        super.paintComponent( g );
        Graphics2D g2D = (Graphics2D)g;
        // g2D.fillRect( 40, 40, 40, 40 );
        g2D.setColor( BOARD_COLOR );
        g2D.fillRect( BOARD_EDGE_OFFSET,
            BORDER_WIDTH + BOARD_EDGE_OFFSET,
            BOARD_DIMMENSION,
            BOARD_DIMMENSION );
        g2D.setColor( BOARD_LINES_COLOR );
        g2D.drawRect( BOARD_EDGE_OFFSET,
            BORDER_WIDTH + BOARD_EDGE_OFFSET,
            BOARD_DIMMENSION,
            BOARD_DIMMENSION );
        g2D.drawRect( 40, 40 + BORDER_WIDTH, 720, 720 );
        for ( int i = 1; i < 19; i++ )
        {
            g2D.drawLine(
                BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET
                    + i * LINE_GAP_DISTANCE,
                BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET + BORDER_WIDTH,
                BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET
                    + i * LINE_GAP_DISTANCE,
                WINDOW_LENGTH - BOARD_EDGELINES_OFFSET - BOARD_EDGE_OFFSET
                    + BORDER_WIDTH );
            g2D.drawLine( BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET,
                BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET
                    + i * LINE_GAP_DISTANCE + BORDER_WIDTH,
                WINDOW_LENGTH - BOARD_EDGELINES_OFFSET - BOARD_EDGE_OFFSET,
                BOARD_EDGELINES_OFFSET + BOARD_EDGE_OFFSET
                    + i * LINE_GAP_DISTANCE + BORDER_WIDTH );
        }
        for ( int i = 0; i < 3; i++ )
        {
            for ( int j = 0; j < 3; j++ )
            {
                g2D.fillOval( FIRST_DOT_OFFSET + LINE_GAP_DISTANCE * 6 * i,
                    FIRST_DOT_OFFSET + LINE_GAP_DISTANCE * 6 * j
                        + +BORDER_WIDTH,
                    REFERENCE_DOT_DIAMETER,
                    REFERENCE_DOT_DIAMETER );
            }
        }
    }
}

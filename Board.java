import javax.swing.*;
import java.awt.*;
import java.util.HashSet;


public class Board extends JPanel
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

    private int width;

    private int height;

    private Stone[][] stoneMatrix;

    // private Graphics2D graphics2D;

    GameManager gm;


    public Board( GameManager gm )
    {
        this( gm, 19, 19 );
    }


    public Board( GameManager gm, int boardWidth, int boardHeight )
    {
        this.gm = gm;
        this.width = boardWidth;
        this.height = boardHeight;
        stoneMatrix = new Stone[width][height];
        // paint( getGraphics() );
        // getGraphics().fillRect( 40, 40, 40, 40 );
        setPreferredSize(new Dimension(800, 800));
        setBackground(BACKGROUND_COLOR);
    }


    public static BoardLocation translateToLocation( int x, int y )
    {
        BoardLocation bl = new BoardLocation( ( x - 20 ) / 40,
            ( y - 20 ) / 40 );
        System.out.println( bl );
        return bl;
    }


    public boolean addStone( StoneColor color, BoardLocation location )
    {
        boolean isEmptyLocation = isEmptyLocation( location );
        if ( !isEmptyLocation )
        {
            return false;
        }
        Stone testStone = new Stone( color, location, this );
        stoneMatrix[location.getX()][location.getY()] = testStone;

        if ( hasLiberties( color,
            location )/* GoRules.isValidPlacement( board, location, color ) */ )
        {
            removeZeroLibertyStones( testStone );
            testStone.display();
            return true;
        }
        else
        {
            // removes placed stone for liberty test
            removeStone( location );
            return false;
        }

    }


    public void removeStone( BoardLocation location )
    {
        stoneMatrix[location.getX()][location.getY()] = null;
        repaintLocation( location );
    }


    public void repaintLocation( BoardLocation location )
    {
        // Stone deletedStone = new Stone(StoneColor.RED, location, this);
        // deletedStone.display();
        repaint( 5000, location.getX() * 40 + 23,
            location.getY() * 40 + 23,
            34,
            34 );
        this.update( getGraphics() );
    }


    public boolean hasLiberties( StoneColor color, BoardLocation location )
    {
        Stone stone = new Stone( color, location, this );
        HashSet<Stone> visitedStones = new HashSet<Stone>();
        return hasLiberties( stone, visitedStones );

    }


    private boolean hasLiberties( Stone stone, HashSet<Stone> visitedStones )
    {
        if ( visitedStones.contains( stone ) )
        {
            return false;
        }
        visitedStones.add( stone );
        return northHasLiberties( stone, visitedStones )
            || southHasLiberties( stone, visitedStones )
            || eastHasLiberties( stone, visitedStones )
            || westHasLiberties( stone, visitedStones );

    }


    private void removeZeroLibertyStones( Stone stone )
    {
        // get adjacent stones north south east west
        // check if each adjacent enemy stone has at least one liberty
        // if has no liberties, whole chain needs to be removed
        if ( stone.getLocation().getX() > 0 )
        {
            Stone adjacentStone = getWestStone( stone.getLocation() );
            if ( adjacentStone != null
                && adjacentStone.getColor() != stone.getColor() )
            {
                dealWithVisitedStones( adjacentStone ); // either
                                                        // delete
                                                        // or not
                                                        // delete
            }
        }

        if ( stone.getLocation().getX() < width - 1 )
        {
            Stone adjacentStone = getEastStone( stone.getLocation() );
            if ( adjacentStone != null
                && adjacentStone.getColor() != stone.getColor() )
            {
                dealWithVisitedStones( adjacentStone ); // either
                                                        // delete
                                                        // or not
                                                        // delete
            }
        }

        if ( stone.getLocation().getY() > 0 )
        {
            Stone adjacentStone = getNorthStone( stone.getLocation() );
            if ( adjacentStone != null
                && adjacentStone.getColor() != stone.getColor() )
            {
                dealWithVisitedStones( adjacentStone ); // either
                                                        // delete
                                                        // or not
                                                        // delete
            }
        }

        if ( stone.getLocation().getY() < width - 1 )
        {
            Stone adjacentStone = getSouthStone( stone.getLocation() );
            if ( adjacentStone != null
                && adjacentStone.getColor() != stone.getColor() )
            {
                dealWithVisitedStones( adjacentStone ); // either
                                                        // delete
                                                        // or not
                                                        // delete
            }
        }
    }


    private void dealWithVisitedStones( Stone adjacentStone )
    {
        HashSet<Stone> visitedStones = new HashSet<Stone>();
        if ( !hasLiberties( adjacentStone, visitedStones ) )
        {
            for ( Stone visitedStone : visitedStones )
            {  
                removeStone( visitedStone.getLocation() );
            }
        }
    }


    public boolean isEmptyLocation( BoardLocation location )
    {
        if ( !isInRange( location ) )
        {
            return false;
        }
        return stoneMatrix[location.getX()][location.getY()] == null;
    }


    public boolean isInRange( BoardLocation location )
    {
        return location.getX() < getWidth() && location.getX() >= 0
            && location.getY() < getHeight() && location.getY() >= 0;
    }


    public boolean northHasLiberties(
        Stone stone,
        HashSet<Stone> visitedStones )
    {
        if ( stone.getLocation().getY() <= 0 )
        {
            return false;
        }
        Stone adjacentStone = getNorthStone( stone.getLocation() );
        return adjacentHasLiberties( stone, adjacentStone, visitedStones );
    }


    public boolean southHasLiberties(
        Stone stone,
        HashSet<Stone> visitedStones )
    {
        if ( stone.getLocation().getY() >= height - 1 )
        {
            return false;
        }
        Stone adjacentStone = getSouthStone( stone.getLocation() );
        return adjacentHasLiberties( stone, adjacentStone, visitedStones );
    }


    public boolean eastHasLiberties(
        Stone stone,
        HashSet<Stone> visitedStones )
    {
        if ( stone.getLocation().getX() >= width - 1 )
        {
            return false;
        }
        Stone adjacentStone = getEastStone( stone.getLocation() );
        return adjacentHasLiberties( stone, adjacentStone, visitedStones );
    }


    public boolean westHasLiberties(
        Stone stone,
        HashSet<Stone> visitedStones )
    {
        if ( stone.getLocation().getX() <= 0 )
        {
            return false;
        }
        Stone adjacentStone = getWestStone( stone.getLocation() );
        return adjacentHasLiberties( stone, adjacentStone, visitedStones );
    }


    public boolean adjacentHasLiberties(
        Stone stone,
        Stone adjacentStone,
        HashSet<Stone> visitedStones )
    {
        // check if stone is null: return true
        if ( adjacentStone == null )
        {
            // adjacentStone = new Stone( StoneColor.RED,
            // new BoardLocation( stone.getLocation().getX() - 1,
            // stone.getLocation().getY() ),
            // this );
            // adjacentStone.display();
            return true;
        }
        // check if stone is enemy stone: return false
        if ( adjacentStone.getColor() != stone.getColor() )
        {
            return false;
        }
        // check if stone is allied stone: call hasLiberties on stone
        return hasLiberties( adjacentStone, visitedStones );
    }


    public Stone getStone( BoardLocation loc )
    {
        return getStone( loc.getX(), loc.getY() );
    }


    public Stone getStone( int x, int y )
    {
        if ( x < 0 || x >= width || y < 0 || y >= height )
        {
            return null;
        }
        return stoneMatrix[x][y];
    }


    public Stone getNorthStone( BoardLocation location )
    {
        return getStone( location.getX(), location.getY() - 1 );
    }


    public Stone getSouthStone( BoardLocation location )
    {
        return getStone( location.getX(), location.getY() + 1 );
    }


    public Stone getEastStone( BoardLocation location )
    {
        return getStone( location.getX() + 1, location.getY() );
    }


    public Stone getWestStone( BoardLocation location )
    {
        return getStone( location.getX() - 1, location.getY() );
    }


    public Graphics2D getGraphics2D()
    {
        return (Graphics2D)getGraphics();
    }


    public int getWidth()
    {
        return width;
    }


    public int getHeight()
    {
        return height;
    }


    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        
        System.out
            .println( "I am repainting if this is not the first line :P" );
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

        // BoardLocation test = new BoardLocation( 0, 0 );
        // BoardLocation test2 = new BoardLocation( 0, 1 );
        // gm.playPiece( BoardPieceColor.BLACK, test);
        // gm.playPiece( BoardPieceColor.WHITE, test2);
        // BoardLocation l = new BoardLocation(4, 4);
        // BoardPiece bp = new BoardPiece( BoardPieceColor.BLACK, l, this );
        // bp.display(getGraphics());
    }
}

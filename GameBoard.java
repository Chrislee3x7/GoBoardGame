import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;


public class GameBoard
{
    private static final int WINDOW_LENGTH = 800;

    public static final int BORDER_WIDTH = 0;

    private static final int LINE_GAP_DISTANCE = 40;

    public static final int BOARD_EDGE_OFFSET = 10;

    public static final int BOARD_EDGELINES_OFFSET = 30;

    private static final Color BACKGROUND_COLOR = new Color( 0, 100, 0 );

    private static final Color BOARD_COLOR = new Color( 255, 210, 120 );

    private static final Color BOARD_LINES_COLOR = new Color( 0, 0, 0 );

    private static final int WIDTH = 19;

    private static final int HEIGHT = 19;

    private Stone[][] stoneMatrix;

    // private Graphics2D graphics2D;

    private GameManager gm;

    private DrawBoard drawBoard;

    private StoneZone stoneZone;


    public GameBoard( GameManager gm )
    {
        this.gm = gm;
        stoneZone = new StoneZone( this );
        drawBoard = new DrawBoard( this );
        stoneMatrix = new Stone[WIDTH][HEIGHT];
    }


    public static BoardLocation translateToLocation( int x, int y )
    {
        BoardLocation bl = new BoardLocation( ( x - 20 ) / 40,
            ( y - 20 ) / 40 );
        //System.out.println( bl );
        return bl;
    }


    public DrawBoard getDrawBoard()
    {
        return drawBoard;
    }


    public boolean addStone( StoneColor color, BoardLocation location )
    {
        boolean isEmptyLocation = isEmptyLocation( location );
        if ( !isEmptyLocation )
        {
            return false;
        }
        Stone testStone = new Stone( color, location, stoneZone );
        stoneMatrix[location.getX()][location.getY()] = testStone;
        

        if ( removeZeroLibertyStones( testStone ) || hasLiberties( color,
            location )/* GoRules.isValidPlacement( board, location, color ) */ )
        {
            testStone.display((Graphics2D)stoneZone.getGraphics());
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
        Stone stone = stoneMatrix[location.getX()][location.getY()];
        stoneMatrix[location.getX()][location.getY()] = null;
        //System.out.println( "trying to repaint to remove stone" );
        // stoneZone.repaint();
        // stoneZone.paintComponent( stoneZone.getGraphics() );
        // drawBoard.repaintLocation( location );
        // drawBoard.update();
    }


    public boolean hasLiberties( StoneColor color, BoardLocation location )
    {
        Stone stone = new Stone( color, location, stoneZone );
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


    //return if any stones are removed
    private boolean removeZeroLibertyStones( Stone stone )
    {
        // get adjacent stones north south east west
        // check if each adjacent enemy stone has at least one liberty
        // if has no liberties, whole chain needs to be removed
        HashSet<Stone> stonesToBeRemoved = new HashSet<Stone>();
        if ( stone.getLocation().getX() > 0 )
        {
            Stone adjacentStone = getWestStone( stone.getLocation() );

            // either delete or not delete
            stonesToBeRemoved
                .addAll( findRemovableStones( adjacentStone, stone ) );
        }

        if ( stone.getLocation().getX() < WIDTH - 1 )
        {
            Stone adjacentStone = getEastStone( stone.getLocation() );
            // either delete or not delete
            stonesToBeRemoved
                .addAll( findRemovableStones( adjacentStone, stone ) );
        }

        if ( stone.getLocation().getY() > 0 )
        {
            Stone adjacentStone = getNorthStone( stone.getLocation() );
            // either delete or not delete
            stonesToBeRemoved
                .addAll( findRemovableStones( adjacentStone, stone ) );
        }

        if ( stone.getLocation().getY() < WIDTH - 1 )
        {
            Stone adjacentStone = getSouthStone( stone.getLocation() );
            // either delete or not delete
            stonesToBeRemoved
                .addAll( findRemovableStones( adjacentStone, stone ) );
        }
        for ( Stone stoneToBeRemoved : stonesToBeRemoved )
        {
            removeStone( stoneToBeRemoved.getLocation() );
        }
        if ( stonesToBeRemoved.size() > 0 )
        {
            stoneZone.repaint();
        }
        return stonesToBeRemoved.size() > 0;
    }


    private HashSet<Stone> findRemovableStones(
        Stone adjacentStone,
        Stone stone )
    {
        if ( adjacentStone != null
            && adjacentStone.getColor() != stone.getColor() )
        {
            HashSet<Stone> visitedStones = new HashSet<Stone>();
            if ( !hasLiberties( adjacentStone, visitedStones ) )
            {
                return visitedStones;
            }
        }
        return new HashSet<Stone>();
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
        if ( stone.getLocation().getY() >= HEIGHT - 1 )
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
        if ( stone.getLocation().getX() >= WIDTH - 1 )
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
        if ( x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT )
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


    public int getWidth()
    {
        return WIDTH;
    }


    public int getHeight()
    {
        return HEIGHT;
    }


    public StoneZone getStoneZone()
    {
        return stoneZone;
    }
}

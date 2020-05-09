import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


public class GoRules
{
    // Must be on the board, not on a placed piece,
    // not in an eye unless can eat(goes with rule in next line)
    // A move cannot create the immediate former board pattern
    // self-capture is not allowed (no play that would make a player lose their
    // own stones
    // capture of the enemy takes precedence over self-capture
    public static boolean isValidPlacement(
        Board board,
        BoardLocation location,
        StoneColor color )
    {
        BoardLocation bl = new BoardLocation( location.getX(),
            location.getY() );
        BoardZone bz = bl.getBoardZone();
        if ( !isInRange( board, location ) || isOccupied( board, location ) )
        {
            return false;
        }
        StoneColor otherColor;
        switch ( color )
        {
            case BLACK:
                otherColor = StoneColor.WHITE;
                break;
            default:
                otherColor = StoneColor.BLACK;
        }
        int x = location.getX();
        int y = location.getY();
        boolean isValidNorthStone = false;
        boolean isValidSouthStone = false;
        boolean isValidEastStone = false;
        boolean isValidWestStone = false;
        if ( x > 0 )
        {
            isValidWestStone = isValidWestStone( board, location, otherColor );
        }
        if ( x < board.getWidth() - 1 )
        {
            isValidEastStone = isValidEastStone( board, location, otherColor );
        }
        if ( y > 0 )
        {
            isValidNorthStone = isValidNorthStone( board,
                location,
                otherColor );
        }
        if ( y < board.getHeight() - 1 )
        {
            isValidSouthStone = isValidSouthStone( board,
                location,
                otherColor );
        }

        return isValidNorthStone || isValidSouthStone || isValidEastStone
            || isValidWestStone;
    }


    // These should replace the bottom 3 eventually possibly in the board class

    public static HashSet<Stone> getAdjacentStones(
        Stone stone,
        Stone[][] board )
    {
        BoardZone bz = stone.getLocation().getBoardZone();
        int x = stone.getLocation().getX();
        int y = stone.getLocation().getY();
        HashSet<Stone> adjacentStones = new HashSet<Stone>();
        switch ( bz )
        {
            case CENTER:
                adjacentStones.add( board[x + 1][y] );
                adjacentStones.add( board[x][y + 1] );
                adjacentStones.add( board[x - 1][y] );
                adjacentStones.add( board[x][y - 1] );
                break;
            case EDGE:
                if ( x == 0 )
                {
                    adjacentStones.add( board[x + 1][y] );
                    adjacentStones.add( board[x][y + 1] );
                    adjacentStones.add( board[x][y - 1] );
                }
                else if ( x == board.length - 1 )
                {
                    adjacentStones.add( board[x - 1][y] );
                    adjacentStones.add( board[x][y + 1] );
                    adjacentStones.add( board[x][y - 1] );
                }
                else if ( y == 0 )
                {
                    adjacentStones.add( board[x - 1][y] );
                    adjacentStones.add( board[x + 1][y] );
                    adjacentStones.add( board[x][y - 1] );
                }
                // aka: y == board[0].length() - 1
                else
                {
                    adjacentStones.add( board[x - 1][y] );
                    adjacentStones.add( board[x + 1][y] );
                    adjacentStones.add( board[x][y + 1] );
                }
                break;
            case CORNER:
                if ( x == 0 )
                {
                    if ( y == 0 )
                    {
                        adjacentStones.add( board[x + 1][y] );
                        adjacentStones.add( board[x][y + 1] );
                    }
                    else
                    {
                        adjacentStones.add( board[x + 1][y] );
                        adjacentStones.add( board[x][y - 1] );
                    }
                }
                // aka x == board.getWidth() - 1
                else
                {
                    if ( y == 0 )
                    {
                        adjacentStones.add( board[x - 1][y] );
                        adjacentStones.add( board[x][y + 1] );
                    }
                    else
                    {
                        adjacentStones.add( board[x - 1][y] );
                        adjacentStones.add( board[x][y - 1] );
                    }
                }
                break;
        }
        return adjacentStones;
    }


    private static boolean isValidNorthStone(
        Board board,
        BoardLocation location,
        StoneColor otherColor )
    {
        Stone northStone = board.getNorthStone( location );
        return northStone == null || northStone.getColor() != otherColor;
    }


    private static boolean isValidSouthStone(
        Board board,
        BoardLocation location,
        StoneColor otherColor )
    {
        Stone southStone = board.getSouthStone( location );
        return southStone == null || southStone.getColor() != otherColor;
    }


    private static boolean isValidEastStone(
        Board board,
        BoardLocation location,
        StoneColor otherColor )
    {
        Stone eastStone = board.getEastStone( location );
        return eastStone == null || eastStone.getColor() != otherColor;
    }


    private static boolean isValidWestStone(
        Board board,
        BoardLocation location,
        StoneColor otherColor )
    {
        Stone westStone = board.getWestStone( location );
        return westStone == null || westStone.getColor() != otherColor;
    }
    

    private static boolean isInRange( Board board, BoardLocation location )
    {
        return location.getX() < board.getWidth() && location.getX() >= 0
            && location.getY() < board.getHeight() && location.getY() >= 0;
    }


    private static boolean isOccupied( Board board, BoardLocation location )
    {
        return board.getStone( location ) != null;
    }
}

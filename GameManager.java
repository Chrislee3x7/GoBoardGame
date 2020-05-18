import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComponent;


public class GameManager implements MouseListener
{

    // private JFrame board;

    private Player player1;

    private Player player2;

    private Player currentPlayer;

    private GameBoard gameBoard;

    private JFrame window;

    private JPanel drawBoard;

    private CommandPanel commandPanel;

    private File savedGame;

    private boolean atHome = true;
    
    private boolean isSaved = true;


    public static void main( String[] args ) throws FileNotFoundException
    {
        GameManager gm = new GameManager();
    }


    public GameManager()
    {
        savedGame = new File( "SavedGame" );

        window = new JFrame( "Go" );

        gameBoard = new GameBoard( this );

        commandPanel = new CommandPanel( this );

        drawBoard = ( gameBoard ).getDrawBoard();
        window.add( drawBoard, BorderLayout.WEST );
        window.add( commandPanel, BorderLayout.EAST );

        JComponent stoneZone = gameBoard.getStoneZone();
        window.setGlassPane( stoneZone );
        // drawBoard.setVisible( false );
        stoneZone.setVisible( true );
        drawBoard.addMouseListener( this );
        // window.setBounds( 200, 30, 810, 835 );

        window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        window.setResizable( false );
        window.setVisible( true );
        window.pack();

        player1 = new HumanPlayer( StoneColor.BLACK );
        player2 = new HumanPlayer( StoneColor.WHITE );
        currentPlayer = player1;
    }


    private void switchPlayer()
    {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }


    public boolean canUndo()
    {
        return gameBoard.getPreviousBoardState() != null;
    }


    public boolean canRedo()
    {
        return gameBoard.getUndoneBoardState() != null;
    }


    public boolean isAtHome()
    {
        return atHome;
    }


    public void setAtHome( boolean atHome )
    {
        this.atHome = atHome;
    }


    public void startNewGame()
    {
        currentPlayer = player1;
        setAtHome( false );
        gameBoard.setBoardState( new Stone[19][19] );
        gameBoard.getStoneZone().repaint();
        commandPanel.updateButtons();
    }


    public void loadGame()
    {

        setAtHome( false );
        gameBoard.getStoneZone().repaint();
        Scanner sc = null;
        try
        {
            sc = new Scanner( savedGame );
        }
        catch ( FileNotFoundException e1 )
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        gameBoard.setBoardState( new Stone[19][19] );
        for ( int i = 0; i < 19; i++ )
        {
            for ( int j = 0; j < 19; j++ )
            {
                int piece = sc.nextInt();
                switch ( piece )
                {
                    case 0:
                        break;
                    case 1:
                        gameBoard.addStone( StoneColor.BLACK,
                            new BoardLocation( j, i ) );
                        break;
                    case 2:
                        gameBoard.addStone( StoneColor.WHITE,
                            new BoardLocation( j, i ) );
                        break;
                    default:
                        System.out
                            .println( "incorrect color at: " + j + " " + i );
                }
            }
        }
        String nextPlayerColor = sc.next();
        //System.out.println(nextPlayerColor.toString() + "<-- saved next player color");
        if (!currentPlayer.getColor().toString().equals( nextPlayerColor))
        {
            switchPlayer();
        }
        isSaved = true;
        commandPanel.updateButtons();
    }


    public void saveGame()
    {
        PrintWriter pw = null;
        try
        {
            pw = new PrintWriter( savedGame );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for ( int i = 0; i < 19; i++ )
        {
            for ( int j = 0; j < 19; j++ )
            {
                Stone stone = gameBoard.getStone( j, i );
                if ( stone == null )
                {
                    pw.print( "0 " );
                    continue;
                }
                StoneColor stoneColor = stone.getColor();
                switch ( stoneColor )
                {
                    case BLACK:
                        pw.print( "1 " );
                        break;
                    case WHITE:
                        pw.print( "2 " );
                }
            }
            pw.println();
        }
        pw.print( currentPlayer.getColor() );
        pw.close();
        isSaved = true;
        commandPanel.updateButtons();
    }


    public void goHome()
    {
        // warn user if not saved game progress
        // if (false)//user does not want to actually leave the game)
        // {
        // //return;
        // }
        gameBoard.setBoardState( null );
        gameBoard.setPreviousBoardState( null );
        gameBoard.setUndoneBoardState( null );
        setAtHome( true );
        gameBoard.getStoneZone().repaint();
        isSaved = true;
        commandPanel.updateButtons();
    }


    public boolean canLoad()
    {
        // TODO checks if there is anything in the loaded file
        // returns false if there isn't must also be at home
        Scanner sc = null;
        try
        {
            sc = new Scanner( savedGame );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean hasSavedGame = sc.hasNext();
        return atHome && hasSavedGame;
    }


    public boolean isSaved()
    {
        return isSaved;
    }
    
    public boolean canSave()
    {
        Stone[][] board = gameBoard.getCurrentBoardState();
        if (atHome)
        {
            return false;
        }
        for ( int i = 0; i < 19; i++ )
        {
            for (int j = 0 ; j < 19; j++)
            {
                Stone s = board[i][j];
                if (s != null)
                {
                    return true;
                }
            }
        }
        return false;
    }


    public void undo()
    {
        // System.out.println("Called undo");
        // gameBoard.printStones();
        Stone[][] previousBoardState = gameBoard.getPreviousBoardState();
        if ( !canUndo() )
        {
            return;
        }
        gameBoard.setUndoneBoardState( gameBoard.getCurrentBoardState() );
        gameBoard.setBoardState( previousBoardState );
        gameBoard.setPreviousBoardState( null );
        switchPlayer();
        commandPanel.updateButtons();
        drawBoard.repaint();
        gameBoard.getStoneZone().repaint();
        isSaved = false;
        commandPanel.updateButtons();
    }


    public void redo()
    {
        // System.out.println("Called redo");
        Stone[][] undoneBoardState = gameBoard.getUndoneBoardState();
        if ( !canRedo() )
        {
            return;
        }
        gameBoard.setPreviousBoardState( gameBoard.getCurrentBoardState() );
        gameBoard.setBoardState( undoneBoardState );
        gameBoard.setUndoneBoardState( null );
        switchPlayer();
        drawBoard.repaint();
        gameBoard.getStoneZone().repaint();
        isSaved = false;
        commandPanel.updateButtons();
    }


    public boolean playPiece( StoneColor color, BoardLocation location )
    {
        // returns if it was played or not (valid or invalid)
        if ( atHome )
        {
            return false;
        }
        boolean wasValidPlay = ( gameBoard ).addStone( color, location );
        if ( !wasValidPlay )
        {
            // player receives a message "pls choose nother place",
            // stone is not played, still same players turn
        }
        else
        {
            //System.out.println("saying it is not saved");
            isSaved = false;
            commandPanel.updateButtons();
        }
        return wasValidPlay;
    }


    public GameBoard getBoard()
    {
        return gameBoard;
    }


    public void mouseClicked( MouseEvent e )
    {
        // System.out.println("mouse clicked");
        BoardLocation loc = GameBoard.translateToLocation( e.getX(),
            e.getY() );
        if ( playPiece( currentPlayer.getColor(), loc ) )
        {
            switchPlayer();
            //System.out.println(currentPlayer.getColor().toString());
        }
        else
        {
            // TODO
            // System.out.println("Please choose another location.");
        }
    }

    public JFrame getWindow()
    {
        return window;
    }
    
    public void mouseReleased( MouseEvent e )
    {
    }


    public void mousePressed( MouseEvent e )
    {
    }


    public void mouseEntered( MouseEvent e )
    {
    }


    public void mouseExited( MouseEvent e )
    {
    }
}

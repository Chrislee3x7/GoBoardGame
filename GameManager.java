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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComponent;


public class GameManager implements MouseListener
{

    // private JFrame board;

    private Player player1;

    private Player player2;

    private String currentP1Name;

    private String currentP2Name;

    private Player currentPlayer;

    private GameBoard gameBoard;

    private JFrame window;

    private JPanel drawBoard;

    private JLabel quickInfo;

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
        quickInfo = new JLabel("Home Screen. " + "will display what image here");
        quickInfo.setPreferredSize( new Dimension(500, 30) );
        window.add( quickInfo, BorderLayout.SOUTH );

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

        player1 = new HumanPlayer( StoneColor.BLACK, "Player1" );
        player2 = new HumanPlayer( StoneColor.WHITE, "Player2" );
        currentPlayer = player1;
    }


    private void switchPlayer()
    {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
        quickInfo.setText( currentPlayer.getColor() + ": "
            + currentPlayer.getName() + "'s turn." );
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
        Object[] options = { "OK", "Cancel" };
        DoubleInputPanel playerNameFields = new DoubleInputPanel(
            "Player 1 (BLACK)",
            "Player 2 (WHITE)" );
        int n = JOptionPane.showOptionDialog( getWindow(),
            playerNameFields,
            "Please enter player names. (only letters and digits, no spaces)",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0] );
        if ( n == 1 )
        {
            goHome();
        }
        else if ( n == 0 )
        {
            String player1Name = playerNameFields.getXField();
            String player2Name = playerNameFields.getYField();

            if ( isActualPlayerName( player1Name ) )
            {
                player1.setName( player1Name );
            }
            else
            {
                player1.setName( "Player1" );
            }

            if ( isActualPlayerName( player2Name ) )
            {
                player2.setName( player2Name );
            }
            else
            {
                player2.setName( "Player2" );
            }
            player2.setName( player2Name );
            currentPlayer = player1;
            setAtHome( false );
            gameBoard.setBoardState( new Stone[19][19] );
            gameBoard.getStoneZone().repaint();
            commandPanel.updateButtons();
            JOptionPane.showMessageDialog( getWindow(),
                currentPlayer.getColor() + ": " + currentPlayer.getName()
                    + "'s turn.",
                "New Game",
                JOptionPane.INFORMATION_MESSAGE );
            quickInfo.setText( "New game started. " + currentPlayer.getColor() + ": "
                            + currentPlayer.getName() + "'s turn." );
        }
    }


    public boolean isActualPlayerName( String playerName )
    {
        if ( playerName.isEmpty() || playerName.contains( " " ) )
        {
            return false;
        }

        for ( int i = 0; i < playerName.length(); i++ )
        {
            if ( !Character.isLetterOrDigit( playerName.charAt( i ) ) )
            {
                return false;
            }
        }
        return true;
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
        // System.out.println(nextPlayerColor.toString() + "<-- saved next
        // player color");
        if ( !currentPlayer.getColor().toString().equals( nextPlayerColor ) )
        {
            switchPlayer();
        }
        player1.setName( sc.next() );
        player2.setName( sc.next() );
        JOptionPane.showMessageDialog( getWindow(),
            currentPlayer.getColor() + ": " + currentPlayer.getName()
                + "'s turn.",
            "Loade Game",
            JOptionPane.INFORMATION_MESSAGE );
        quickInfo.setText( "Game loaded." + currentPlayer.getColor() + ": "
            + currentPlayer.getName() + "'s turn." );
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
        pw.println( currentPlayer.getColor() );
        pw.println( player1.getName() );
        pw.println( player2.getName() );
        pw.close();
        quickInfo.setText( "Game saved. " + currentPlayer.getColor() + ": "
            + currentPlayer.getName() + "'s turn." );
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
        quickInfo.setText( "Home Screen. " + "will display what image here" );
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
        if ( atHome )
        {
            return false;
        }
        for ( int i = 0; i < 19; i++ )
        {
            for ( int j = 0; j < 19; j++ )
            {
                Stone s = board[i][j];
                if ( s != null )
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
        quickInfo.setText( "Move undone. " + currentPlayer.getColor() + ": "
                        + currentPlayer.getName() + "'s turn." );
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
        quickInfo.setText( "Move redone. " + currentPlayer.getColor() + ": "
                        + currentPlayer.getName() + "'s turn." );
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
            JOptionPane.showMessageDialog( getWindow(),
                "Please choose a valid location",
                "Invalid Location",
                JOptionPane.WARNING_MESSAGE );
        }
        else
        {
            // System.out.println("saying it is not saved");
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
            // System.out.println(currentPlayer.getColor().toString());
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

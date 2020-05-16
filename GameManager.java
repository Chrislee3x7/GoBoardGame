import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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


    public static void main( String[] args )
    {
        GameManager gm = new GameManager();
    }


    public GameManager()
    {

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
    
    public void undo()
    {
        System.out.println("Called undo");
        //gameBoard.printStones();
        Stone[][] previousBoardState = gameBoard.getPreviousBoardState();
        if (!canUndo())
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
    }


    public void redo()
    {
        // System.out.println("Called redo");
        Stone[][] undoneBoardState = gameBoard.getUndoneBoardState();
        if (!canRedo())
        {
            return;
        }
        gameBoard.setPreviousBoardState( gameBoard.getCurrentBoardState() );
        gameBoard.setBoardState( undoneBoardState );
        gameBoard.setUndoneBoardState( null );
        switchPlayer();
        commandPanel.updateButtons();
        drawBoard.repaint();
        gameBoard.getStoneZone().repaint();
    }


    public boolean playPiece( StoneColor color, BoardLocation location )
    {
        // returns if it was played or not (valid or invalid)
        boolean wasValidPlay = ( gameBoard ).addStone( color, location );
        if ( !wasValidPlay )
        {
            // player receives a message "pls choose nother place",
            // stone is not played, still same players turn
        }
        else
        {
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
        }
        else
        {
            // TODO
            // System.out.println("Please choose another location.");
        }
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

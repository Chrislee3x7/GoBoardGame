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

    public static void main( String[] args )
    {
        GameManager gm = new GameManager();
    }


    public GameManager()
    {
        
        
        window = new JFrame( "Go" );
        
        gameBoard = new GameBoard( this );
        
        JPanel drawBoard = (gameBoard).getDrawBoard();
        window.getContentPane().add( drawBoard);
        
        JComponent stoneZone = gameBoard.getStoneZone();
        window.setGlassPane( stoneZone );
        stoneZone.setVisible( true );
        drawBoard.addMouseListener( this );
        
        window.setBounds( 200, 30, 810, 835 );
        window.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        window.setResizable( false);
        window.setVisible( true );
        

        
       
        player1 = new HumanPlayer( StoneColor.BLACK );
        player2 = new HumanPlayer( StoneColor.WHITE );
        currentPlayer = player1;
    }


    private void switchPlayer()
    {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
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
        return wasValidPlay;
    }


    public GameBoard getBoard()
    {
        return gameBoard;
    }


    public void mouseClicked( MouseEvent e )
    {
        System.out.println("mouse clicked");
        BoardLocation loc = GameBoard.translateToLocation( e.getX(), e.getY() );
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

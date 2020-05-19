import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JPanel;


public class StoneZone extends JComponent
{
    private GameBoard myGameBoard;

    private GameManager gm;

    private int startFileIndex;
    
    private StoneColor winnerColor;
    
    private boolean isTieGame;


    public StoneZone( GameBoard gameBoard, GameManager gm )
    {
        this.myGameBoard = gameBoard;
        this.gm = gm;
        winnerColor = null;
        isTieGame = false;
        startFileIndex = 0;
    }


    // public void repaintLocation( BoardLocation location )
    // {
    // // Stone deletedStone = new Stone(StoneColor.RED, location, this);
    // // deletedStone.display();
    // repaint( new Rectangle( location.getX() * 40 + 20,
    // location.getY() * 40 + 20,
    // 40,
    // 40 ));
    // }
    
    public void paintFromFile( Graphics g, String startFileName )
    {
        Scanner sc = null;
        try
        {
            sc = new Scanner( new File( startFileName ) );
        }
        catch ( FileNotFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // System.out.println(Integer.valueOf(sc.next()));
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
                        BoardLocation loc = new BoardLocation( j, i );
                        Stone stone = new Stone( StoneColor.BLACK, loc, this );
                        stone.display( (Graphics2D)g );
                        break;
                    case 2:
                        BoardLocation loc2 = new BoardLocation( j, i );
                        Stone stone2 = new Stone( StoneColor.WHITE,
                            loc2,
                            this );
                        stone2.display( (Graphics2D)g );
                        break;
                    default:
                        System.out
                            .println( "incorrect color at: " + j + " " + i );
                }
            }
        }
        sc.close();
        // final ScheduledExecutorService executorService =
        // Executors.newSingleThreadScheduledExecutor();
        // executorService.scheduleWithFixedDelay(new Runnable() {
        // @Override
        // public void run() {
        // repaint();
        // }
        // }, 0, 20, TimeUnit.MINUTES);
    }

    public void tieGame()
    {
        isTieGame = true;
    }
    
    public void setWinnerColor(StoneColor color) 
    {
        winnerColor = color;
    }
    
    public void incrementStartFileIndex()
    {
        if ( startFileIndex == 3 )
        {
            startFileIndex = 0;
        }
        else
        {
            startFileIndex++;
        }
    }


    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );
        //System.out.println( "Called repaint for stonezone" );
        // if (i == 4)
        // {
        // i = 0;
        // }
        if ( gm.isAtHome() )
        {
            String startFileName = "";
            switch ( startFileIndex )
            {
                case 0:
                    startFileName = "StartPattern1";
                    break;
                case 1:
                    startFileName = "YinYangStartPattern";
                    break;
                case 2:
                    startFileName = "ThumbsUpStartPattern";
                    break;
                case 3:
                    startFileName = "MinecraftSwordStartPattern";
                    break;
            }
            gm.setQuickInfoMessage(
                " At Home. Currently displaying: " + startFileName );
            paintFromFile( g, startFileName );
        }
        else if (winnerColor != null)
        {
            paintFromFile(g, winnerColor.toString() + "WinPattern");
            winnerColor = null;
        }
        else if (isTieGame)
        {
            paintFromFile(g, "TieGamePattern");
            isTieGame = false;
        }
        else
        {
            for ( int i = 0; i < myGameBoard.getWidth(); i++ )
            {
                for ( int j = 0; j < myGameBoard.getHeight(); j++ )
                {
                    Stone stone = myGameBoard.getStone( i, j );
                    if ( stone != null )
                    {
                        // System.out.println("im a stone");
                        stone.display( (Graphics2D)g );
                        // getGraphics().fillRect( 40, 40, 40, 40 );
                    }
                }
            }
        }
    }
}

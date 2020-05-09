import java.awt.Graphics;

import javax.swing.JFrame;


public class Go
{
    private GameManager gm;


    public Go()
    {
        gm = new GameManager();
    }


    public static void main( String[] args )
    {
        Go window = new Go();

        // JFrame window = new JFrame("Go");
        // Canvas canvas = new GameManager();
        // canvas.setSize( WINDOW_LENGTH, WINDOW_LENGTH );
        // window.add( canvas );
        // window.pack();
        // window.setVisible( true );

    }
}

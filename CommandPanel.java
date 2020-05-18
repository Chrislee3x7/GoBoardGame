import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JPanel;


public class CommandPanel extends JPanel implements ActionListener
{
    private static final int BUTTON_WIDTH = 120;

    private static final int BUTTON_HEIGHT = 40;

    private static final int COMMAND_PANEL_WIDTH = BUTTON_WIDTH + 20;

    private static final int COMMAND_PANEL_HEIGHT = 800;

    private JButton newGameButton;

    private JButton saveGameButton;

    private JButton loadGameButton;

    private JButton homeButton;

    private JButton undoButton;

    private JButton redoButton;

    GameManager gm;


    public CommandPanel( GameManager gm )
    {
        setPreferredSize(
            new Dimension( COMMAND_PANEL_WIDTH, COMMAND_PANEL_HEIGHT ) );
        this.gm = gm;
        undoButton = new JButton( "Undo Move" );
        undoButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        redoButton = new JButton( "Redo Move" );
        redoButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        saveGameButton = new JButton( "Save Game" );
        saveGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        newGameButton = new JButton( "New Game" );
        newGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        loadGameButton = new JButton( "Load Game" );
        loadGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        homeButton = new JButton( "Home" );
        homeButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        add( newGameButton );
        add( saveGameButton );
        add( loadGameButton );
        add( undoButton );
        add( redoButton );
        add( homeButton );

        updateButtons();

        undoButton.addActionListener( this );
        redoButton.addActionListener( this );
        saveGameButton.addActionListener( this );
        newGameButton.addActionListener( this );
        loadGameButton.addActionListener( this );
        homeButton.addActionListener( this );

    }


    public void updateButtons()
    {
        undoButton.setEnabled( gm.canUndo() && !gm.isAtHome() );
        redoButton.setEnabled( gm.canRedo() && !gm.isAtHome() );
        saveGameButton.setEnabled( !gm.isAtHome() );
        newGameButton.setEnabled( gm.isAtHome() );
        loadGameButton.setEnabled( gm.canLoad() && gm.isAtHome() );
        homeButton.setEnabled( !gm.isAtHome() );
    }


    public void actionPerformed( ActionEvent e )
    {
        JButton button = (JButton)e.getSource();
        if ( button == undoButton )
        {
            gm.undo();
        }
        else if ( button == redoButton )
        {
            gm.redo();
        }
        else if ( button == saveGameButton )
        {
            gm.saveGame();
        }
        else if ( button == newGameButton )
        {
            gm.startNewGame();
        }
        else if ( button == loadGameButton )
        {
            gm.loadGame();
        }
        else if ( button == homeButton )
        {
            gm.goHome();
        }
        updateButtons();
    }
}

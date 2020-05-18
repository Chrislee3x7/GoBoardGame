import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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

    private JButton passTurnButton;

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
        passTurnButton = new JButton( "Pass Turn" );
        passTurnButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        add( newGameButton );
        add( saveGameButton );
        add( loadGameButton );
        add( undoButton );
        add( redoButton );
        add( passTurnButton );
        add( homeButton );

        updateButtons();

        undoButton.addActionListener( this );
        redoButton.addActionListener( this );
        saveGameButton.addActionListener( this );
        newGameButton.addActionListener( this );
        loadGameButton.addActionListener( this );
        homeButton.addActionListener( this );
        passTurnButton.addActionListener(this);
        setBackground( new Color(173,216,230) );
        setBorder( BorderFactory.createLineBorder( Color.BLACK ) );

    }


    public void updateButtons()
    {
        undoButton.setEnabled( gm.canUndo() && !gm.isAtHome() );
        redoButton.setEnabled( gm.canRedo() && !gm.isAtHome() );
        saveGameButton.setEnabled( gm.canSave() && !gm.isSaved() );
        newGameButton.setEnabled( gm.isAtHome() );
        loadGameButton.setEnabled( gm.canLoad() && gm.isAtHome() );
        homeButton.setEnabled( !gm.isAtHome() );
        passTurnButton.setEnabled( !gm.isAtHome() );
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
            tryToSave();
        }
        else if ( button == newGameButton )
        {
            gm.startNewGame();
        }
        else if ( button == loadGameButton )
        {
            gm.loadGame();
        }
        else if ( button == passTurnButton )
        {
            Object[] options = { "Yeah!", "Oops"};
            int n = JOptionPane.showOptionDialog( gm.getWindow(),
                "Are you sure you want to pass your turn? \nIf both players pass, the game will be over",
                "Confirm Pass Turn",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1] );
            if (n == 0)
            {
                gm.passTurn();
            }
        }
        else if ( button == homeButton )
        {
            if ( !gm.isSaved() )
            {
                tryToGoHomeWithoutSave();
                if ( !gm.isSaved() )
                {
                    Object[] options = { "Go home", "Wait, not yet" };
                    int n = JOptionPane.showOptionDialog( gm.getWindow(),
                        "Would you still like to go home?",
                        "Confirm Go Home",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1] );
                    if ( n == 0 )
                    {
                        gm.goHome();
                    }
                }
                else
                {
                    gm.goHome();
                }
            }
            else if ( gm.isSaved() )
            {
                gm.goHome();
            }
        }
    }


    public void tryToGoHomeWithoutSave()
    {
        Object[] options = { "Yeah!", "Nah, not this one", "Cancel" };
        int n = JOptionPane.showOptionDialog( gm.getWindow(),
            "Hmm... Looks like the current game isnt saved... \nWould you like to save?",
            "Save Progress?",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[2] );
        if ( n == 0 )
        {
            tryToSave();
        }
        else if ( n == 1 )
        {
            gm.goHome();
        }
        // else if (n == 1)
        // {
        //
        // }
    }


    public void tryToSave()
    {
        Object[] options = { "I'm sure!", "Cancel" };
        int n = JOptionPane.showOptionDialog( gm.getWindow(),
            "Are you sure? Clicking OK will overwrite the current saved game!",
            "Confirm Save",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            options[1] );
        if ( n == 0 )
        {
            undoButton.setEnabled( false );
            redoButton.setEnabled( false );
            gm.saveGame();
        }
    }
}

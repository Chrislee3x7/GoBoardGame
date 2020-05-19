import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class CommandPanel extends JPanel implements ActionListener
{
    private static final int BUTTON_WIDTH = 130;

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
        ImageIcon tempIcon = null;
        //UIManager.getIcon( "" )
        setPreferredSize(
            new Dimension( COMMAND_PANEL_WIDTH, COMMAND_PANEL_HEIGHT ) );
        this.gm = gm;
        //tempIcon = new ImageIcon("Undo.png");
        //undoButton = new JButton( tempIcon );
        undoButton = new JButton( "Undo Move" );
        undoButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        undoButton.setIcon( UIManager.getIcon( "Table.ascendingSortIcon" ) );
        
        redoButton = new JButton( "Redo Move" );
        redoButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        redoButton.setIcon( UIManager.getIcon( "Table.descendingSortIcon" ) );
        
        saveGameButton = new JButton( "Save Game" );
        saveGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        saveGameButton.setIcon( UIManager.getIcon( "FileView.floppyDriveIcon" ) );
        
        newGameButton = new JButton( "New Game" );
        newGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        newGameButton.setIcon( UIManager.getIcon( "Tree.leafIcon" ) );

        
        loadGameButton = new JButton( "Load Game" );
        loadGameButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        loadGameButton.setIcon( UIManager.getIcon( "FileChooser.upFolderIcon" ) );

        homeButton = new JButton( "Go Home" );
        homeButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        homeButton.setIcon( UIManager.getIcon( "FileChooser.homeFolderIcon" ) );
        
        passTurnButton = new JButton( "Pass Turn" );
        passTurnButton
            .setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        passTurnButton.setIcon( UIManager.getIcon( "Slider.verticalThumbIcon" ) );
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
        passTurnButton.addActionListener( this );
        setBackground( new Color( 173, 216, 230 ) );
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
            Object[] options = { "Yeah!", "Oops" };
            int n = JOptionPane.showOptionDialog( gm.getWindow(),
                "Are you sure you want to pass your turn? \nIf both players pass, the game will be over",
                "Confirm Pass Turn",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1] );
            if ( n == 0 )
            {
                gm.passTurn();
            }
        }
        else if ( button == homeButton )
        {
            if ( !gm.isSaved() )
            {
                tryToGoHomeWithoutSave();
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
            Object[] options2 = { "Go home", "Wait, not yet" };
            int m = JOptionPane.showOptionDialog( gm.getWindow(),
                "Would you still like to go home?",
                "Confirm Go Home",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options2,
                options2[1] );
            if ( m == 0 )
            {
                gm.goHome();
            }
        }
        else if ( n == 1 )
        {
            gm.goHome();
        }
        else if ( n == 2 )
        {

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

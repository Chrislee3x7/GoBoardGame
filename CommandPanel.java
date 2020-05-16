import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CommandPanel extends JPanel implements ActionListener
{
    JButton undoButton;
    
    JButton redoButton;
    
    GameManager gm;
    
    public CommandPanel(GameManager gm)
    {
        setPreferredSize( new Dimension(300, 800));
        this.gm = gm;
        undoButton = new JButton("Undo Move");
        redoButton = new JButton("Redo Move");
        add(undoButton);
        add(redoButton);
        updateButtons();
        
        undoButton.addActionListener( this );
        redoButton.addActionListener( this );
    }
    
    public void updateButtons()
    {
        undoButton.setEnabled( gm.canUndo() );
        redoButton.setEnabled( gm.canRedo() );
    }
    
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == undoButton)
        {   
            gm.undo();
        }
        else if (e.getSource() == redoButton)
        {
            gm.redo();
        }
    }
}

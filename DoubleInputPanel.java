import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class DoubleInputPanel extends JPanel
{
    private JTextField x;

    private JTextField y;

    private String xLabel;

    private String yLabel;


    public DoubleInputPanel( String xLabel, String yLabel )
    {
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        x = new JTextField( 12 );
        y = new JTextField( 12 );
        setLayout( new GridLayout(4, 1) );
        add( new JLabel( xLabel + ": " ) );
        add( x );
        add( new JLabel( yLabel + ": " ) );
        add( y );
        ;
    }
    
    public String getXField()
    {
        return x.getText();
    }
    
    public String getYField()
    {
        return y.getText();
    }
}

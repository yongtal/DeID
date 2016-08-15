/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author angelo
 */
public class WarningJdialog extends JDialog implements ActionListener{
    

  public WarningJdialog(JFrame parent, String title, String message) {
    super(parent, title, true);
    if (parent != null) {
      Dimension parentSize = parent.getSize(); 
      Point p = parent.getLocation(); 
      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
    }
    JPanel messagePane = new JPanel();
    messagePane.add(new JLabel(message));
    getContentPane().add(messagePane);
    JPanel buttonPane = new JPanel();
    JButton button = new JButton("OK"); 
    buttonPane.add(button); 
    button.addActionListener(this);
    getContentPane().add(buttonPane, BorderLayout.SOUTH);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    
    setLocationRelativeTo(this);
    
    pack(); 
    setVisible(true);
  }
  public void actionPerformed(ActionEvent e) {
    setVisible(false); 
    dispose(); 
  }
}

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class DisplayMain {
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1000, 1020);
        
        DisplayFrame panel = new DisplayFrame();
        f.add(panel, BorderLayout.CENTER);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
    
}
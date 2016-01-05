import javax.swing.JFrame;
import javax.swing.JPanel;

public class DisplayMain {
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setSize(1000, 1020);
        f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.add(new DisplayFrame());
    }
    
}
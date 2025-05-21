import javax.swing.JFrame;

public class FrameChat extends JFrame {

    public FrameChat() {
        setTitle("UniChat");
        setSize(630, 630);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        PanelChat panel = new PanelChat();
        add(panel);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }

}

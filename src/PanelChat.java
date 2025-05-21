import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.BorderFactory;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

public class PanelChat extends JPanel {
    private JTextField textField;

    public PanelChat() {
        setLayout(null);
        setBackground(new Color(0, 150, 215));
        JLabel headerLabel = new JLabel("Bem-vindo ao UniChat!");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBounds(0, 7, 600, 48);
        add(headerLabel);

        JPanel panelCen = new JPanel();
        panelCen.setBackground(Color.WHITE);
        panelCen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelCen.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(panelCen);
        scrollPane.setBounds(10, 59, 580, 470);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JButton btnEnviar = new JButton("ENVIAR");
        btnEnviar.setBounds(523, 539, 67, 35);
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        btnEnviar.setBackground(new Color(0, 120, 215));
        btnEnviar.setForeground(Color.WHITE);
        add(btnEnviar);

        JTextField inputField = new JTextField();
        inputField.setBounds(75, 539, 438, 35);
        add(inputField);
        inputField.setColumns(10);
        inputField.setBackground(Color.WHITE);
        // inputField.setText(" Clique aqui para digitar sua mensagem...");
        inputField.setForeground(Color.GRAY);

        JButton btnSelectFile = new JButton("");
        btnSelectFile.setBounds(26, 543, 21, 21);
        add(btnSelectFile);

        btnSelectFile.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
            }
        });
    }
}

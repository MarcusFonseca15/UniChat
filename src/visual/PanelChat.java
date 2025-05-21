package visual;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.BorderFactory;
import java.io.File;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;

//
public class PanelChat extends JPanel {
    private JTextField textField;
    private JPanel panelCen;
    private int yAtual = 10; // altura da prox msg
    private JButton btnEnviar;
    private JTextField inputField;

    private PrintWriter escritor;

    public void setEscritor(PrintWriter escritor) {
        this.escritor = escritor;
    }

    public PanelChat() {
        setLayout(null);
        setBackground(new Color(0, 150, 215));
        JLabel headerLabel = new JLabel("Bem-vindo ao UniChat!");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBounds(0, 7, 600, 48);
        add(headerLabel);

        this.panelCen = new JPanel();
        panelCen.setBackground(Color.WHITE);
        panelCen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelCen.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(panelCen);
        scrollPane.setBounds(10, 59, 580, 470);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        this.btnEnviar = new JButton("ENVIAR");
        btnEnviar.setBounds(523, 539, 67, 35);
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        btnEnviar.setBackground(new Color(0, 120, 215));
        btnEnviar.setForeground(Color.WHITE);
        add(btnEnviar);
        btnEnviar.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                addMensagem(msg, true);
                if (escritor != null) {
                    escritor.println(msg);
                }
                inputField.setText("");
            }
        });

        this.inputField = new JTextField();
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

    public void addMensagem(String texto, boolean enviada) {
        JLabel msgLabel = new JLabel(texto);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(enviada ? new Color(0, 200, 100) : new Color(230, 230, 230));
        msgLabel.setForeground(Color.BLACK);
        msgLabel.setBounds(enviada ? 180 : 10, yAtual, 380, 20);
        panelCen.add(msgLabel);
        panelCen.revalidate();
        panelCen.repaint();

        yAtual += 25; // sobe o y para a prox msg
    }

}

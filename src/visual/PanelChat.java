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
import java.awt.Dimension;

//
public class PanelChat extends JPanel {
    private JTextField textField;
    private JPanel panelCen;
    private JScrollPane scrollPane;
    private int yAtual = 10; // altura da prox msg
    private JButton btnEnviar;
    private JTextField inputField;

    private PrintWriter escritor;

    public void setEscritor(PrintWriter escritor) {
        this.escritor = escritor;
    }

    private String nomeUsuario;

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public PanelChat() {
        setLayout(null);
        setBackground(new Color(123, 124, 206));

        JLabel headerLabel = new JLabel("Bem-vindo ao UniChat!");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBounds(0, 7, 600, 48);
        headerLabel.setForeground(Color.WHITE);
        add(headerLabel);

        this.panelCen = new JPanel();
        panelCen.setBackground(new Color(51, 51, 51));
        panelCen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelCen.setLayout(null);

        this.scrollPane = new JScrollPane(panelCen);
        scrollPane.setBounds(10, 59, 580, 470);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        this.btnEnviar = new JButton("ENVIAR");
        btnEnviar.setBounds(523, 539, 67, 35);
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        btnEnviar.setBackground(Color.WHITE);
        btnEnviar.setForeground(Color.BLACK);
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
        inputField.setBackground(new Color(51, 51, 51));
        // inputField.setText(" Clique aqui para digitar sua mensagem...");
        inputField.setForeground(Color.WHITE);

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

        // Limpa a mensagem pra comparar o nome do usuário
        String textoFormatado = texto.toLowerCase().replaceAll("[\\[\\]\\s]", ""); // remove colchetes e espaços
        String nomeFormatado = nomeUsuario.toLowerCase();

        // Evita mostrar mensagem recebida que já foi enviada localmente (proprio
        // usuario)
        if (!enviada && texto.startsWith("[" + nomeUsuario + "]")) {
            return; // ignora
        }

        JLabel msgLabel = new JLabel(texto);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(enviada ? new Color(66, 133, 244) : new Color(86, 94, 117));
        msgLabel.setForeground(enviada ? Color.BLACK : Color.WHITE);
        msgLabel.setBounds(enviada ? 180 : 10, yAtual, 380, 20);

        panelCen.add(msgLabel);
        panelCen.revalidate();
        panelCen.repaint();

        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // Scroll automático

        yAtual += 25; // sobe o y para a prox msg
        panelCen.setPreferredSize(new Dimension(560, yAtual));

    }

}

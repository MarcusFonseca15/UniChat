package visual;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.BorderFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.SystemColor;

//
public class PanelChat extends JPanel {
    private JTextField textField;
    private JPanel panelCen;
    private JScrollPane scrollPane;
    private int yAtual = 10; // altura da prox msg
    private JButton btnEnviar;
    private JTextField inputField;

    private PrintWriter escritor;

    private DataOutputStream output;

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    private String nomeUsuario;

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario.toUpperCase();
    }

    private static final Color verdeClaro = new Color(0, 255, 102);

    public PanelChat() {
        setLayout(null);
        setBackground(SystemColor.control);

        JLabel headerLabel = new JLabel("UniChat!");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 32));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerLabel.setBounds(0, 7, 600, 48);
        headerLabel.setForeground(SystemColor.desktop);
        add(headerLabel);

        this.panelCen = new JPanel();
        panelCen.setBackground(SystemColor.text);
        panelCen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panelCen.setLayout(null);

        this.scrollPane = new JScrollPane(panelCen);
        scrollPane.setBounds(10, 59, 580, 470);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        this.btnEnviar = new JButton("ENVIAR");
        btnEnviar.setBounds(515, 539, 75, 35);
        btnEnviar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnEnviar.setBackground(verdeClaro);
        btnEnviar.setForeground(new Color(0, 0, 0));
        add(btnEnviar);
        btnEnviar.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (output != null && !msg.isEmpty()) {
                try {
                    String msgFormatada = "[" + nomeUsuario + "] " + msg;
                    output.writeUTF(msgFormatada);
                    output.flush();
                    addMensagem(msgFormatada, true); // Exibir localmente
                    inputField.setText(""); // LIMPAR
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.inputField = new JTextField();
        inputField.setBounds(75, 539, 430, 35);
        add(inputField);
        inputField.setColumns(10);
        inputField.setBackground(new Color(255, 255, 255));
        // inputField.setText(" Clique aqui para digitar sua mensagem...");
        inputField.setForeground(new Color(0, 0, 0));

        inputField.addActionListener(e -> btnEnviar.doClick());

        JButton btnSelectFile = new JButton("File");
        btnSelectFile.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSelectFile.setBounds(10, 539, 55, 35);
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

        // LIMPA MSG PRA COLOCAR NOME DO USER
        String textoFormatado = texto.toUpperCase().replaceAll("[\\[\\]\\s]", ""); // remove colchetes e espaços
        String nomeFormatado = nomeUsuario.toUpperCase();

        // Evita mostrar mensagem recebida que já foi enviada localmente (proprio
        // usuario)
        if (!enviada && texto.startsWith("[" + nomeUsuario.toUpperCase() + "]")) {
            return; // ignora
        }

        JLabel msgLabel = new JLabel(texto);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        msgLabel.setOpaque(true);
        msgLabel.setBackground(enviada ? verdeClaro : new Color(166, 166, 166));
        msgLabel.setForeground(new Color(0, 0, 0));
        msgLabel.setBounds(enviada ? 180 : 10, yAtual, 380, 20);

        panelCen.add(msgLabel);
        panelCen.revalidate();
        panelCen.repaint();

        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // Scroll automático

        yAtual += 25; // sobe o y para a prox msg
        panelCen.setPreferredSize(new Dimension(560, yAtual));

    }

}

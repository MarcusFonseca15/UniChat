package cliente;

import visual.FrameChat;
import visual.PanelChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

public class MainClient {
    public static void main(String[] args) {
        try {
            InetAddress endereco = InetAddress.getByName("127.0.0.1"); // IP
            int porta = 12345;

            Socket socket = new Socket(endereco, porta);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            String nome = JOptionPane.showInputDialog(null, "Digite seu nome:");
            out.writeUTF(nome); // envia o nome pro servidor

            FrameChat frame = new FrameChat();
            PanelChat panel = frame.getPanel();
            panel.setOutput(out);
            panel.setNomeUsuario(nome);

            // Thread pra ESCUTAR mensagens
            Thread receptor = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readUTF()) != null) {
                        final String mensagemFinal = msg;
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            panel.addMensagem(mensagemFinal, false);
                        });
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            });

            receptor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

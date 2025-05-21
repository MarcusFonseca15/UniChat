package TESTE;

import visual.FrameChat;
import visual.PanelChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class MainClient {
    public static void main(String[] args) {

        try {
            // Conectar ao servidor
            Socket socket = new Socket("localhost", 8089);
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

            // pergunta o nome
            String nome = JOptionPane.showInputDialog(null, "Digite seu nome:");
            escritor.println(nome); // envia o nome pro servidor

            ////////////////////////
            FrameChat frame = new FrameChat();
            PanelChat panel = frame.getPanel();
            panel.setEscritor(escritor); // envia msg pelo butao
            panel.setNomeUsuario(nome);

            // thread pa ESCUTAR mensagens do servidor
            Thread receptor = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = leitor.readLine()) != null) {
                        panel.addMensagem(msg, false); // false = recebida
                    }
                } catch (Exception e) {
                    System.out.println("Conexão encerrada.");
                }
            });

            receptor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

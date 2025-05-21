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
            // 1. Conectar ao servidor
            Socket socket = new Socket("localhost", 8089);
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

            // 2. Pergunta o nome com JOptionPane (interface gráfica)
            String nome = JOptionPane.showInputDialog(null, "Digite seu nome:");
            escritor.println(nome); // envia o nome pro servidor

            // 3. Criar a interface
            FrameChat frame = new FrameChat(); // Janela principal
            PanelChat panel = frame.getPanel(); // Recupera o painel
            panel.setEscritor(escritor); // Permite enviar mensagens pelo botão
            panel.setNomeUsuario(nome);

            // 4. Criar thread para ESCUTAR mensagens do servidor
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

            receptor.start(); // inicia thread que escuta o servidor

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

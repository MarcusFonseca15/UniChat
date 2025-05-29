package cliente;

import visual.FrameChat;
import visual.PanelChat;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import javax.swing.JOptionPane;

public class MainClient {
    public static void main(String[] args) {
        try {
            InetAddress endereco = InetAddress.getByName("127.0.0.1");
            int porta = 12345;

            Socket socket = new Socket(endereco, porta);

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            String nome = JOptionPane.showInputDialog(null, "Digite seu nome:");
            if (nome == null || nome.trim().isEmpty()) {
                nome = "Usuario";
            }

            FrameChat frame = new FrameChat();
            PanelChat panel = frame.getPanel();
            panel.setOutput(out);
            panel.setNomeUsuario(nome.toUpperCase());

            // 🟩 Criação da pasta recebidos
            File recebidosDir = new File("recebidos");
            if (!recebidosDir.exists()) {
                recebidosDir.mkdir();
            }

            // 🎧 Thread para escutar mensagens e arquivos
            Thread receptor = new Thread(() -> {
                try {
                    while (true) {
                        String tipo = in.readUTF();
                        
                        if ("TEXTO".equals(tipo)) {
                            processarMensagemTexto(in, panel);
                        } else if ("ARQUIVO".equals(tipo)) {
                            processarArquivoRecebido(in, panel, recebidosDir);
                        }
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
    
    private static void processarMensagemTexto(DataInputStream in, PanelChat panel) {
        try {
            String mensagem = in.readUTF();
            
            javax.swing.SwingUtilities.invokeLater(() -> {
                panel.addMensagem(mensagem, false);
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void processarArquivoRecebido(DataInputStream in, PanelChat panel, File recebidosDir) {
        try {
            // Lê informações do arquivo
            String nomeArquivo = in.readUTF();
            long tamanhoArquivo = in.readLong();
            
            System.out.println("Recebendo arquivo: " + nomeArquivo + " (" + tamanhoArquivo + " bytes)");
            
            // Salva o arquivo na pasta recebidos
            File arquivo = new File(recebidosDir, nomeArquivo);
            
            try (FileOutputStream fos = new FileOutputStream(arquivo)) {
                byte[] buffer = new byte[4096];
                long bytesRestantes = tamanhoArquivo;
                
                while (bytesRestantes > 0) {
                    int bytesParaLer = (int) Math.min(buffer.length, bytesRestantes);
                    int bytesLidos = in.read(buffer, 0, bytesParaLer);
                    
                    if (bytesLidos == -1) {
                        throw new IOException("Conexão interrompida durante a transferência do arquivo");
                    }
                    
                    fos.write(buffer, 0, bytesLidos);
                    bytesRestantes -= bytesLidos;
                }
            }
            
            System.out.println("Arquivo salvo: " + arquivo.getAbsolutePath());
            
            // Exibe a mensagem no chat
            javax.swing.SwingUtilities.invokeLater(() -> {
                String mensagemArquivo = "📎 Arquivo recebido: " + nomeArquivo;
                panel.addMensagem(mensagemArquivo, false);
                
                // Pergunta se quer abrir o arquivo
                int resposta = JOptionPane.showConfirmDialog(
                    null, 
                    "Arquivo recebido: " + nomeArquivo + "\nDeseja abrir?", 
                    "Arquivo Recebido", 
                    JOptionPane.YES_NO_OPTION
                );
                
                if (resposta == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(arquivo);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Erro ao abrir o arquivo: " + nomeArquivo);
                    }
                }
            });
            
        } catch (IOException e) {
            System.err.println("Erro ao receber arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
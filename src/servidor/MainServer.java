package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServer {
    private static final int PORTA = 12345;
    private static final List<ClienteInfo> clientes = new CopyOnWriteArrayList<>();
    
    // Classe para armazenar informações do cliente
    static class ClienteInfo {
        DataOutputStream output;
        Socket socket;
        String endereco;
        
        ClienteInfo(DataOutputStream output, Socket socket) {
            this.output = output;
            this.socket = socket;
            this.endereco = socket.getInetAddress().toString();
        }
    }

    public static void main(String[] args) {
        // Criar pasta para arquivos no servidor
        File arquivosDir = new File("arquivos_servidor");
        if (!arquivosDir.exists()) {
            arquivosDir.mkdir();
        }
        
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectou: " + cliente.getInetAddress());

                DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                ClienteInfo clienteInfo = new ClienteInfo(out, cliente);
                clientes.add(clienteInfo);

                Thread threadCliente = new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(cliente.getInputStream());

                        while (true) {
                            // Lê o tipo de dados (TEXTO ou ARQUIVO)
                            String tipo = in.readUTF();
                            
                            if ("TEXTO".equals(tipo)) {
                                processarMensagemTexto(in, clienteInfo);
                            } else if ("ARQUIVO".equals(tipo)) {
                                processarArquivo(in, clienteInfo, arquivosDir);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Cliente desconectou: " + cliente.getInetAddress());
                    } finally {
                        try {
                            clientes.remove(clienteInfo);
                            cliente.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                threadCliente.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void processarMensagemTexto(DataInputStream in, ClienteInfo remetente) {
        try {
            String mensagem = in.readUTF();
            System.out.println("Mensagem de " + remetente.endereco + ": " + mensagem);
            
            // Envia mensagem para todos os clientes
            for (ClienteInfo cliente : clientes) {
                try {
                    cliente.output.writeUTF("TEXTO");
                    cliente.output.writeUTF(mensagem);
                    cliente.output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void processarArquivo(DataInputStream in, ClienteInfo remetente, File arquivosDir) {
        try {
            // Lê informações do arquivo
            String nomeArquivo = in.readUTF();
            long tamanhoArquivo = in.readLong();
            
            System.out.println("Recebendo arquivo: " + nomeArquivo + " (" + tamanhoArquivo + " bytes) de " + remetente.endereco);
            
            // Salva o arquivo no servidor
            File arquivo = new File(arquivosDir, nomeArquivo);
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
            
            // Envia o arquivo para todos os outros clientes
            for (ClienteInfo cliente : clientes) {
                if (cliente != remetente) { // Não reenvia para quem enviou
                    try {
                        enviarArquivoParaCliente(cliente, arquivo, nomeArquivo);
                    } catch (Exception e) {
                        System.err.println("Erro ao enviar arquivo para " + cliente.endereco + ": " + e.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void enviarArquivoParaCliente(ClienteInfo cliente, File arquivo, String nomeArquivo) throws IOException {
        cliente.output.writeUTF("ARQUIVO");
        cliente.output.writeUTF(nomeArquivo);
        cliente.output.writeLong(arquivo.length());
        
        // Envia o conteúdo do arquivo
        try (java.io.FileInputStream fis = new java.io.FileInputStream(arquivo)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                cliente.output.write(buffer, 0, bytesRead);
            }
        }
        
        cliente.output.flush();
        System.out.println("Arquivo " + nomeArquivo + " enviado para " + cliente.endereco);
    }
}
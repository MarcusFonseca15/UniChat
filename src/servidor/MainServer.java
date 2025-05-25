package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainServer {
    private static final int PORTA = 12345;
    private static final List<DataOutputStream> clientes = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor iniciado na porta " + PORTA);

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectou: " + cliente.getInetAddress());

                DataOutputStream out = new DataOutputStream(cliente.getOutputStream());
                clientes.add(out);

                Thread threadCliente = new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(cliente.getInputStream());

                        while (true) {
                            String mensagem = in.readUTF();
                            System.out.println("Receb" + cliente.getInetAddress() + ":" + mensagem);

                            // Envia pra todos
                            for (DataOutputStream o : clientes) {
                                try {
                                    o.writeUTF(mensagem);
                                    o.flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Cliente desconectou:" + cliente.getInetAddress());
                    } finally {
                        try {
                            clientes.remove(out);
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
}

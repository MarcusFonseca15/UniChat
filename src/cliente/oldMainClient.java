package cliente;

import visual.FrameChat;
import visual.PanelChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class oldMainClient {
    public static void main(String[] args) {

        System.out.println("Digite seu nome:");
        Scanner scanner = new Scanner(System.in);
        String nome = scanner.nextLine();

        try (Socket socket = new Socket("localhost", 8089);
                BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

        ) {

            escritor.println(nome); // envia o nome do cliente pro o servidor

            // Thread pra escutar a transmissão
            Thread receptor = new Thread(() -> {
                String msgRecebida;
                try {
                    while ((msgRecebida = leitor.readLine()) != null) {
                        System.out.println(msgRecebida);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Socket já fechado.");
                }
            });
            receptor.start(); // inicia a thread pra escutar o servidor

            // Prepara pra enviar mensagens
            while (true) {
                Thread.sleep(3000); // delay de 3 segundos entre msgs
                System.out.println("Digite sua mensagem:");
                String msg = scanner.nextLine();

                // SAIR DO CHAT
                if (msg.equals("//sair")) {
                    break;
                }

                escritor.println(msg);
                // a msg vai pro servidor
            }
            receptor.interrupt(); // interrompe a thread de escuta
            socket.close();
            scanner.close();
            System.out.println("Você saiu do chat.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

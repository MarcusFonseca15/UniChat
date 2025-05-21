import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    private static final int PORTA = 8089;

    private static Set<PrintWriter> escritores = new HashSet<>(); // lista de escritores para saber quem são os clientes

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(PORTA)) {

            System.out.println("Servidor rodando na porta " + PORTA);

            while (true) {
                Thread.sleep(3000); // delay de 3 segundos entre msgs
                new ClienteHandler(
                        server.accept(),
                        escritores).start(); // Cria um novo cliente e herda de thread

                // sempre que um cliente chegar, passo a lista de escritores
            }

            // como preciso saber quem são os outros clientes, preciso criar uma lista de
            // escritores

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar o servidor.");
        }

        FrameChat frame = new FrameChat();
    }
}

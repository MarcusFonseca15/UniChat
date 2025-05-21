package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

//CLIENTE SE CONECTA
//ADD NA LSITA DE CLEINTES
//ESPERA COLOCAR NOME
//AVISA QUE ENTROU NO CHAT
//ENVIAR A MSG QUE ESCREVEU
//SE SAIR, AVISA QUE SAIU DO CHAT
public class ClienteHandler extends Thread {

    private Socket socket; // socket do cliente

    private PrintWriter escritor; // o que escreve para o cliente

    private Set<PrintWriter> listaEscritores; // lista de escritores para saber quem são os clientes

    ClienteHandler(Socket socket, Set<PrintWriter> listaEscritores) {
        this.socket = socket;
        this.listaEscritores = listaEscritores;
    }

    @Override
    public void run() {
        try {
            // cliente handler rodando a thread
            // espera o nome do cliente (quando ele entra no servidor)
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            escritor = new PrintWriter(socket.getOutputStream(), true);

            synchronized (listaEscritores) { // evita que dois clientes usam a lista ao mesmo tempo
                listaEscritores.add(escritor); // sempre que cliente conecta, add o escritor na lista

            }

            String nome = leitor.readLine();
            transmitir("[" + nome + "]" + " entrou no chat");
            System.out.println("[" + nome + "]" + " conectou no servidor");

            // agora que o cliente entrou, ele pode mandar mensagem

            String msg;

            while ((msg = leitor.readLine()) != null) { // enqt ele nao envia nada, ele espera o comando de entrada
                transmitir("[" + nome + "]" + ": " + msg); // Fulano: Bom dia

                // SAIR DO CHAT
                if (msg.equals("//sair")) {
                    synchronized (listaEscritores) {
                        listaEscritores.remove(escritor);
                    }
                    transmitir("[" + nome + "]" + " saiu do chat");
                    System.out.println("[" + nome + "]" + " desconectou do servidor");

                    break;

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void transmitir(String msg) {

        synchronized (listaEscritores) { // evita que dois clientes usam a lista ao mesmo tempo
            listaEscritores.forEach(escritor -> {
                escritor.println(msg); // manda a mensagem pra todos os clientes
            });
        }
    } // transmitir

}

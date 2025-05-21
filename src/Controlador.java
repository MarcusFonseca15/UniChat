/*
 * import javafx.fxml.Initializable;
 * import java.io.IOException;
 * import java.net.ServerSocket;
 * import java.net.URL;
 * import java.util.ResourceBundle;
 * 
 * public class Controlador implements Initializable {
 * 
 * private JButton btnEnviar = PanelChat.btnEnviar;
 * private JTextField inputField = PanelChat.inputField;
 * private JButton btnSelectFile = PanelChat.btnSelectFile;
 * private JPanel panelCen = PanelChat.panelCen;
 * private JScrollPane scrollPane = PanelChat.scrollPane;
 * 
 * private Server server;
 * 
 * @Override
 * public void initialize(URL location, ResourceBundle resources) {
 * 
 * try {
 * server = new Server(new ServerSocket(1234));
 * } catch (IOException e) {
 * e.printStackTrace();
 * System.out.println("Erro ao criar o servidor.");
 * }
 * 
 * inputField.addActionListener(e -> {
 * String message = inputField.getText();
 * if (!message.isEmpty()) {
 * server.sendMessage(message);
 * inputField.setText("");
 * }
 * });
 * }
 * 
 * }
 */
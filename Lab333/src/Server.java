import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Application{

    public static int port = 8000;
    public static int connections = 0;

    public static ServerSocket serverSocket;
    public static Socket clientSocket;

    public static BufferedReader input;
    public static BufferedWriter output;

    public static TextArea textArea1;
    public static TextArea textArea2;
    public static Text label2;


    public static void main(String[] args) {
        new Thread(()->{launch(args);}).start();
        new Thread(()->{StartServer();}).start();
    }

    @Override
    public void start(Stage stage) {
        Text title = Objects.createText("Server", FontWeight.BOLD, 16, Color.BLUE, 50, 20);
        Text label1 = Objects.createText("Port: " + port, FontWeight.NORMAL, 12, Color.GRAY, 330, 20);
        label2 = Objects.createText("Connections: " + connections, FontWeight.NORMAL, 12, Color.GRAY, 460, 20);
        textArea1 = Objects.createTextArea(18, 6, 40, 30);
        textArea2 = Objects.createTextArea(18, 6, 320, 30);

        Group group = new Group();
        group.getChildren().add(title);
        group.getChildren().add(label1);
        group.getChildren().add(label2);
        group.getChildren().add(textArea1);
        group.getChildren().add(textArea2);

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("Лабораторна робота");
        stage.setWidth(605);
        stage.setHeight(230);
        stage.setX(400);
        stage.setY(300);
        //stage.setX(1200);
        //stage.setY(200);
        stage.show();
    }

    private static void StartServer() {
        try {
            serverSocket = new ServerSocket(port);

            String s0;
            String[] s1;
            String s2 = "";

            while(true) {
                clientSocket = serverSocket.accept();

                textArea2.appendText(">>> Connected: " + clientSocket.getLocalAddress().toString().replace("/", "") + '\n');
                connections++;
                label2.setText("Connections: " + connections);

                while(true) {
                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                    s0 = input.readLine();
                    if(s0 != null){

                        if (s0.equals("close")) {
                            textArea2.appendText("<<< Disconnected: " + clientSocket.getLocalAddress().toString().replace("/", "") + '\n');
                            connections--;
                            label2.setText("Connections: " + connections);

                            clientSocket.close();

                            break;
                        }else {
                            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                            s1 = s0.split(" ");

                            textArea1.appendText("Операнди: \n" + s1[0] + '\n' + s1[2] + '\n');

                            if (s1[1].equals("+")) {
                                s2 = "Сума: " + (Double.parseDouble(s1[0]) + Double.parseDouble(s1[2])) + "\n";
                                textArea1.appendText("Сума: \n" + (Double.parseDouble(s1[0]) + Double.parseDouble(s1[2])) + '\n');
                            }
                            if (s1[1].equals("*")) {
                                s2 = "Добуток: " + (Double.parseDouble(s1[0]) * Double.parseDouble(s1[2])) + "\n";
                                textArea1.appendText("Добуток: \n" + (Double.parseDouble(s1[0]) * Double.parseDouble(s1[2])) + '\n');
                            }
                            if (s1[1].equals("sin")) {
                                s2 = "Синус: " + Math.sin(Double.parseDouble(s1[0])) + " " + Math.sin(Double.parseDouble(s1[2])) + "\n";
                                textArea1.appendText("Синус: \n" + Math.sin(Double.parseDouble(s1[0])) + '\n' + Math.sin(Double.parseDouble(s1[2])) + '\n');
                            }
                            if (s1[1].equals("tan")) {
                                s2 = "Тангенс: " + Math.tan(Double.parseDouble(s1[0])) + " " + Math.tan(Double.parseDouble(s1[2])) + "\n";
                                textArea1.appendText("Тангенс: \n" + Math.tan(Double.parseDouble(s1[0])) + '\n' + Math.tan(Double.parseDouble(s1[2])) + '\n');
                            }

                            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                            output.write(s2);
                            output.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
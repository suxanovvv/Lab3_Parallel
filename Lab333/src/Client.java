import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.*;
import java.net.Socket;

public class Client extends Application{

    public static int port = 8000;
    public static String host = "127.0.0.1";
    public static int connections = 0;

    public static double operand1 = 10.0;
    public static double operand2 = 20.0;

    public static Socket clientSocket;

    public static BufferedReader input;
    public static BufferedWriter output;

    public static TextArea textArea;


    public static void main(String[] args) {
        new Thread(()->{launch(args);}).start();
        new Thread(()->{ ClientFunction();}).start();
    }

    @Override
    public void start(Stage stage) {
        Text title = Objects.createText("Client", FontWeight.BOLD, 16, Color.BLUE, 50, 20);

        textArea = Objects.createTextArea(12, 10, 40, 30);

        Text label1 = Objects.createText("Port", FontWeight.NORMAL, 14, Color.GRAY, 230, 30);
        Text label2 = Objects.createText("Host", FontWeight.NORMAL,14, Color.GRAY, 230, 60);
        Text label3 = Objects.createText("Operand 1", FontWeight.NORMAL, 14, Color.GRAY, 230, 100);
        Text label4 = Objects.createText("Operand 2", FontWeight.NORMAL, 14, Color.GRAY, 230, 130);

        TextField textField1 = Objects.createTextField(Integer.toString(port), 320, 30);
        TextField textField2 = Objects.createTextField(host, 320, 60);
        TextField textField3 = Objects.createTextField(Double.toString(operand1), 320, 100);
        TextField textField4 = Objects.createTextField(Double.toString(operand2), 320, 130);

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton radioButton1 = Objects.createRadioButton("Sum", toggleGroup,230, 180);
        RadioButton radioButton2 = Objects.createRadioButton("Mul", toggleGroup,300, 180);
        RadioButton radioButton3 = Objects.createRadioButton("Sin", toggleGroup,370, 180);
        RadioButton radioButton4 = Objects.createRadioButton("Tan", toggleGroup,440, 180);

        Button button1 = Objects.createButton("Connect", 510, 30);
        Button button2 = Objects.createButton("Disconnect", 510, 60);
        Button button3 = Objects.createButton("Submit", 510, 180);

        toggleGroup.selectToggle(radioButton1);

        button2.setDisable(true);
        button3.setDisable(true);

        button1.setOnAction(value ->  {
            try{
                port = Integer.parseInt(textField1.getText());
                host = textField2.getText();
                clientSocket = new Socket(host, port);
                textArea.appendText(">>> Connected: " + "\nPort: " + port + "\nHost: " + host + '\n');
                connections++;

                button1.setDisable(true);
                button2.setDisable(false);
                button3.setDisable(false);
            } catch (IOException e){
                textArea.appendText("== Connection failed: " + "\nPort: " + port + "\nHost: " + host + '\n');
                e.printStackTrace();
            }
        });

        button2.setOnAction(value ->  {
            try{
                output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                output.write("close");
                output.flush();

                clientSocket.close();
                textArea.appendText("<<< Disconnected: " + "\nPort: " + port + "\nHost: " + host + '\n');
                connections--;

                button1.setDisable(false);
                button2.setDisable(true);
                button3.setDisable(true);
            } catch (IOException e){
                e.printStackTrace();
            }
        });

        button3.setOnAction(value ->  {
            Task<Void> new_task = new Task<>() {
                @Override
                protected Void call() {
                    Platform.runLater(() -> {
                        try{
                            operand1 = Double.parseDouble(textField3.getText());
                            operand2 = Double.parseDouble(textField4.getText());

                            output = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                            String s = "";
                            if(toggleGroup.getSelectedToggle() == radioButton1){
                                s = operand1 + " + " + operand2 + "\n";
                            }
                            if(toggleGroup.getSelectedToggle() == radioButton2){
                                s = operand1 + " * " + operand2 + "\n";
                            }
                            if(toggleGroup.getSelectedToggle() == radioButton3){
                                s = operand1 + " sin " + operand2 + "\n";
                            }
                            if(toggleGroup.getSelectedToggle() == radioButton4){
                                s = operand1 + " tan " + operand2 + "\n";
                            }

                            output.write(s);
                            output.flush();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    });
                    return null;
                }
            };

            Thread thread = new Thread(new_task);
            thread.start();
        });


        Group group = new Group();
        group.getChildren().add(title);
        group.getChildren().add(textArea);
        group.getChildren().add(label1);
        group.getChildren().add(label2);
        group.getChildren().add(label3);
        group.getChildren().add(label4);
        group.getChildren().add(textField1);
        group.getChildren().add(textField2);
        group.getChildren().add(textField3);
        group.getChildren().add(textField4);
        group.getChildren().add(radioButton1);
        group.getChildren().add(radioButton2);
        group.getChildren().add(radioButton3);
        group.getChildren().add(radioButton4);
        group.getChildren().add(button1);
        group.getChildren().add(button2);
        group.getChildren().add(button3);

        Scene scene = new Scene(group);
        stage.setScene(scene);
        stage.setTitle("Лабораторна робота");
        stage.setWidth(650);
        stage.setHeight(300);
        stage.setX(1100);
        stage.setY(300);
        //stage.setX(1200);
        //stage.setY(600);
        stage.show();
    }

    public static void ClientFunction(){
        String s0;
        String[] s1;

        try{

            while(true){
                System.out.print("");
                if(clientSocket != null && !clientSocket.isClosed()){

                    input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    s0 = input.readLine();
                    if(s0 != null){
                        s1 = s0.split(" ");

                        if(s1[0].equals("Сума:") || s1[0].equals("Добуток:")){
                            textArea.appendText(s1[0] + '\n' + s1[1] + '\n');
                        }else {
                            textArea.appendText(s1[0] + '\n' + s1[1] + '\n' + s1[2] + '\n');
                        }
                    }
                }
            }

        }catch(Exception e){
            if(!e.getClass().getCanonicalName().equals("java.net.SocketException")){
                e.printStackTrace();
            }
        }

    }

}
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Objects {
    public static Text createText(String line, FontWeight type, int size, Color color, int x, int y){
        Text text = new Text(line);
        text.setFont(Font.font("Arial", type, size));
        text.setFill(color);
        text.setLayoutX(x);
        text.setLayoutY(y);
        return text;
    }
    public static TextArea createTextArea(int columns, int rows, int x, int y){
        TextArea textArea = new TextArea();
        textArea.setPrefColumnCount(columns);
        textArea.setPrefRowCount(rows);
        textArea.setLayoutX(x);
        textArea.setLayoutY(y);
        return textArea;
    }

    public static TextField createTextField(String text, int x, int y){
        TextField textField = new TextField(text);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        return textField;
    }

    public static RadioButton createRadioButton(String text, ToggleGroup toggleGroup, int x, int y){
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setLayoutX(x);
        radioButton.setLayoutY(y);
        return radioButton;
    }

    public static Button createButton(String text, int x, int y){
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.web("#ff0000"));
        button.setMinWidth(100);
        return button;
    }
}
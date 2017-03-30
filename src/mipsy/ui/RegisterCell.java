package mipsy.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * Created by Adnan on 3/30/2017.
 */
public class RegisterCell
{
    @FXML
    private HBox hBox;

    @FXML
    private Label registerLabel;
    @FXML
    private TextField registerValue;

    public RegisterCell()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registercell.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public HBox getBox()
    {
        return hBox;
    }

    public void setLabel(String label) {
        registerLabel.setText(label);
    }

    public void setValue(int val) {
        registerValue.setText(val + "");
    }

    public int getValue() {
        try {
            return Integer.parseInt(registerValue.getText());
        } catch ( Exception ex ) {
            return 0;
        }
    }
}
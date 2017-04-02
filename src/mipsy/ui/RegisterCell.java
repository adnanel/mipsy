package mipsy.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import mipsy.types.Register;

import java.io.IOException;

/**
 * Created on 3/30/2017.
 */
public class RegisterCell
{
    @FXML
    private HBox hBox;

    @FXML
    private Label registerLabel;
    @FXML
    private TextField registerValue;

    private Register register;

    public RegisterCell(Register register, boolean readonly)
    {
        this.register = register;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registercell.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();

            registerValue.setOnKeyReleased(event -> register.value = getValue());

            setValue(register.value);
            setLabel(register.name);

            registerValue.setEditable(!readonly);
            registerValue.setDisable(readonly);
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
            setValue(0);
            return 0;
        }
    }
}
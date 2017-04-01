package mipsy.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import mipsy.types.MemoryEntry;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Adnan on 3/30/2017.
 */
public class MemoryCell
{
    @FXML
    private HBox hBox;

    @FXML
    private Label memAddress;
    @FXML
    private TextField memValue;


    HashMap<Integer, MemoryEntry> memory;
    Integer address;

    public MemoryCell(HashMap<Integer, MemoryEntry> memory, Integer address, boolean readonly)
    {
        this.memory = memory;
        this.address = address;
        MemoryEntry value = memory.containsKey(address) ? memory.get(address) : new MemoryEntry(address, 0);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("memorycell.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();

            memValue.setOnKeyReleased(event -> {
                if ( !memory.containsKey(address) )
                    memory.put(address, new MemoryEntry(address, getValue()));
                else
                    memory.get(address).value = getValue();
            });

            setValue(value.value);
            setLabel(value.address + ""); //todo convert to hex

            memValue.setEditable(!readonly);
            memValue.setDisable(readonly);
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
        memAddress.setText(label);
    }

    public void setValue(int val) {
        memValue.setText(val + "");
    }

    public int getValue() {
        try {
            return Integer.parseInt(memValue.getText());
        } catch ( Exception ex ) {
            setValue(0);
            return 0;
        }
    }
}
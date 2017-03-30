package mipsy;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import mipsy.ui.RegisterCell;
import mipsy.ui.listviewcells.ListViewCellRegister;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private ListView lvRegisters;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // change next line to DB load
        List<String> values = Arrays.asList("one", "two", "three");

        lvRegisters.setItems(FXCollections.observableList(values));

        lvRegisters.setCellFactory(listView -> new ListViewCellRegister());
    }


    @FXML
    protected void menuAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("MIPSy " + AppInfo.VERSION);
        alert.setContentText(
                "Authors:\n\n" +
                "Adnan Elezović ( aelezovic2@etf.unsa.ba )\n" +
                "Haris Halilović ( hhalilovic1@etf.unsa.ba )");

        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                System.out.println("Pressed OK.");
            }
        });
    }
}

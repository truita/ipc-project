package upv.ipc.runninglasafor.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class ActivityCellController implements Initializable {

    @FXML
    public Label name;

    @FXML
    public Text date;

    @FXML
    public Text distance;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}

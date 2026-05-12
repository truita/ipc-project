package upv.ipc.runninglasafor.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import upv.ipc.runninglasafor.ActivityListCell;
import upv.ipc.runninglasafor.App;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

public class MainController implements Initializable {

    @FXML
    private ListView<Activity> activityList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activityList.setCellFactory(c -> new ActivityListCell());
        activityList
            .getItems()
            .addAll(SportActivityApp.getInstance().getAllActivities());
    }

    @FXML
    private void editUser() throws IOException {
        App.setRoot("editProfile");
    }
}

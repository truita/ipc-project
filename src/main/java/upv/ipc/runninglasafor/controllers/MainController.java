package upv.ipc.runninglasafor.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import upv.ipc.runninglasafor.ActivityListCell;
import upv.ipc.runninglasafor.App;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.SportActivityApp;

public class MainController implements Initializable {

    @FXML
    private ListView<Activity> activityList;

    @FXML
    private StackPane map;

    @FXML
    private MapController mapController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        activityList.setCellFactory(c -> new ActivityListCell());
        activityList
            .getItems()
            .addAll(SportActivityApp.getInstance().getAllActivities());
        activityList
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((_observable, _old, selectedActivity) -> {
                mapController.setActivity(selectedActivity);
            });
    }

    // TODO: change speed button icon when toggled
    @FXML
    private void toggleSpeed() {
        mapController.speedToggle.set(!mapController.speedToggle.get());
    }

    @FXML
    private void editUser() throws IOException {
        App.setRoot("editProfile");
    }
}

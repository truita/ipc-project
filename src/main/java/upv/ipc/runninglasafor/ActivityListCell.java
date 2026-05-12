package upv.ipc.runninglasafor;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import upv.ipc.runninglasafor.controllers.ActivityCellController;
import upv.ipc.sportlib.Activity;

public class ActivityListCell extends ListCell<Activity> {

    public ActivityListCell() {}

    @Override
    protected void updateItem(Activity item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(
                    App.class.getResource("activityCell.fxml")
                );
                Parent activityCell = loader.load();
                ActivityCellController controller = loader.getController();

                controller.name.setText(item.getName());
                controller.date.setText(
                    item
                        .getStartTime()
                        .format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))
                );
                controller.distance.setText(
                    Double.toString(item.getTotalDistance())
                );

                setGraphic(activityCell);
            } catch (IOException e) {
                throw new Error(e);
            }
        }
    }
}

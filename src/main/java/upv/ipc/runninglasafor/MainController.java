package upv.ipc.runninglasafor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane zoomPane;

    @FXML
    private VBox zoomButtons;

    private double zoomScale = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    private final double ZOOM_STEP = 0.2;

    @FXML
    private void zoomIn() {
        zoom(ZOOM_STEP);
    }

    @FXML
    private void zoomOut() {
        zoom(ZOOM_STEP * -1);
    }

    private void zoom(double increment) {
        zoomScale += increment;
        double zoom = Math.pow(2, zoomScale);

        double scrollH = scrollPane.getHvalue();
        double scrollV = scrollPane.getVvalue();

        zoomPane.setScaleX(zoom);
        zoomPane.setScaleY(zoom);

        scrollPane.setHvalue(scrollH);
        scrollPane.setVvalue(scrollV);
    }
}

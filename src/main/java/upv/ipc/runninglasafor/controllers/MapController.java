package upv.ipc.runninglasafor.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import upv.ipc.runninglasafor.App;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.SportActivityApp;

public class MapController implements Initializable {

    private MapProjection currentMapProjection;
    private Activity currentActivity;

    @FXML
    private ImageView imageView;

    @FXML
    private Polyline activityTrace;

    @FXML
    private Text noActivityText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MapRegion region = SportActivityApp.getInstance()
            .getMapRegions()
            .get(0);
        loadMapRegion(region);
    }

    private void loadMapRegion(MapRegion region) {
        Image mapImg = new Image(
            App.class.getResourceAsStream(region.getImagePath())
        );
        currentMapProjection = new MapProjection(
            region,
            mapImg.getWidth(),
            mapImg.getHeight()
        );
        imageView.setImage(mapImg);
    }

    // TODO: Change width of the polyline based on zoom
    public void setActivity(Activity activity) {
        activityTrace.setDisable(true);
        activityTrace.setVisible(false);
        ObservableList<Double> linePoints = activityTrace.getPoints();

        loadMapRegion(activity.getSuggestedMap());

        linePoints.clear();
        for (Point2D point : currentMapProjection.projectActivity(activity)) {
            linePoints.add(point.getX());
            linePoints.add(point.getY());
        }

        activityTrace.setDisable(false);
        activityTrace.setVisible(true);

        // Technically only needed for the first activity selected
        noActivityText.setVisible(false);
        noActivityText.setDisable(true);
        scrollPane.setDisable(false);
        scrollPane.setVisible(true);
        zoomButtons.setDisable(false);
        zoomButtons.setVisible(true);
    }

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane zoomPane;

    @FXML
    private VBox zoomButtons;

    private double zoomScale = 0;
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

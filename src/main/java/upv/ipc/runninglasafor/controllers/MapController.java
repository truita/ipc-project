package upv.ipc.runninglasafor.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import upv.ipc.runninglasafor.App;
import upv.ipc.sportlib.Activity;
import upv.ipc.sportlib.MapProjection;
import upv.ipc.sportlib.MapRegion;
import upv.ipc.sportlib.TrackPoint;

public class MapController implements Initializable {

    private MapProjection currentMapProjection;
    private Activity currentActivity;

    public BooleanProperty speedToggle = new SimpleBooleanProperty(false);

    @FXML
    private ImageView imageView;

    @FXML
    private Group activityTrace;

    @FXML
    private Text noActivityText;

    @FXML
    private AreaChart<Number, Number> elevationChart;

    @FXML
    private NumberAxis elevationChartXAxis;

    @FXML
    private NumberAxis elevationChartYAxis;

    private double traceWidth() {
        return TRACE_WIDTH / Math.pow(2, zoomScale);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        speedToggle.addListener((observable, oldVal, newVal) -> {
            drawTrace(currentActivity, newVal, traceWidth());
        });
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

    private final double TRACE_WIDTH = 3;
    private final Color TRACE_COLOR = new Color(0.0353, 0.0, 1.0, 1.0);

    private void drawTrace(Activity activity, boolean showSpeed, double width) {
        activityTrace.setDisable(true);
        activityTrace.setVisible(false);
        ObservableList<Node> drawnLines = activityTrace.getChildren();
        drawnLines.clear();

        List<TrackPoint> trackPoints = activity.getTrackPoints();

        List<Double> speeds = new ArrayList<>(trackPoints.size() - 1);
        double maxSpeed = 0;
        double minSpeed = Double.POSITIVE_INFINITY;
        for (int i = 1; i < trackPoints.size(); i++) {
            double speed = trackPoints.get(i - 1).speedTo(trackPoints.get(i));
            if (speed < minSpeed) minSpeed = speed;
            if (speed > maxSpeed) maxSpeed = speed;
            speeds.add(speed);
        }

        List<Point2D> projectedPoints = currentMapProjection.projectActivity(
            activity
        );
        for (int i = 1; i < projectedPoints.size(); i++) {
            // NOTE: this is technically inefficient (every point is computed twice)
            Point2D prevPoint = projectedPoints.get(i - 1);
            Point2D point = projectedPoints.get(i);

            Line line = new Line(
                prevPoint.getX(),
                prevPoint.getY(),
                point.getX(),
                point.getY()
            );
            line.setStrokeWidth(width);
            if (showSpeed) {
                double speed = speeds.get(i - 1);
                // A 0.0-1.0 value where 0 is minSpeed and 1 is maxSpeed
                double speedGradient =
                    (speed - minSpeed) / (maxSpeed - minSpeed);
                line.setStroke(new Color(speedGradient, 0.0, 0.0, 1.0));
            } else {
                line.setStroke(TRACE_COLOR);
            }
            drawnLines.add(line);
        }

        activityTrace.setDisable(false);
        activityTrace.setVisible(true);
    }

    private final double ALTITUDE_CHART_PADDING = 5;

    private void populateElevationChart(Activity activity) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        List<TrackPoint> trackPoints = activity.getTrackPoints();

        double currentMeter = 0;
        TrackPoint prevPoint = trackPoints.get(0);
        for (TrackPoint point : trackPoints) {
            currentMeter += prevPoint.distanceTo(point);
            series
                .getData()
                .add(
                    new XYChart.Data<>(
                        currentMeter / 1000,
                        point.getElevation()
                    )
                );
            prevPoint = point;
        }

        elevationChart.getData().clear();
        elevationChart.getData().add(series);

        elevationChartYAxis.setUpperBound(
            activity.getMaxElevation() + ALTITUDE_CHART_PADDING
        );
        elevationChartYAxis.setLowerBound(activity.getMinElevation());

        elevationChartXAxis.setUpperBound(currentMeter / 1000);
    }

    // TODO: center view/zoom on the activity
    public void setActivity(Activity activity) {
        currentActivity = activity;

        loadMapRegion(activity.getSuggestedMap());
        drawTrace(activity, false, traceWidth());
        populateElevationChart(activity);

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

        drawTrace(currentActivity, speedToggle.get(), traceWidth());
    }
}

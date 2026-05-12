package upv.ipc.runninglasafor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import upv.ipc.sportlib.*;

public class EditProfileController implements Initializable {

    @FXML
    private ImageView avatarImage;

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label birthdateLabel;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker birthdateDatePicker;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private Label emailError;

    @FXML
    private Label passwordError;

    @FXML
    private Label birthdateError;

    @FXML
    private Label missingFieldError;

    @FXML
    private Label avatarPathLabel;

    @FXML
    private Button browseAvatarButton;

    private String avatarPath;

    private Paint errorColor = Paint.valueOf("#d1242f");
    private Paint normalColor = Paint.valueOf("BLACK");

    private SportActivityApp app = SportActivityApp.getInstance();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Label[] errorLabels = {
            emailError,
            passwordError,
            birthdateError,
            missingFieldError,
        };

        for (Label errorLabel : errorLabels) {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);
        }

        avatarImage.managedProperty().bind(avatarImage.visibleProperty());

        User user = app.getCurrentUser();

        nicknameTextField.setText(user.getNickName());
        birthdateDatePicker.setValue(user.getBirthDate());
        emailTextField.setText(user.getEmail());
        if (user.getAvatar() != null) avatarImage.setImage(user.getAvatar());
        else avatarImage.setVisible(false);
    }

    @FXML
    private void browseAvatar() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select avatar image");
        fileChooser
            .getExtensionFilters()
            .addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg"),
                new ExtensionFilter("All Files", "*.*")
            );
        File avatarFile = fileChooser.showOpenDialog(
            avatarPathLabel.getScene().getWindow()
        );
        avatarPathLabel.setText(avatarFile.getName());
        avatarPath = avatarFile.getAbsolutePath();
        avatarImage.setImage(new Image("file:" + avatarPath));
        avatarImage.setVisible(true);
    }

    @FXML
    private void update() throws IOException {
        String email = emailTextField.getText();
        String password = passwordField.getText();
        LocalDate birthDate = birthdateDatePicker.getValue();
        if (email.isBlank()) emailLabel.setTextFill(errorColor);
        else emailLabel.setTextFill(normalColor);
        if (birthDate == null) birthdateLabel.setTextFill(errorColor);
        else birthdateLabel.setTextFill(normalColor);
        if (email.isBlank() || birthDate == null) {
            missingFieldError.setVisible(true);
            return;
        } else {
            missingFieldError.setVisible(false);
        }
        if (
            User.checkEmail(email) &&
            (User.checkPassword(password) || password.isBlank()) &&
            User.isOlderThan(birthDate, 12)
        ) {
            String pass = password.isBlank()
                ? app.getCurrentUser().getPassword()
                : password;
            String path =
                avatarPath == null
                    ? app.getCurrentUser().getAvatarPath()
                    : avatarPath;
            app.updateCurrentUser(email, pass, birthDate, path);
            App.setRoot("main");
        }
    }
}

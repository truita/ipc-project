package upv.ipc.runninglasafor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import upv.ipc.sportlib.*;

public class RegisterController implements Initializable {

    @FXML
    private Label emailLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label birthdateLabel;

    @FXML
    private Label nicknameLabel;

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
    private Label nicknameError;

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
            nicknameError,
            missingFieldError,
        };

        for (Label errorLabel : errorLabels) {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);
        }

        emailTextField
            .focusedProperty()
            .addListener((ob, oldV, newV) -> {
                if (newV) return;
                emailError.setVisible(
                    !User.checkEmail(emailTextField.getText()) &&
                        !emailTextField.getText().isBlank()
                );
            });

        emailTextField
            .textProperty()
            .addListener((ob, oldV, newV) -> {
                if (User.checkEmail(newV)) emailError.setVisible(false);
            });
        passwordField
            .textProperty()
            .addListener((ob, oldV, newV) -> {
                if (User.checkPassword(newV)) passwordError.setVisible(false);
            });
        birthdateDatePicker
            .valueProperty()
            .addListener((ob, oldV, newV) -> {
                if (User.isOlderThan(newV, 12)) birthdateError.setVisible(
                    false
                );
            });
        nicknameTextField
            .textProperty()
            .addListener((ob, oldV, newV) -> {
                if (User.checkNickName(newV)) nicknameError.setVisible(false);
            });

        passwordField
            .focusedProperty()
            .addListener((ob, oldV, newV) -> {
                if (newV) return;
                passwordError.setVisible(
                    !User.checkPassword(passwordField.getText()) &&
                        !passwordField.getText().isBlank()
                );
            });

        nicknameTextField
            .focusedProperty()
            .addListener((ob, oldV, newV) -> {
                if (newV) return;
                nicknameError.setVisible(
                    !User.checkNickName(nicknameTextField.getText()) &&
                        !nicknameTextField.getText().isBlank()
                );
            });

        birthdateDatePicker
            .focusedProperty()
            .addListener((ob, oldV, newV) -> {
                if (newV) return;
                birthdateError.setVisible(
                    !User.isOlderThan(birthdateDatePicker.getValue(), 12) &&
                        birthdateDatePicker.getValue() != null
                );
            });
    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void browseAvatar() throws IOException {
        if (avatarPath == null) {
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
            browseAvatarButton.setText("Delete");
        } else {
            avatarPathLabel.setText("No file selected");
            avatarPath = null;
            browseAvatarButton.setText("Browse");
        }
    }

    @FXML
    private void register() {
        String email = emailTextField.getText();
        String password = passwordField.getText();
        LocalDate birthDate = birthdateDatePicker.getValue();
        String nickname = nicknameTextField.getText();
        if (
            email.isBlank() ||
            password.isBlank() ||
            birthDate == null ||
            nickname.isBlank()
        ) {
            missingFieldError.setVisible(true);
            if (email.isBlank()) emailLabel.setTextFill(errorColor);
            else emailLabel.setTextFill(normalColor);
            if (password.isBlank()) passwordLabel.setTextFill(errorColor);
            else passwordLabel.setTextFill(normalColor);
            if (birthDate == null) birthdateLabel.setTextFill(errorColor);
            else birthdateLabel.setTextFill(normalColor);
            if (nickname.isBlank()) nicknameLabel.setTextFill(errorColor);
            else nicknameLabel.setTextFill(normalColor);
            return;
        } else {
            missingFieldError.setVisible(false);
        }
        if (
            User.checkEmail(email) &&
            User.checkPassword(password) &&
            User.isOlderThan(birthDate, 12) &&
            User.checkNickName(nickname)
        ) {
            app.registerUser(nickname, email, password, birthDate, avatarPath);
            System.out.println("User succesfully registered!");
        }
    }
}

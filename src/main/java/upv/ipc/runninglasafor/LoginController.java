package upv.ipc.runninglasafor;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import upv.ipc.sportlib.*;

public class LoginController implements Initializable {

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private TextField nicknameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label incorrectLoginError;

    @FXML
    private Label missingFieldError;

    private Paint errorColor = Paint.valueOf("#d1242f");
    private Paint normalColor = Paint.valueOf("BLACK");

    private SportActivityApp app = SportActivityApp.getInstance();

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        Label[] errorLabels = { incorrectLoginError, missingFieldError };

        for (Label errorLabel : errorLabels) {
            errorLabel.managedProperty().bind(errorLabel.visibleProperty());
            errorLabel.setVisible(false);
        }
    }

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void login() throws IOException {
        incorrectLoginError.setVisible(false);
        String nickname = nicknameTextField.getText();
        String password = passwordField.getText();
        if (nickname.isBlank()) nicknameLabel.setTextFill(errorColor);
        else nicknameLabel.setTextFill(normalColor);
        if (password.isBlank()) passwordLabel.setTextFill(errorColor);
        else passwordLabel.setTextFill(normalColor);
        if (nickname.isBlank() || password.isBlank()) {
            missingFieldError.setVisible(true);
            return;
        } else {
            missingFieldError.setVisible(false);
        }
        boolean loginSuccess = app.login(nickname, password);
        if (loginSuccess) App.setRoot("main");
        else incorrectLoginError.setVisible(true);
    }
}

package org.but.feec.cinema.controller;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.cinema.App;
import org.but.feec.cinema.data.PersonRepository;
import org.but.feec.cinema.exceptions.DataAccessException;
import org.but.feec.cinema.exceptions.ExceptionHandler;
import org.but.feec.cinema.exceptions.ResourceNotFoundException;
import org.but.feec.cinema.services.AuthService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    public Label usernameLabel;
    @FXML
    public Label passwordLabel;

    @FXML
    private Button loginBtn;
    @FXML
    private TextField usernameTxtField;
    @FXML
    private PasswordField passwordTxtField;

    private PersonRepository personRepository;
    private AuthService authService;

    private ValidationSupport validation;

    public LoginController() {
    }

    @FXML
    private void initialize() {
        usernameTxtField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSignIn();
            }
        });
        passwordTxtField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleSignIn();
            }
        });

        initializeLogos();
        initializeServices();
        initializeValidations();

        logger.info("LoginController initialized");
    }

    private void initializeValidations() {
        validation = new ValidationSupport();
        validation.registerValidator(usernameTxtField, Validator.createEmptyValidator("The username must not be empty."));
        validation.registerValidator(passwordTxtField, Validator.createEmptyValidator("The password must not be empty."));
        loginBtn.disableProperty().bind(validation.invalidProperty());
    }

    private void initializeServices() {
        personRepository = new PersonRepository();
        authService = new AuthService(personRepository);
    }

    private void initializeLogos() {
        Image vutImage = new Image(App.class.getResourceAsStream("logos/loginLogo.jpg"));
        ImageView vutLogoImage = new ImageView(vutImage);
        vutLogoImage.setFitHeight(85);
        vutLogoImage.setFitWidth(150);

    }

    public void signInActionHandler(ActionEvent event) {
        handleSignIn();
    }

    private void handleSignIn() {
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();

        try {
            boolean authenticated = authService.authenticate(username, password);
            if (authenticated) {
                showPersonsView();
            } else {
                showInvalidPasswordDialog();
            }
        } catch (ResourceNotFoundException | DataAccessException e) {
            showInvalidPasswordDialog();
        }
    }

    private void showPersonsView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/Persons.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
            Stage stage = new Stage();
            stage.setTitle("BDS cinema");
            stage.setScene(scene);

            Stage stageOld = (Stage) loginBtn.getScene().getWindow();
            stageOld.close();

            stage.getIcons().add(new Image(App.class.getResourceAsStream("logos/projectionLogo.jpg")));
            authConfirmDialog();

            stage.show();
        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    private void showInvalidPasswordDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unauthenticated");
        alert.setHeaderText("The user is not authenticated");
        alert.setContentText("Please provide a valid username and password");//www.java2s.com

        alert.showAndWait();
    }


    private void authConfirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logging confirmation");
        alert.setHeaderText("You were successfully logged in.");

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.out.println("ok clicked");
        } else if (result.get() == ButtonType.CANCEL) {
            System.out.println("cancel clicked");
        }
    }

    public void handleOnEnterActionPassword(ActionEvent dragEvent) {
        handleSignIn();
    }
}

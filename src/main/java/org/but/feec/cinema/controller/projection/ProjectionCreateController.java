package org.but.feec.cinema.controller.projection;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.but.feec.cinema.api.projection.ProjectionCreateView;
import org.but.feec.cinema.data.ProjectionRepository;
import org.but.feec.cinema.services.ProjectionService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Optional;

public class ProjectionCreateController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionCreateController.class);


    @FXML
    private Button createProjectionBtn;
    @FXML
    private TextField newUserIdTxtField;
    @FXML
    private TextField newProjectionTxtField;
    @FXML
    private TextField newStatusTxtField;
    @FXML
    private TextField newReservationTxtField;

    private ProjectionService projectionService;
    private ProjectionRepository projectionRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        projectionRepository = new ProjectionRepository();
        projectionService = new ProjectionService(projectionRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newUserIdTxtField, Validator.createEmptyValidator("The user id must not be empty."));
        validation.registerValidator(newProjectionTxtField, Validator.createEmptyValidator("The projection id must not be empty."));
        validation.registerValidator(newStatusTxtField, Validator.createEmptyValidator("Status field must not be empty."));
        validation.registerValidator(newReservationTxtField, Validator.createEmptyValidator("The Reservation date must not be empty."));

        createProjectionBtn.disableProperty().bind(validation.invalidProperty());

        logger.info("ProjectionCreateController initialized");
    }

    @FXML
    void handleCreateNewProjection(ActionEvent event) throws ParseException {

        Long userId = Long.valueOf(newUserIdTxtField.getText());
        Long projetionId = Long.valueOf(newProjectionTxtField.getText());
        Boolean status = Boolean.valueOf(newStatusTxtField.getText());
        Timestamp ReservationDate = Timestamp.valueOf(newReservationTxtField.getText());

        ProjectionCreateView projectionCreateView = new ProjectionCreateView();

        projectionCreateView.setUserId(userId);
        projectionCreateView.setProjectionId(projetionId);
        projectionCreateView.setStatus(status);
        projectionCreateView.setReservation_date(ReservationDate);


        projectionService.createProjection(projectionCreateView);

        projectionCreatedConfirmationDialog();
    }

    private void projectionCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Projection creation confirmed");
        alert.setHeaderText("Your projection was successfully created.");

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
    }



}

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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.cinema.api.projection.ProjectionBasicView;
import org.but.feec.cinema.api.projection.ProjectionEditView;
import org.but.feec.cinema.data.ProjectionRepository;
import org.but.feec.cinema.services.ProjectionService;

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Timestamp;
import java.util.Optional;

public class ProjectionEditController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionEditController.class);

    @FXML
    private Button editProjectionBtn;
    @FXML
    private TextField idTxtField;
    @FXML
    private TextField userTxtField;
    @FXML
    private TextField projectionTxtField;
    @FXML
    private TextField statusTxtField;
    @FXML
    private TextField reservationTxtField;


    private ProjectionService projectionService;
    private ProjectionRepository projectionRepository;
    private ValidationSupport validation;
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        projectionRepository = new ProjectionRepository();
        projectionService = new ProjectionService(projectionRepository);

        validation = new ValidationSupport();
        validation.registerValidator(idTxtField, Validator.createEmptyValidator("The id must not be empty."));
        idTxtField.setEditable(false);
        validation.registerValidator(userTxtField, Validator.createEmptyValidator("The user id must not be empty."));
        validation.registerValidator(projectionTxtField, Validator.createEmptyValidator("The projection id must not be empty."));
        validation.registerValidator(statusTxtField, Validator.createEmptyValidator("Status field  must not be empty."));
        validation.registerValidator(reservationTxtField, Validator.createEmptyValidator("The Reservation date must not be empty."));

        editProjectionBtn.disableProperty().bind(validation.invalidProperty());

        loadProjectionsData();

        logger.info("ProjectionsEditController initialized");
    }

    private void loadProjectionsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof ProjectionBasicView) {
            ProjectionBasicView projectionBasicView = (ProjectionBasicView) stage.getUserData();
            idTxtField.setText(String.valueOf(projectionBasicView.getId()));
            userTxtField.setText(String.valueOf(projectionBasicView.getUserId()));
            projectionTxtField.setText(String.valueOf(projectionBasicView.getProjectionId()));
            statusTxtField.setText(String.valueOf(projectionBasicView.isStatus()));
            reservationTxtField.setText(String.valueOf(projectionBasicView.getReservation_date()));
        }
    }

    @FXML
    public void handleEditProjectionButton(ActionEvent event) {
        Long id = Long.valueOf(idTxtField.getText());

        Long userId = Long.valueOf(userTxtField.getText());
        Long projectionId = Long.valueOf(projectionTxtField.getText());
        Boolean status = Boolean.valueOf(statusTxtField.getText());
        Timestamp ReservationDate = Timestamp.valueOf(reservationTxtField.getText());


        ProjectionEditView projectionEditView = new ProjectionEditView();

        projectionEditView.setId(id);
        projectionEditView.setUserId(userId);
        projectionEditView.setProjectionId(projectionId);
        projectionEditView.setStatus(status);
        projectionEditView.setReservation_date(ReservationDate);

        projectionService.editProjection(projectionEditView);

        projectionEditedConfirmationDialog();
    }

    private void projectionEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Projection Edited - Confirmation");
        alert.setHeaderText("Projection was successfully edited.");

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

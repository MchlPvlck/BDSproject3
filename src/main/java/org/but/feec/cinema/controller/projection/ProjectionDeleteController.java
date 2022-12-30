package org.but.feec.cinema.controller.projection;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.but.feec.cinema.api.projection.ProjectionDeleteView;
import org.but.feec.cinema.data.ProjectionRepository;
import org.but.feec.cinema.services.ProjectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ProjectionDeleteController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionDeleteController.class);
    private ProjectionService projectionService;
    private ProjectionRepository projectionRepository;

    public void initialize(Long id) {

        projectionRepository = new ProjectionRepository();
        projectionService = new ProjectionService(projectionRepository);

        ProjectionDeleteView projectionDeleteView = new ProjectionDeleteView();
        projectionDeleteView.setId(id);

        projectionService.deleteProjection(projectionDeleteView);

        projectionDeletedConfirmationDialog();
        logger.info("ProjectionsDeleteController initialized");
    }


    private void projectionDeletedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("User Deleted Confirmation");
        alert.setHeaderText("Your user was successfully deleted.");

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

package org.but.feec.cinema.controller.projection;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.cinema.api.projection.ProjectionDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ProjectionDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionDetailViewController.class);

    @FXML
    private TextField idTxtField;
    @FXML
    private TextField givenTxtField;
    @FXML
    private TextField familyTxtField;
    @FXML
    private TextField movieNameTxtField;
    @FXML
    private TextField roomTxtField;
    @FXML
    private TextField seatTxtField;
    @FXML
    private TextField durationTxtField;
    @FXML
    private TextField directorTxtField;

    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTxtField.setEditable(false);
        givenTxtField.setEditable(false);
        familyTxtField.setEditable(false);
        movieNameTxtField.setEditable(false);
        roomTxtField.setEditable(false);
        seatTxtField.setEditable(false);
        durationTxtField.setEditable(false);
        directorTxtField.setEditable(false);
        loadProjectionsData();

        logger.info("ProjectionDetailViewController initialized");
    }

    private void loadProjectionsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof ProjectionDetailView) {
            ProjectionDetailView projectionBasicView = (ProjectionDetailView) stage.getUserData();
            idTxtField.setText(String.valueOf(projectionBasicView.getProjectionId()));
            givenTxtField.setText(projectionBasicView.getGivenName());
            familyTxtField.setText(projectionBasicView.getFamilyName());
            movieNameTxtField.setText(projectionBasicView.getMovieName());
            roomTxtField.setText(projectionBasicView.getRoomName());
            seatTxtField.setText(String.valueOf(projectionBasicView.getSeat()));
            durationTxtField.setText(String.valueOf(projectionBasicView.getDuration()));
            directorTxtField.setText(projectionBasicView.getDirectorName());
        }
    }
}

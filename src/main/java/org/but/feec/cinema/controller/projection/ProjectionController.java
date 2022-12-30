package org.but.feec.cinema.controller.projection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.but.feec.cinema.App;
import org.but.feec.cinema.api.projection.ProjectionBasicView;
import org.but.feec.cinema.api.projection.ProjectionDetailView;
import org.but.feec.cinema.data.ProjectionRepository;
import org.but.feec.cinema.exceptions.ExceptionHandler;
import org.but.feec.cinema.services.ProjectionService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class ProjectionController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectionController.class);

    @FXML
    public Button addProjectionBtn;
    @FXML
    public Button refreshBtn;
    @FXML
    public Button findBtn;

    @FXML
    private TableColumn<ProjectionBasicView, Long> reservationId;
    @FXML
    private TableColumn<ProjectionBasicView, String> projectionGivenName;
    @FXML
    private TableColumn<ProjectionBasicView, String> projectionFamilyName;
    @FXML
    private TableColumn<ProjectionBasicView, String> projectionMovieName;
    @FXML
    private TableColumn<ProjectionBasicView, String> projectionStart;
    @FXML
    private TableView<ProjectionBasicView> projectionTableView;
    @FXML
    private TextField findTxtField;
    @FXML
    private ChoiceBox<String> choiceBox;



    private String[] columns = {"Id" ,"Given name", "Family name", "Movie name"};
    private ProjectionService projectionService;
    private ProjectionRepository projectionRepository;
    private ValidationSupport validation;

    public ProjectionController() {
    }

    @FXML
    private void initialize() {
        projectionRepository = new ProjectionRepository();
        projectionService = new ProjectionService(projectionRepository);
        validation = new ValidationSupport();

        reservationId.setCellValueFactory(new PropertyValueFactory<ProjectionBasicView, Long >("id"));
        projectionGivenName.setCellValueFactory(new PropertyValueFactory<ProjectionBasicView, String>("givenName"));
        projectionFamilyName.setCellValueFactory(new PropertyValueFactory<ProjectionBasicView, String>("familyName"));
        projectionMovieName.setCellValueFactory(new PropertyValueFactory<ProjectionBasicView, String>("movieName"));
        projectionStart.setCellValueFactory(new PropertyValueFactory<ProjectionBasicView, String>("projectionStart"));

        validation.registerValidator(findTxtField, Validator.createEmptyValidator("The value must not be empty."));

        findBtn.disableProperty().bind(validation.invalidProperty());


        ObservableList<ProjectionBasicView> observableProjectionsList = initializeProjectionsData();
        projectionTableView.setItems(observableProjectionsList);

        projectionTableView.getSortOrder().add(reservationId);

        initializeTableViewSelection();
        initializeChoiceBox();

        logger.info("ProjectionsController initialized");
    }

    private void initializeChoiceBox(){
        choiceBox.getItems().addAll(columns);
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit reservation");
        MenuItem delete = new MenuItem("Delete reservation");
        MenuItem detailedView = new MenuItem("Detailed reservation view");
        edit.setOnAction((ActionEvent event) -> {
            ProjectionBasicView projectionView = projectionTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/ProjectionEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(projectionView);
                stage.setTitle("Cinema edit reservation");

                ProjectionEditController controller = new ProjectionEditController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 700, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        delete.setOnAction((ActionEvent event) -> {
            ProjectionBasicView projectionView = projectionTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();

                ProjectionDeleteController controller = new ProjectionDeleteController();
                controller.initialize(projectionView.getId());
                fxmlLoader.setController(controller);

                ObservableList<ProjectionBasicView> observableProjectionsList = initializeProjectionsData();
                projectionTableView.setItems(observableProjectionsList);
                projectionTableView.refresh();
                projectionTableView.sort();

            } catch (Exception ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            ProjectionBasicView projectionView = projectionTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/ProjectionDetailView.fxml"));
                Stage stage = new Stage();

                Long projectionId = projectionView.getId();
                ProjectionDetailView projectionDetailView = projectionService.getProjectionDetailView(projectionId);

                stage.setUserData(projectionDetailView);
                stage.setTitle("Cinema reservation detail view");

                ProjectionDetailViewController controller = new ProjectionDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 700, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });


        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().add(delete);
        menu.getItems().addAll(detailedView);
        projectionTableView.setContextMenu(menu);
    }

    private ObservableList<ProjectionBasicView> initializeProjectionsData() {
        List<ProjectionBasicView> projections = projectionService.getProjectionBasicView();
        return FXCollections.observableArrayList(projections);
    }

    private ObservableList<ProjectionBasicView> initializeProjectionsFindData(String find, String choice) {
        List<ProjectionBasicView> projections = projectionService.getProjectionFindView(find, choice);
        return FXCollections.observableArrayList(projections);
    }



    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddProjectionButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/ProjectionCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 700, 500);
            Stage stage = new Stage();
            stage.setTitle("Cinema create reservation");
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            ExceptionHandler.handleException(e);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<ProjectionBasicView> observableProjectionsList = initializeProjectionsData();
        projectionTableView.setItems(observableProjectionsList);
        projectionTableView.refresh();
        projectionTableView.sort();
    }

    public void handleFindButton(ActionEvent actionEvent) throws IOException{
        String find = findTxtField.getText();

        ProjectionBasicView projectionBasicView = new ProjectionBasicView();
        projectionBasicView.setFind(find);
        projectionBasicView.setChoice(choiceBox.getValue());

        String choice = projectionBasicView.getChoice();
        String value = projectionBasicView.getFind();


        ObservableList<ProjectionBasicView> observableProjectionsList = initializeProjectionsFindData(value, choice);
        projectionTableView.setItems(observableProjectionsList);
        projectionTableView.refresh();
        projectionTableView.sort();
    }

    public void handleUsersButton(ActionEvent actionEvent) throws IOException{
        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Persons.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleDummyButton(ActionEvent actionEvent) throws IOException{
        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/DummyTable.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

}

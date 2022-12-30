package org.but.feec.cinema.controller.dummy;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.cinema.App;
import org.but.feec.cinema.api.dummy.DummyBasicView;
import org.but.feec.cinema.api.person.PersonBasicView;
import org.but.feec.cinema.data.DummyRepository;
import org.but.feec.cinema.exceptions.ExceptionHandler;
import org.but.feec.cinema.services.DummyService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DummyController {

    private static final Logger logger = LoggerFactory.getLogger(DummyController.class);

    @FXML
    private TableView<DummyBasicView> dummyTable;
    @FXML
    private TableColumn<DummyBasicView, String> stringValue;
    @FXML
    private TextField stringAddTxtField;
    @FXML
    private Button projectionBtn;
    @FXML
    private Button userBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button addStringBtn;


    private DummyService dummyService;
    private DummyRepository dummyRepository;
    private ValidationSupport validation;

    @FXML
    private void initialize() {
        dummyRepository = new DummyRepository();
        dummyService = new DummyService(dummyRepository);

        stringValue.setCellValueFactory(new PropertyValueFactory<DummyBasicView, String>("string"));

        ObservableList<DummyBasicView> observableDummyList = initializeDummyData();
        dummyTable.setItems(observableDummyList);

        dummyTable.getSortOrder().add(stringValue);

        //loadIcons();

        logger.info("DummyController initialized");
    }

    private ObservableList<DummyBasicView> initializeDummyData() {
        List<DummyBasicView> dummy = dummyService.getDummyBasicView();
        return FXCollections.observableArrayList(dummy);
    }



   /* private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logo/vut-logo-eng.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }*/

    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddStringButton(ActionEvent actionEvent) {
        try {

            ValidationSupport validation;
            validation = new ValidationSupport();
            validation.registerValidator(stringAddTxtField, Validator.createEmptyValidator("String must not be empty."));

            addStringBtn.disableProperty().bind(validation.invalidProperty());

            String string = stringAddTxtField.getText();

            DummyBasicView dummyBasicView = new DummyBasicView();
            dummyBasicView.setString(string);

            dummyService.createString(dummyBasicView);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("String Creation Confirmation");
            alert.setHeaderText("Your string was successfully created.");

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


            dummyTable.refresh();

        } catch (Exception ex) {
            ExceptionHandler.handleException(ex);
        }
    }



    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<DummyBasicView> observableDummyList = initializeDummyData();
        dummyTable.setItems(observableDummyList);
        dummyTable.refresh();
        dummyTable.sort();
    }



    public void handleUsersButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Persons.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleProjectionButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Projection.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }
}

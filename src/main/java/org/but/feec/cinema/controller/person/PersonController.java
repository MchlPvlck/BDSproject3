package org.but.feec.cinema.controller.person;

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
import javafx.stage.Stage;
import org.but.feec.cinema.App;
import org.but.feec.cinema.api.person.PersonBasicView;
import org.but.feec.cinema.data.PersonRepository;
import org.but.feec.cinema.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class PersonController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @FXML
    private TableColumn<PersonBasicView, Long> userId;
    @FXML
    private TableColumn<PersonBasicView, String> userGivenName;
    @FXML
    private TableColumn<PersonBasicView, String> userFamilyName;
    @FXML
    private TableColumn<PersonBasicView, String> userEmail;
    @FXML
    private TableColumn<PersonBasicView, String> userPhoneNumber;
    @FXML
    private TableColumn<PersonBasicView, String> userName;
    @FXML
    private TableView<PersonBasicView> userTableView;




    private PersonRepository personRepository;


    private PersonService personService;
    @FXML
    private Button projectionBtn;
    @FXML
    private Button dummyBtn;

    public PersonController() {
    }

    @FXML
    private void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);


        userId.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("id"));
        userGivenName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("givenName"));
        userFamilyName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("familyName"));
        userEmail.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("email"));
        userPhoneNumber.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("phoneNumber"));
        userName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("username"));

        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        userTableView.setItems(observablePersonsList);

        userTableView.getSortOrder().add(userId);

        logger.info("PersonsController initialized");
    }



    private ObservableList<PersonBasicView> initializePersonsData() {
        List<PersonBasicView> persons = personService.getPersonsBasicView();
        return FXCollections.observableArrayList(persons);
    }


    public void handleDummyButton(ActionEvent actionEvent) throws IOException {

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/DummyTable.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(tableViewScene);
        window.show();
    }

    public void handleProjectionButton(ActionEvent actionEvent) throws IOException{

        Parent tableViewParent = FXMLLoader.load(App.class.getResource("fxml/Projection.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();;

        window.setScene(tableViewScene);
        window.show();
    }








}

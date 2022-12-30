package org.but.feec.cinema.api.projection;

import javafx.beans.property.*;

public class ProjectionBasicView {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty movieName = new SimpleStringProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty projectionStart = new SimpleStringProperty();

    private LongProperty userId = new SimpleLongProperty();
    private LongProperty projectionId =  new SimpleLongProperty();
    private BooleanProperty status = new SimpleBooleanProperty();
    private StringProperty reservation_date = new SimpleStringProperty();


    public long getUserId() {
        return userIdProperty().get();
    }

    public LongProperty userIdProperty() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userIdProperty().set(userId);
    }

    public long getProjectionId() {
        return projectionIdProperty().get();
    }

    public LongProperty projectionIdProperty() {
        return projectionId;
    }

    public void setProjectionId(long projectionId) {
        this.projectionIdProperty().set(projectionId);
    }

    public boolean isStatus() {
        return statusProperty().get();
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    public void setStatus(boolean status) {
        this.statusProperty().set(status);
    }

    public String getReservation_date() {
        return reservation_dateProperty().get();
    }

    public StringProperty reservation_dateProperty() {
        return reservation_date;
    }

    public void setReservation_date(String reservation_date) {
        this.reservation_dateProperty().set(reservation_date);
    }

    private String find;

    public String getProjectionStart() {
        return projectionStartProperty().get();
    }

    public StringProperty projectionStartProperty() {
        return projectionStart;
    }

    public void setProjectionStart(String projectionStart) {
        this.projectionStartProperty().set(projectionStart);
    }

    private String choice;


    public Long getId() {
        return idProperty().get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(Long id) {
        this.idProperty().set(id);
    }

    public String getMovieName() {
        return movieNameProperty().get();
    }

    public StringProperty movieNameProperty() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieNameProperty().set(movieName);
    }

    public String getGivenName() {
        return givenNameProperty().get();
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenNameProperty().set(givenName);
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyNameProperty().set(familyName);
    }

    public String getFind() {
        return find;
    }

    public void setFind(String find) {
        this.find = find;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }


}

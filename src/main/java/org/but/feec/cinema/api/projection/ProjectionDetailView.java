package org.but.feec.cinema.api.projection;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProjectionDetailView {
    private LongProperty projectionId = new SimpleLongProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty movieName = new SimpleStringProperty();
    private StringProperty roomName = new SimpleStringProperty();
    private LongProperty seat = new SimpleLongProperty();
    private LongProperty duration = new SimpleLongProperty();
    private StringProperty directorName = new SimpleStringProperty();

    public long getProjectionId() {
        return projectionIdProperty().get();
    }

    public LongProperty projectionIdProperty() {
        return projectionId;
    }

    public void setProjectionId(long projectionId) {
        this.projectionIdProperty().set(projectionId);
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

    public String getMovieName() {
        return movieNameProperty().get();
    }

    public StringProperty movieNameProperty() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieNameProperty().set(movieName);
    }

    public String getRoomName() {
        return roomNameProperty().get();
    }

    public StringProperty roomNameProperty() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomNameProperty().set(roomName);
    }

    public long getSeat() {
        return seatProperty().get();
    }

    public LongProperty seatProperty() {
        return seat;
    }

    public void setSeat(long seat) {
        this.seatProperty().set(seat);
    }

    public long getDuration() {
        return durationProperty().get();
    }

    public LongProperty durationProperty() {
        return duration;
    }

    public void setDuration(long duration) {
        this.durationProperty().set(duration);
    }

    public String getDirectorName() {
        return directorNameProperty().get();
    }

    public StringProperty directorNameProperty() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorNameProperty().set(directorName);
    }

}

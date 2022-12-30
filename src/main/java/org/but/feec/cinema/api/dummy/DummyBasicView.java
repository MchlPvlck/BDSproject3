package org.but.feec.cinema.api.dummy;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DummyBasicView {
    private StringProperty string = new SimpleStringProperty();

    public String getString() {
        return stringProperty().get();
    }

    public StringProperty stringProperty() {
        return string;
    }

    public void setString(String string) {
        this.stringProperty().set(string);
    }
}

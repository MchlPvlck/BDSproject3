package org.but.feec.cinema.data;



import org.but.feec.cinema.api.person.PersonAuthView;
import org.but.feec.cinema.api.person.PersonBasicView;
import org.but.feec.cinema.config.DataSourceConfig;
import org.but.feec.cinema.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public PersonAuthView findPersonByUsername(String username) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT user_name, user_password" +
                             " FROM bds.login l" +
                             " WHERE l.user_name = ? ")
        ) {
            preparedStatement.setString(1, username);


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by username failed.", e);
        }
        return null;
    }

    public List<PersonBasicView> getPersonsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT u.user_id, u.given_name, u.family_name, u.email, u.phone_number, l.user_name" +
                             " FROM bds.user u" +
                             " JOIN bds.login l ON u.user_id = l.user_id");
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }


    private PersonAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        PersonAuthView person = new PersonAuthView();
        person.setUsername(rs.getString("user_name"));
        person.setPassword(rs.getString("user_password"));
        return person;
    }

    private PersonBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        PersonBasicView personBasicView = new PersonBasicView();
        personBasicView.setId(rs.getLong("user_id"));
        personBasicView.setEmail(rs.getString("email"));
        personBasicView.setGivenName(rs.getString("given_name"));
        personBasicView.setFamilyName(rs.getString("family_name"));
        personBasicView.setUsername(rs.getString("user_name"));
        personBasicView.setPhoneNumber(rs.getString("phone_number"));
        return personBasicView;
    }



}

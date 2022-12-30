package org.but.feec.cinema.data;

import org.but.feec.cinema.api.dummy.DummyBasicView;
import org.but.feec.cinema.api.projection.*;
import org.but.feec.cinema.config.DataSourceConfig;
import org.but.feec.cinema.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectionRepository {

    public ProjectionDetailView findProjectionDetailView(Long reservationId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT r.reservation_id, u.given_name, u.family_name, m.movie_name,\n" +
                                "o.room_name, s.seat_id, m.duration, d.director_family_name \n" +
                                "FROM bds.user u\n" +
                                "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                                "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                                "JOIN bds.room o ON p.room_id = o.room_id\n" +
                                "JOIN bds.movie m ON p.movie_id = m.movie_id\n" +
                                "LEFT JOIN bds.reserved_seat s ON r.reservation_id = s.reservation_id\n" +
                                "JOIN bds.director d ON m.director_id = d.director_id\n" +
                                "WHERE r.reservation_id = ?\n");
        ) {
            preparedStatement.setLong(1, reservationId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToProjectionDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find projection by id failed.", e);
        }
        return null;
    }

    public List<ProjectionBasicView> getProjectionBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                             " m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                             "FROM bds.user u\n" +
                             "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                             "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                             "JOIN bds.movie m ON p.movie_id = m.movie_id");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<ProjectionBasicView> projectionBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                projectionBasicViews.add(mapToProjectionBasicView(resultSet));
            }
            return projectionBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Projection basic view couldn't be loaded.", e);
        }
    }

    public void createProjection(ProjectionCreateView projectionCreateView) {
        String insertProjectionSQL = "INSERT INTO bds.reservation (reservation_id, user_id, projection_id, is_active, reservation_date) VALUES (DEFAULT,?,?,?,?);";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProjectionSQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, projectionCreateView.getUserId());
            preparedStatement.setLong(2, projectionCreateView.getProjectionId());
            preparedStatement.setBoolean(3, projectionCreateView.getStatus());
            preparedStatement.setTimestamp(4, projectionCreateView.getReservation_date());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating projection failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating projection operation failed.");
        }
    }

    public void deleteProjection(ProjectionDeleteView projectionDeleteView){
        String deleteProjectionSQL = "DELETE FROM bds.reservation r WHERE r.reservation_id = ?";
        String checkIfExists = "SELECT reservation_id FROM bds.reservation r WHERE r.reservation_id = ? ORDER BY r.reservation_id";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteProjectionSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, projectionDeleteView.getId());

            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, projectionDeleteView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This projection to delete doesn't exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Deleting projection failed, no rows affected.");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Deleting projection operation failed.");
        }
    }

    public void editProjection(ProjectionEditView projectionEditView) {
        String insertProjectionSQL = "UPDATE bds.reservation SET user_id = ?, projection_id = ?, is_active = ?, reservation_date = ? WHERE reservation_id = ?";
        String checkIfExists = "SELECT r.reservation_id FROM bds.reservation r WHERE r.reservation_id = ? ORDER BY r.reservation_id";
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProjectionSQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, projectionEditView.getUserId());
            preparedStatement.setLong(2, projectionEditView.getProjectionId());
            preparedStatement.setBoolean(3, projectionEditView.getStatus());
            preparedStatement.setTimestamp(4, projectionEditView.getReservation_date());
            preparedStatement.setLong(5, projectionEditView.getId());

            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, projectionEditView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This projection for edit do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating projection failed, no rows affected.");
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating projection operation failed.");
        }
    }

    public List<ProjectionBasicView> getProjectionFindView(String find, String choice) {
        try (Connection connection = DataSourceConfig.getConnection()){
            PreparedStatement preparedStatement;
            ResultSet resultSet;
            if(choice.equals("Id")){
                String selectQueryId = "SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                                " m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                                "FROM bds.user u\n" +
                                "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                                "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                                "JOIN bds.movie m ON p.movie_id = m.movie_id\n" +
                                "WHERE r.reservation_id = " + find +
                                "ORDER BY r.reservation_id"  ;
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(selectQueryId);
            }
            else if(choice.equals("Given name")){
                preparedStatement = connection.prepareStatement(
                        "SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                                " m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                                "FROM bds.user u\n" +
                                "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                                "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                                "JOIN bds.movie m ON p.movie_id = m.movie_id\n" +
                                "WHERE u.given_name\n" +
                                "LIKE ?\n" +
                                "ORDER BY u.given_name\n");
                preparedStatement.setString(1, "%" + find + "%");
                resultSet = preparedStatement.executeQuery();
            }
            else if(choice.equals("Family name")){
                preparedStatement = connection.prepareStatement(
                        "SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                                " m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                                "FROM bds.user u\n" +
                                "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                                "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                                "JOIN bds.movie m ON p.movie_id = m.movie_id\n" +
                                "WHERE u.family_name\n" +
                                "LIKE ?\n" +
                                "ORDER BY u.family_name\n");
                preparedStatement.setString(1, "%" + find + "%");
                resultSet = preparedStatement.executeQuery();
            }
            else if(choice.equals("Movie name")){
                preparedStatement = connection.prepareStatement("SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                        "m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                        "FROM bds.user u\n" +
                        "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                        "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                        "JOIN bds.movie m ON p.movie_id = m.movie_id\n" +
                        "WHERE m.movie_name\n" +
                        "LIKE ?\n" +
                        "ORDER BY m.movie_name;\n");
                preparedStatement.setString(1, "%" + find + "%");
                resultSet = preparedStatement.executeQuery();
            }
            else{
                preparedStatement = connection.prepareStatement(
                        "SELECT r.reservation_id, u.user_id, u.given_name, u.family_name,\n" +
                                " m.movie_name, p.projection_start, p.projection_id, r.is_active, r.reservation_date\n" +
                                "FROM bds.user u\n" +
                                "JOIN bds.reservation r ON u.user_id = r.user_id\n" +
                                "JOIN bds.projection p ON r.projection_id = p.projection_id\n" +
                                "JOIN bds.movie m ON p.movie_id = m.movie_id");
                resultSet = preparedStatement.executeQuery();
            }

            List<ProjectionBasicView> projectionBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                projectionBasicViews.add(mapToProjectionBasicView(resultSet));
            }
            return projectionBasicViews;

        } catch (SQLException e) {
            throw new DataAccessException("Finding projection has failed: ", e);
        }
    }

    private ProjectionBasicView mapToProjectionBasicView(ResultSet rs) throws SQLException {
        ProjectionBasicView projectionBasicView = new ProjectionBasicView();
        projectionBasicView.setId(rs.getLong("reservation_id"));
        projectionBasicView.setGivenName(rs.getString("given_name"));
        projectionBasicView.setFamilyName(rs.getString("family_name"));
        projectionBasicView.setMovieName(rs.getString("movie_name"));
        projectionBasicView.setProjectionStart(rs.getString("projection_start"));
        projectionBasicView.setUserId(rs.getLong("user_id"));
        projectionBasicView.setProjectionId(rs.getLong("projection_id"));
        projectionBasicView.setStatus(rs.getBoolean("is_active"));
        projectionBasicView.setReservation_date(rs.getString("reservation_date"));
        return projectionBasicView;
    }

    private ProjectionDetailView mapToProjectionDetailView(ResultSet rs) throws SQLException {
        ProjectionDetailView projectionDetailView = new ProjectionDetailView();
        projectionDetailView.setProjectionId(rs.getLong("reservation_id"));
        projectionDetailView.setGivenName(rs.getString("given_name"));
        projectionDetailView.setFamilyName(rs.getString("family_name"));
        projectionDetailView.setMovieName(rs.getString("movie_name"));
        projectionDetailView.setRoomName(rs.getString("room_name"));
        projectionDetailView.setSeat(rs.getLong("seat_id"));
        projectionDetailView.setDuration(rs.getLong("duration"));
        projectionDetailView.setDirectorName(rs.getString("director_family_name"));
        return projectionDetailView;
    }

}

package org.but.feec.cinema.data;

import org.but.feec.cinema.api.dummy.DummyBasicView;
import org.but.feec.cinema.config.DataSourceConfig;
import org.but.feec.cinema.exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DummyRepository {

    public List<DummyBasicView> getDummyBasicView() {
        String selectQuerySQL = "SELECT string FROM public.dummy_table ORDER BY string;";
        try (Connection connection = DataSourceConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuerySQL);) {
            List<DummyBasicView> dummyBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                dummyBasicViews.add(mapToDummyBasicView(resultSet));
            }
            return dummyBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Dummy basic view could not be loaded.", e);
        }
    }

    public void createString(DummyBasicView dummyBasicView) {
        String string = dummyBasicView.getString();
        String insertQuerySQL = "INSERT INTO public.dummy_table (string) VALUES('" + string + "');";

        try(Connection connection = DataSourceConfig.getConnection()){
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertQuerySQL);

        }catch (SQLException e){
            throw new DataAccessException("Strings create exception. ", e);
        }
    }

    private DummyBasicView mapToDummyBasicView(ResultSet rs) throws SQLException {
        DummyBasicView dummyBasicView = new DummyBasicView();
        dummyBasicView.setString(rs.getString("string"));
        return dummyBasicView;
    }

}

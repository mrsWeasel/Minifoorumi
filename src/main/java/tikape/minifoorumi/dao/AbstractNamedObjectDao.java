package tikape.minifoorumi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.domain.AbstractNamedObject;

public abstract class AbstractNamedObjectDao<T extends AbstractNamedObject>
        implements Dao<T, Integer> {

    protected Database database;
    protected String tableName;

    public AbstractNamedObjectDao(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    @Override
    public T findOne(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE id = ?");
            stmt.setInt(1, key);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return createFromRow(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error when looking for a row in " + tableName + " with id " + key);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<T> findAll() throws SQLException {
        List<T> objectList = new ArrayList<>();

        try (Connection conn = database.getConnection();
                ResultSet result = conn.prepareStatement("SELECT * FROM " + tableName).executeQuery()) {

            while (result.next()) {
                objectList.add(createFromRow(result));
            }
        }

        return objectList;
    }

    public abstract T createFromRow(ResultSet resultSet) throws SQLException;
    

}

package tikape.minifoorumi.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.domain.Viestiketju;

public class ViestiketjuDao extends AbstractNamedObjectDao<Viestiketju> {
   
    public ViestiketjuDao(Database database, String tableName) {
        super(database, tableName);
    }

    @Override
    public Viestiketju createFromRow(ResultSet resultSet) throws SQLException {
        return new Viestiketju(resultSet.getInt("id"), resultSet.getString("aihe"));
    }
    
    @Override
    public Viestiketju save(Viestiketju viestiketju) throws SQLException {
            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viestiketju"
                    + "(aihe) "
                    + "VALUES (?)");
                stmt.setString(1, viestiketju.getAihe());
            
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            
            // todo
            return null;
        }
    }
}

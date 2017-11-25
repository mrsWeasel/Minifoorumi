package tikape.minifoorumi.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.domain.Viesti;

public class ViestiDao extends AbstractNamedObjectDao<Viesti>  {
    public ViestiDao(Database database, String tableName) {
        super(database, tableName);
    }
        
    public Viesti save(Viesti viesti) throws SQLException {
        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viesti"
                    + "(viestiketju_id, kirjoittaja, sisalto, aika) "
                    + "VALUES (?, ?, ?, ?)");
            stmt.setInt(1, viesti.getViestiketjuId());
            stmt.setString(2, viesti.getKirjoittaja());
            stmt.setString(3, viesti.getSisalto());
            stmt.setDate(4, viesti.getAika());
            
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            // todo
            return null;
        }
    }    

    @Override
    public Viesti createFromRow(ResultSet resultSet) throws SQLException {
        return new Viesti(resultSet.getInt("id"), resultSet.getInt("viestiketju_id"), resultSet.getString("kirjoittaja"), resultSet.getString("sisalto"), resultSet.getDate("aika"));
    }
}

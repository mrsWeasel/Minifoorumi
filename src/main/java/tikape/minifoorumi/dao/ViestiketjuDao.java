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
    
    public Viestiketju findCreated(String aihe) throws SQLException {
        try (Connection conn = database.getConnection()) {
            // Haetaan viestiketju, jonka aihe täsmää ja joka ei sisällä vielä viestejä (siltä varalta,
            // että useammalla ketjulla onkin sama aihe)
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viestiketju WHERE aihe = ? "
                    + "AND id NOT IN (SELECT viestiketju_id FROM Viesti)");
            stmt.setString(1, aihe);
            ResultSet rs = stmt.executeQuery();
            
            rs.next();
            
            Viestiketju viestiketju = new Viestiketju(rs.getInt("id"), rs.getString("aihe"));
            return viestiketju;
        }
    }
    
    @Override
    public Viestiketju save(Viestiketju vk) throws SQLException {
            try (Connection conn = database.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viestiketju"
                    + "(aihe) "
                    + "VALUES (?)");
                stmt.setString(1, vk.getAihe());
            
                stmt.executeUpdate();
                stmt.close();
                conn.close();
            
            // todo
            Viestiketju viestiketju = findCreated(vk.getAihe());
            return viestiketju;
        }
    }
}

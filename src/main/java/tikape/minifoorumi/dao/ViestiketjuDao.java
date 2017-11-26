package tikape.minifoorumi.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.domain.Viestiketju;

public class ViestiketjuDao extends AbstractNamedObjectDao<Viestiketju> {
   
    public ViestiketjuDao(Database database, String tableName) {
        super(database, tableName);
    }

    @Override
    public Viestiketju createFromRow(ResultSet resultSet) throws SQLException {
        Integer viestimaara = getMessageCount(resultSet.getInt("id"));
        return new Viestiketju(resultSet.getInt("id"), resultSet.getString("aihe"), viestimaara);
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
            Integer viestimaara = getMessageCount(rs.getInt("id"));
            Viestiketju viestiketju = new Viestiketju(rs.getInt("id"), rs.getString("aihe"), viestimaara);
            stmt.close();
            rs.close();
            conn.close();
            return viestiketju;
        }
    }
    
    public Integer getMessageCount(Integer key) throws SQLException {
        
        try (Connection conn = database.getConnection()) {
            Integer count;
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) AS count FROM Viesti WHERE viestiketju_id = ?");
                stmt.setInt(1, key);
                ResultSet rs = stmt.executeQuery();
                
                rs.next();
                count = rs.getInt("count");
                return count;

                
        }
    }
    
    public List<Viestiketju> findAllAndReorder() throws SQLException {
        List<Viestiketju> viestit = new ArrayList<>();

        try (Connection conn = database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM Viestiketju, Viesti "
                    + "WHERE Viestiketju.id = Viesti.viestiketju_id "
                    + "GROUP BY Viestiketju.id "
                    + "ORDER BY Viesti.aika DESC");
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                viestit.add(createFromRow(rs));
            }
            stmt.close();
            rs.close();
            conn.close();
        }
        
        return viestit;
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

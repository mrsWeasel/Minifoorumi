package tikape.minifoorumi.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import tikape.minifoorumi.database.Database;
import tikape.minifoorumi.domain.Viesti;

public class ViestiDao extends AbstractNamedObjectDao<Viesti>  {
    public ViestiDao(Database database, String tableName) {
        super(database, tableName);
    }
        
    public Viesti save(Viesti viesti) throws SQLException {
        try (Connection conn = database.getConnection()) {
            SimpleDateFormat aikaFormaatti = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String aika = aikaFormaatti.format(viesti.getAika());
            
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Viesti"
                    + "(viestiketju_id, kirjoittaja, sisalto, aika) "
                    + "VALUES (?, ?, ?, ?)");
            stmt.setInt(1, viesti.getViestiketjuId());
            stmt.setString(2, viesti.getKirjoittaja());
            stmt.setString(3, viesti.getSisalto());
            stmt.setString(4, aika);
            
            stmt.executeUpdate();
            stmt.close();
            conn.close();
            
            // todo
            return null;
        }
    }    
    
    public List<Viesti> findAllFromThread(Integer key) throws SQLException {
        try (Connection conn = database.getConnection()) {
            List<Viesti> viestit = new ArrayList<>();
            
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Viesti WHERE viestiketju_id = ?");
            stmt.setInt(1, key);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                viestit.add(new Viesti(rs.getInt("id"), rs.getInt("viestiketju_id"), rs.getString("kirjoittaja"), rs.getString("sisalto"), rs.getDate("aika")));          
            }
            
            return viestit;
        }
    }
    
    @Override
    public Viesti createFromRow(ResultSet resultSet) throws SQLException {
        return new Viesti(resultSet.getInt("id"), resultSet.getInt("viestiketju_id"), resultSet.getString("kirjoittaja"), resultSet.getString("sisalto"), resultSet.getDate("aika"));
    }
}

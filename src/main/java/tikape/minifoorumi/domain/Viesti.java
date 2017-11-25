package tikape.minifoorumi.domain;
import java.sql.Date;

public class Viesti extends AbstractNamedObject {
    private Integer viestiketju_id;
    private String kirjoittaja;
    private String sisalto;
    private Date aika;
    
    public Viesti (Integer id, Integer viestiketju_id, String kirjoittaja, String sisalto, Date aika) {
        super(id);
        this.viestiketju_id = viestiketju_id;
        this.kirjoittaja = kirjoittaja;
        this.sisalto = sisalto;
        this.aika = aika;
    }
    public Integer getId() {
        return id;
    }
    
    public Integer getViestiketjuId() {
        return this.viestiketju_id;
    }
    
    public String getKirjoittaja() {
        return this.kirjoittaja;
    }
    
    public String getSisalto() {
        return this.sisalto;
    }
    
    public Date getAika() {
        return this.aika;
    }
    
    
  
}

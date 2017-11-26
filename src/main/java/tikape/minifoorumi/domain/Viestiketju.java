package tikape.minifoorumi.domain;

import java.util.List;


public class Viestiketju extends AbstractNamedObject {
    private String aihe;
    private Integer viestimaara;
    
    public Viestiketju(Integer id, String aihe, Integer viestimaara) {
        super(id);
        this.aihe = aihe;
        this.viestimaara = viestimaara;
    }
    
    public String getAihe() {
        return this.aihe;
    }
    
    public Integer getViestimaara() {
        return this.viestimaara;
    }
}

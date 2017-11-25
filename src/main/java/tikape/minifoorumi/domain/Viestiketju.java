package tikape.minifoorumi.domain;


public class Viestiketju extends AbstractNamedObject {
    private String aihe;
    
    public Viestiketju(Integer id, String aihe) {
        super(id);
        this.aihe = aihe;
    }
    
    public String getAihe() {
        return this.aihe;
    }
}

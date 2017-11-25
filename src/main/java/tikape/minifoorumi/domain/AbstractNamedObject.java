package tikape.minifoorumi.domain;

public abstract class AbstractNamedObject {

    protected Integer id;

    public AbstractNamedObject(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

}


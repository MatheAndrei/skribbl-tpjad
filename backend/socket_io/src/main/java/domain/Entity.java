package domain;

public abstract class Entity {
    protected String id = null;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}

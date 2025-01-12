package domain;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class BaseEntity<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected T id = null;

    public T getId() {
        return this.id;
    }

    public void setId(T id) {
        this.id = id;
    }
    
}

package domain;
import java.util.Objects;

public class DrawnImage {
    private String image; ///need change

    public DrawnImage() {
    }

    public DrawnImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{" +
            " image='" + getImage() + "'" +
            "}";
    }
    
}

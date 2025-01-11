package domain;
import java.util.Objects;

public class Turn  extends Entity{
    private User drawerUser;
    private Word currentWord;
    private DrawnImage image;


    public Turn() {
    }

    public Turn(User drawerUser, Word currentWord, DrawnImage image) {
        this.drawerUser = drawerUser;
        this.currentWord = currentWord;
        this.image = image;
    }

    public User getDrawerUser() {
        return this.drawerUser;
    }

    public void setDrawerUser(User drawerUser) {
        this.drawerUser = drawerUser;
    }

    public Word getCurrentWord() {
        return this.currentWord;
    }

    public void setCurrentWord(Word currentWord) {
        this.currentWord = currentWord;
    }

    public DrawnImage getImage() {
        return this.image;
    }

    public void setImage(DrawnImage image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "{" +
            " drawerUser='" + getDrawerUser() + "'" +
            ", currentWord='" + getCurrentWord() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
    
}

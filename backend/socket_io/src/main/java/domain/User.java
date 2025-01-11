package domain;
import java.util.Objects;

public class User extends Entity{
    private String username;

    private Boolean hasGuessed;
    private Boolean isDrawer;
    private Boolean isHost;

    public User() {
    }

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        this.hasGuessed = false;
        this.isDrawer = false;
        this.isHost = false;
    }

    public User(String id, String username, Boolean hasGuessed, Boolean isDrawer, Boolean isHost) {
        this.id = id;
        this.username = username;
        this.hasGuessed = hasGuessed;
        this.isDrawer = isDrawer;
        this.isHost = isHost;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean hasGuessed() {
        return this.hasGuessed;
    }

    public void setHasGuessed(Boolean hasGuessed) {
        this.hasGuessed = hasGuessed;
    }

    public Boolean isDrawer() {
        return this.isDrawer;
    }

    public void setIsDrawer(Boolean isDrawer) {
        this.isDrawer = isDrawer;
    }

    public Boolean isIsHost() {
        return this.isHost;
    }

    public void setIsHost(Boolean isHost) {
        this.isHost = isHost;
    }


    public User(String username, Boolean hasGuessed, Boolean isDrawer, Boolean isHost) {
        this.username = username;
        this.hasGuessed = hasGuessed;
        this.isDrawer = isDrawer;
        this.isHost = isHost;
    }

    public Boolean isHasGuessed() {
        return this.hasGuessed;
    }

    public Boolean getHasGuessed() {
        return this.hasGuessed;
    }


    public Boolean isIsDrawer() {
        return this.isDrawer;
    }

    public Boolean getIsDrawer() {
        return this.isDrawer;
    }


    public Boolean getIsHost() {
        return this.isHost;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username) && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id);
    }

    @Override
    public String toString() {
        return "{" +
            " username='" + getUsername() + "'" +
            ", hasGuessed='" + isHasGuessed() + "'" +
            ", isDrawer='" + isIsDrawer() + "'" +
            ", isHost='" + isIsHost() + "'" +
            "}";
    }    
    
    // public SocketIOClient getClient() {
    //     return this.client;
    // }

    // public void setClient(SocketIOClient client) {
    //     this.client = client;
    // }
}

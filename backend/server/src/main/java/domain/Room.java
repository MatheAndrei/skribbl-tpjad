package domain;

import java.util.List;

import domain.enums.RoomStatus;
import java.util.Objects;

public class Room extends BaseEntity<String>{
    private User host;
    private List<User> players;
    private Match match;
    private RoomStatus status;
    private RoomSettings settings;
    private Integer timer;

    public Room() {
    }

    public Room(User host, List<User> players, Match match, RoomStatus status, RoomSettings settings) {
        this.host = host;
        this.players = players;
        this.match = match;
        this.status = status;
        this.settings = settings;
    }

    public User getHost() {
        return this.host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    public List<User> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public Match getMatch() {
        return this.match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public RoomStatus getStatus() {
        return this.status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public RoomSettings getSettings() {
        return this.settings;
    }

    public void setSettings(RoomSettings settings) {
        this.settings = settings;
    }

    public Integer getTimer() {
        return this.timer;
    }

    public void setTimer(Integer timer) {
        this.timer = timer;
    }
    public void decrementTimer(){
        this.timer--;
    }

    @Override
    public String toString() {
        return "{" +
            " host='" + getHost() + "'" +
            ", players='" + getPlayers() + "'" +
            ", match='" + getMatch() + "'" +
            ", status='" + getStatus() + "'" +
            ", settings='" + getSettings() + "'" +
            "}";
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(host, room.host) && Objects.equals(players, room.players) && id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, players, id);
    }

    
}

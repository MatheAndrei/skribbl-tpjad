package domain;

import java.util.List;

import domain.enums.RoomStatus;

public class Room extends Entity{
    private User host;
    private List<User> players;
    private Match match;
    private RoomStatus status;
    private RoomSettings settings;
}

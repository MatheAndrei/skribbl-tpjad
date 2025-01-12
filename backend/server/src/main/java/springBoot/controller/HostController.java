package springBoot.controller;

import domain.Room;
import domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springBoot.socket_io.SessionService;


/*
Request:
POST /rooms
BODY
username
Response:
STATUS: 200
	Body: Code room

 */
@RestController
public class HostController {

    @Autowired
    private SessionService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(String username){
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("Username is required.", HttpStatus.BAD_REQUEST);
        }

        Room room = roomService.createRoom(new User(null,username));
        return new ResponseEntity<>(room.getId(), HttpStatus.OK);
    }

}

package springBoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import domain.Room;
import springBoot.socket_io.SessionService;



/*
Join
    Request:
        POST /rooms/{id}
        BODY
            username
    Response:
	    Status: OK / NOT_FOUND
	    Body:
            room data
            players
 */





@RestController
public class JoinController {

    @Autowired
    private SessionService roomService;

    @PostMapping("/rooms/{id}")
    public ResponseEntity<Room> createRoom(String username){
        if (username == null || username.isEmpty()) {
            return (ResponseEntity<Room>) ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }

        return null;
    }
}

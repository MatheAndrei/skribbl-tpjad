package springBoot.controller;

import domain.Room;
import domain.User;
import springBoot.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class HostController {

    @Autowired
    private SessionService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody String username){
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("Username is required.", HttpStatus.BAD_REQUEST);
        }

        Room room = roomService.hostRoom(username);
        return new ResponseEntity<>(room.getId(), HttpStatus.OK);
    }

}

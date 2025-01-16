package springBoot.controller;

import domain.RoomSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springBoot.service.SessionService;

@RestController
@CrossOrigin(origins = "*")
public class StartController {

    @Autowired
    private SessionService roomService;

    @PutMapping("/rooms/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<String> startRoom(@PathVariable String id, @RequestBody RoomSettings roomSettings){
        if (id == null || id.isEmpty()) {
            return new ResponseEntity<>("Incorrect room id ", HttpStatus.BAD_REQUEST);
        }
        roomService.addRoomSettings(id, roomSettings);
        return new ResponseEntity(HttpStatus.OK);
    }
}

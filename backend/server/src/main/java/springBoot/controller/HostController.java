package springBoot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Room;
import domain.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import springBoot.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*")
@RestController
public class HostController {

    @Autowired
    private SessionService roomService;

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody String username){
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>("Username is required.", HttpStatus.BAD_REQUEST);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(username);
            username = jsonNode.get("username").asText();
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(username);
        Room room = roomService.hostRoom(username);
        return new ResponseEntity<>(room.getId(), HttpStatus.OK);
    }

}

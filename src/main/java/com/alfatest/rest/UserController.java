package com.alfatest.rest;

import com.alfatest.domain.User;
import com.alfatest.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping("/")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello test");
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST,consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> possUser(@RequestBody User user){

        try {
            userRepository.save(user);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(URI.create("/users/"+user.getId())).build();
    }

    @RequestMapping(path = "/users/{login}/{pass}")
    public ResponseEntity<User> getByLoginAndPass(@PathVariable("login") String login, @PathVariable("pass") String pass){

        return userRepository.findByLoginAndPassword(login,pass)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<User>(HttpStatus.NOT_FOUND));
    }
}

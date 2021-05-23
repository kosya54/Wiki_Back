package com.kosenko.wikirest.controller;

import com.kosenko.wikirest.entity.User;
import com.kosenko.wikirest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserCrudController {
    private final UserService userService;

    @Autowired
    public UserCrudController(UserService userService) {
        this.userService = userService;
    }

/*    @GetMapping(value = "/current")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(userService.getUserById(user.getId()), HttpStatus.OK);
    } */

    @PostMapping
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.addNewUser(user), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<User> read(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseEntity<List<User>> read() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<User> update(@PathVariable(name = "id") Long id, @Valid @RequestBody User user) {
        return new ResponseEntity<>(userService.updateUser(id, user), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('users:write')")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        userService.deleteUserById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.jelleglebbeek.pcparts.user;

import com.jelleglebbeek.pcparts.user.entities.CreateUserInitializeRequest;
import com.jelleglebbeek.pcparts.user.entities.CreateUserInitializeResponse;
import com.jelleglebbeek.pcparts.user.entities.CreateUserRequest;
import com.jelleglebbeek.pcparts.user.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController()
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/initialize")
    public @ResponseBody
    CreateUserInitializeResponse createInitializeUser(@RequestBody CreateUserInitializeRequest request) throws NoSuchAlgorithmException {
        return userService.createInitialize(request);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    User createUser(@RequestBody CreateUserRequest request) throws GeneralSecurityException {
        return userService.create(request);
    }

    @GetMapping()
    public @ResponseBody
    Iterable<User> getUsersByPlatform() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody
    User getUser(@PathVariable UUID id) {
        return userService.findOne(id);
    }

    @PatchMapping("/{id}")
    public @ResponseBody
    User updateUser(
            @PathVariable UUID id,
            @RequestBody User user
    ) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }
}

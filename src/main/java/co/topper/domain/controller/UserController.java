package co.topper.domain.controller;

import co.topper.domain.data.dto.UserDto;
import co.topper.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static co.topper.configuration.constants.ControllerConstants.APP;
import static co.topper.configuration.constants.ControllerConstants.USER;
import static co.topper.configuration.constants.ControllerConstants.VERSION;

@RestController
@RequestMapping(APP + VERSION + USER)
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

}

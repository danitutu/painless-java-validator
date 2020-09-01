package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ctrl {

    @Autowired
    private final UserService userService;
    @Autowired
    private final CompactUserService compactUserService;

    public Ctrl(UserService userService, CompactUserService compactUserService) {
        this.userService = userService;
        this.compactUserService = compactUserService;
    }

    @PostMapping
    public void save(@RequestBody User input) {
        compactUserService.updateUser(input);
    }

//    @ExceptionHandler(value = {ValidationException.class})
//    public List<Violation> handleConstraintViolation(ValidationException ex, WebRequest request) {
//        return ex.getViolations();
//    }

}

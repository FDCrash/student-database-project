package com.netcracker.denisik.controllers;

import com.netcracker.denisik.services.implementations.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SignUpController {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    @Autowired
    public SignUpController(PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @ApiOperation(value = "Update faculty", nickname = "FacultyController.updateFaculty")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Faculty update")})
    @PutMapping("/signUp")
    public ResponseEntity<Object> signUp(long studentId, String login, String password) {
        String hashPassword = passwordEncoder.encode(password);
        if (!userService.registration(studentId, login, hashPassword)) {
            return new ResponseEntity<>("Registration error(wrong data)", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Registration is successful", HttpStatus.OK);
    }
}

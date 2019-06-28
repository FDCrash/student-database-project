package com.netcracker.denisik.controllers;

import com.netcracker.denisik.details.UserDetailsImpl;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.services.implementations.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Api
@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController{
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    @Autowired
    public AccountController(PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @ApiOperation(value = "Get my login", nickname = "AccountController.getLogin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Login")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map> getUser() {
        String login = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        log.debug("Login is = {}" , login);
        return new ResponseEntity<>(Collections.singletonMap("user", login), HttpStatus.OK);
    }

    @ApiOperation(value = "Update login", nickname = "AccountController.updateLogin")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Login update")})
    @PutMapping(value = "login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateLogin(@RequestParam("login") String login) {
        UserDTO userDTO = userService.get(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        userDTO.setLogin(login);
        long id = userService.add(userDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Update password", nickname = "Account.updatePassword")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Password update")})
    @PutMapping(value = "password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePassword(@RequestParam("Old password") String oldPassword, @RequestParam("New password") String newPassword) {
        UserDTO userDTO = userService.get(((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        if (passwordEncoder.matches(oldPassword,userDTO.getPassword())) {
            userDTO.setPassword(passwordEncoder.encode(newPassword));
        }else{
            return new ResponseEntity<>(Collections.singletonMap("Exception","Wrong password"), HttpStatus.BAD_REQUEST);
        }
        long id = userService.save(userDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
package com.netcracker.denisik.controllers;

import com.netcracker.denisik.dto.RoleDTO;
import com.netcracker.denisik.dto.UserDTO;
import com.netcracker.denisik.dto.UserFormDTO;
import com.netcracker.denisik.services.implementations.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping("/users")
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userService;

    @Autowired
    public UserController(PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @ApiOperation(value = "Gets all users", nickname = "UserController.getAllUsers")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Users")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAll();
        if (CollectionUtils.isEmpty(users)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Get specific user", nickname = "UserController.getUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") long id) {
        UserDTO user = userService.get(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Add admin", nickname = "UserController.addUserAdmin")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Admin is adding")})
    @PostMapping(value = "admins", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addAdmin(@RequestBody UserFormDTO userFormDTO) {
        UserDTO userDTO = UserDTO.builder()
                .id(userFormDTO.getId())
                .login(userFormDTO.getLogin())
                .password(passwordEncoder.encode(userFormDTO.getPassword()))
                .name(userFormDTO.getName())
                .roleDTO(RoleDTO.ADMIN)
                .build();
        long id = userService.add(userDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add employee", nickname = "UserController.addUserEmployee")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Employee is adding")})
    @PostMapping(value = "employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addEmployee(@RequestBody UserFormDTO userFormDTO) {
        UserDTO userDTO = UserDTO.builder()
                .id(userFormDTO.getId())
                .login(userFormDTO.getLogin())
                .password(passwordEncoder.encode(userFormDTO.getPassword()))
                .name(userFormDTO.getName())
                .roleDTO(RoleDTO.EMPLOYEE)
                .build();
        long id = userService.add(userDTO);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update user", nickname = "UserController.updateUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User update")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateUser(@RequestBody UserFormDTO userFormDTO) {
        UserDTO user = userService.get(userFormDTO.getId());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserDTO userDTO = UserDTO.builder()
                .id(userFormDTO.getId())
                .login(userFormDTO.getLogin())
                .password(passwordEncoder.encode(userFormDTO.getPassword()))
                .name(userFormDTO.getName())
                .roleDTO(user.getRoleDTO())
                .build();
        long id = userService.add(userDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete user", nickname = "UserController.deleteUser")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User is deleted")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Gets all admins", nickname = "UserController.getAllAdmins")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Admins")})
    @GetMapping(value = "/admins", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        List<UserDTO> user = userService.getAllAdmins();
        if (CollectionUtils.isEmpty(user)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets all employees", nickname = "UserController.getAllEmployees")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Employees")})
    @GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        List<UserDTO> users = userService.getAllEmployees();
        if (users == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}



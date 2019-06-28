package com.netcracker.denisik.controllers;

import com.netcracker.denisik.details.UserDetailsImpl;
import com.netcracker.denisik.dto.StudentDTO;
import com.netcracker.denisik.dto.WriteBookDTO;
import com.netcracker.denisik.services.implementations.StudentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api
@RestController
@RequestMapping("/student")
public class StudentUserController {

    private final StudentServiceImpl studentService;

    @Autowired
    public StudentUserController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @ApiOperation(value = "Get write book by student", nickname = "StudentUserController.getWriteBook")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "WriteBook")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WriteBookDTO> getWriteBook() {
        long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        WriteBookDTO writeBook = studentService.get(userId).getWriteBook();
        if (writeBook == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(writeBook, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets all students by your speciality", nickname = "StudentUserController.getAllStudentsBySpeciality")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "StudentsBySpeciality")})
    @GetMapping(value = "/allBySpeciality", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity< List<StudentDTO>> getAllStudentsBySpeciality() {
        long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<StudentDTO> student = studentService.getAllBySpeciality(studentService.get(userId).getSpeciality());
        if (CollectionUtils.isEmpty(student)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @ApiOperation(value = "Gets all students by your group", nickname = "StudentUserController.getAllStudentsByGroup")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "StudentsByGroup")})
    @GetMapping(value = "/allByGroupId", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StudentDTO>> getAllStudentsByGroup() {
        long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<StudentDTO> student = studentService.getAllByGroup(studentService.get(userId).getGroupId());
        if (CollectionUtils.isEmpty(student)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(student, HttpStatus.OK);
    }
}

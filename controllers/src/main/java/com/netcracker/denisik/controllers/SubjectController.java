package com.netcracker.denisik.controllers;

import com.netcracker.denisik.dto.SubjectDTO;
import com.netcracker.denisik.services.implementations.SubjectServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api
@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectServiceImpl subjectService;

    @Autowired
    public SubjectController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @ApiOperation(value = "Gets all subjects", nickname = "SubjectController.getAllSubjects")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Subjects")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SubjectDTO>> getAllSubjects() {
        List<SubjectDTO> subject = subjectService.getAll();
        if (CollectionUtils.isEmpty(subject)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subject, HttpStatus.OK);
    }

    @ApiOperation(value = "Get specific subject", nickname = "SubjectController.getSubject")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Subject")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SubjectDTO> getSubject(@PathVariable("id") long id) {
        SubjectDTO subject = subjectService.get(id);
        if (subject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subject, HttpStatus.OK);
    }

    @ApiOperation(value = "Add subject", nickname = "SubjectController.addSubject")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Subject is adding")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addSubject(@RequestBody SubjectDTO subject) {
        long id = subjectService.add(subject);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update subject", nickname = "SubjectController.updateSubject")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Subject update")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateStudent(@RequestBody SubjectDTO subject) {
        if (subjectService.get(subject.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        long id = subjectService.add(subject);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete subject", nickname = "SubjectController.deleteSubject")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Subject is deleted")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSubject(@PathVariable("id") long id) {
        subjectService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

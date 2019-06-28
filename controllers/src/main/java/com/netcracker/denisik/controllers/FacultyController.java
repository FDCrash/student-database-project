package com.netcracker.denisik.controllers;

import com.netcracker.denisik.dto.FacultyDTO;
import com.netcracker.denisik.services.implementations.FacultyServiceImpl;
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
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyServiceImpl facultyService;

    @Autowired
    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @ApiOperation(value = "Gets all faculties", nickname = "FacultyController.getAllFaculties")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Faculties")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FacultyDTO>> getAllFaculties() {
        List<FacultyDTO> faculty = facultyService.getAll();
        if (CollectionUtils.isEmpty(faculty)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(faculty, HttpStatus.OK);
    }

    @ApiOperation(value = "Get specific faculty", nickname = "FacultyController.getFaculty")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Faculty")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FacultyDTO> getFaculty(@PathVariable("id") long id) {
        FacultyDTO faculty = facultyService.get(id);
        if (faculty == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(faculty, HttpStatus.OK);
    }

    @ApiOperation(value = "Add faculty", nickname = "FacultyController.addFaculty")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Faculty is adding")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addFaculty(@RequestBody FacultyDTO faculty) {
        long id = facultyService.add(faculty);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update faculty", nickname = "FacultyController.updateFaculty")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Faculty update")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateFaculty(@RequestBody FacultyDTO faculty) {
        if (facultyService.get(faculty.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        long id = facultyService.add(faculty);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete faculty", nickname = "FacultyController.deleteFaculty")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Faculty is deleted")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFaculty(@PathVariable("id") long id) {
        facultyService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

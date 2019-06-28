package com.netcracker.denisik.controllers;

import com.netcracker.denisik.dto.SpecialityDTO;
import com.netcracker.denisik.services.implementations.SpecialityServiceImpl;
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
@RequestMapping("/specialities")
public class SpecialityController {
    private final SpecialityServiceImpl specialityService;

    @Autowired
    public SpecialityController(SpecialityServiceImpl specialityService) {
        this.specialityService = specialityService;
    }

    @ApiOperation(value = "Gets all specialities", nickname = "SpecialityController.getAllSpecialities")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Specialities")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SpecialityDTO>> getAllSpecialities() {
        List<SpecialityDTO> speciality = specialityService.getAll();
        if (CollectionUtils.isEmpty(speciality)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(speciality, HttpStatus.OK);
    }

    @ApiOperation(value = "Get specific speciality", nickname = "SpecialityController.getSpeciality")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Speciality")})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpecialityDTO> getSpeciality(@PathVariable("id") long id) {
        SpecialityDTO speciality = specialityService.get(id);
        if (speciality == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(speciality, HttpStatus.OK);
    }

    @ApiOperation(value = "Add speciality", nickname = "SpecialityController.addSpeciality")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Speciality is adding")})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> addSpeciality(@RequestBody SpecialityDTO speciality) {
        long id = specialityService.add(speciality);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update speciality", nickname = "SpecialityController.updateSpeciality")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Speciality update")})
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateSpeciality(@RequestBody SpecialityDTO speciality) {
        if (specialityService.get(speciality.getId()) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        long id = specialityService.add(speciality);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete speciality", nickname = "SpecialityController.deleteSpeciality")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Speciality is deleted")})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteSpeciality(@PathVariable("id") long id) {
        specialityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

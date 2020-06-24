package com.utn.rest.controller;

import com.utn.rest.model.LoginRequestDto;
import com.utn.rest.model.UserDto;
import com.utn.rest.service.IntegrationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class Controller {

    @Autowired
    IntegrationService integrationService;

    /*@GetMapping("/pet")
    @ApiOperation(value="Trae una mascota")
    @ApiResponses({
            @ApiResponse(code=200,message = "Success")
    })
    public Pet getPet() {
        return integrationService.getPet();
    }*/
    @GetMapping("/user")
    @ApiOperation(value="Trae un user por ID")
    @ApiResponses({
            @ApiResponse(code=200,message = "Success")
    })
    public UserDto getUser() {
        return integrationService.getUserByIdFromApi();
    }
    @GetMapping("/string")
    @ApiOperation(value="Trae string que dice hola")
    @ApiResponses({
            @ApiResponse(code=200,message = "Success")
    })
    public String getString() {
        return integrationService.getStringFromApi();
    }

    @PostMapping("/login")
    @ApiOperation(value="Se loguea con un User")
    @ApiResponses({
            @ApiResponse(code=200,message = "Success")
    })
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) {
        return integrationService.login(loginRequestDto);
    }
}

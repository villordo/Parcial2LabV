package com.utn.rest.controller;

import com.utn.rest.model.DateDto;
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


    @GetMapping("/invoices/{date}")
    @ApiOperation(value="Trae las facturas de una determinada fecha.")
    @ApiResponses({
            @ApiResponse(code=200,message = "Success."),
            @ApiResponse(code=204,message = "Sin contenido."),
            @ApiResponse(code=404,message = "Error de validacion.")
    })
    public ResponseEntity getInvoicesByDate(@PathVariable String date) {
        return integrationService.getInvoicesByDate(date);
    }
}
